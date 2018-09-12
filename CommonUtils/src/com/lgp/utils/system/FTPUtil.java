package com.lgp.utils.system;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.lgp.utils.file.FileUtils;

public class FTPUtil {
	
	private FTPClient ftp = new FTPClient();
	private ArrayList<String> arFiles = new ArrayList<String>();

	private static final Logger logger = Logger.getLogger(FTPUtil.class);

	public FTPClient fClient;

	private String username; // 登陆FTP 用户名

	private String password; // 用户密码，支持强密码
	private String url; // FTP 地址
	private int port;
	private String remoteDir;
	
	public FTPUtil(String username, String password, String url, int port,String remoteDir) {
		super();
		this.username = username;
		this.password = password;
		this.url = url;
		this.port = port;
		this.remoteDir = remoteDir;
		this.fClient = new FTPClient();
	}
	
	
	public boolean login(String host, int port, String username, String password)
			throws IOException {
		this.ftp.connect(host, port);
		if (FTPReply.isPositiveCompletion(this.ftp.getReplyCode())) {
			if (this.ftp.login(username, password)) {
				this.ftp.setControlEncoding("UTF-8");
				return true;
			}
		}
		if (this.ftp.isConnected()) {
			this.ftp.disconnect();
		}
		return false;
	}

	/**
	 * 关闭数据链接
	 * 
	 * @throws IOException
	 */
	public void disConnection() throws IOException {
		if (this.ftp.isConnected()) {
			this.ftp.disconnect();
		}
	}

	public void List(String pathName) throws IOException {
		if (pathName.startsWith("/")) {
			String directory = pathName;
			this.ftp.changeWorkingDirectory(new String(directory.getBytes("UTF-8"),FTPClient.DEFAULT_CONTROL_ENCODING));
			FTPFile[] files = this.ftp.listFiles();
			for (FTPFile file : files) {
				if (file.isFile()) {
					arFiles.add(directory + file.getName());
				} else if (file.isDirectory()) {
					System.out.println(ftp.printWorkingDirectory());
					ftp.changeWorkingDirectory(directory + file.getName()+"/"); 
					List(directory + file.getName()+"/");
				}
			}
		}
	}
	
	// Ftp登陆，注意此处host不能带ftp://,直接传入站点后半部分就可以了
	public FTPClient loginFtp(String host, String user, String pass) {
		int reply;
		boolean loginFlag = false;
		FTPClient ftp = new FTPClient();
		ftp.setControlEncoding("GBK");
		try {
			ftp.connect(host,21);
			System.out.println("Connected to " + host);
			System.out.print(ftp.getReplyString());
			reply = ftp.getReplyCode();
			ftp.setFileTransferMode(FTP.ASCII_FILE_TYPE);
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.err.println("FTP server refused connection.");
				System.exit(1);
			}
			loginFlag = ftp.login(user, pass);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (loginFlag) {
			System.out.println("login success");
			System.out.println("host" + host);
			return ftp;
		} else {
			System.out.println("login failed");
			return null;
		}
	}

	// 与ftp断开连接
	public void ftpClose(FTPClient ftp) {
		try {
			ftp.logout();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ftp.isConnected()) {
			try {
				ftp.disconnect();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		System.out.println("与ftp断开连接!");
	}

	// 遍历并打印ftp站点资源信息
	public void traverse(String host, FTPClient client, String path) {
		try {
			FTPFile[] files = client.listFiles(path);
			for (int i = 0; i < files.length; i++) {
				// 如果是文件夹就递归方法继续遍历
				if (files[i].isDirectory()) {
					/*
					 * 创建新目录时会自动创建两个文件名: . 和 .. 点指当前目录 点点指父目录
					 */
					// 注意这里的判断，否则会出现死循环
					if (!files[i].getName().equals(".")
							&& !files[i].getName().equals("..")) {
						String tempDir = client.printWorkingDirectory() + "/"
								+ files[i].getName();
						client.changeWorkingDirectory(tempDir);
						// 是文件夹就递归调用
						traverse(host, client, tempDir);
						client.changeToParentDirectory();
					}
					// 如果是文件就扫描信息
				} else {
					String temp = client.printWorkingDirectory();
					Date date = new Date();
					// 根目录下的文件
					if (temp.equals("/")) {
						date = files[i].getTimestamp().getTime();
						java.sql.Date date2 = new java.sql.Date(date.getTime());
						System.out.println("文件名:" + files[i].getName());
						System.out.println("文件路径:" + host + temp);
						System.out
								.println("文件大小:" + format(files[i].getSize()));
						System.out.println("最后修改日期:" + date2);
						System.out
								.println("-----------------------------------");
						// 不是根目录下的文件
					} else {
						// 得到文件的时间戳
						// 转换成保存到数据库的时间格式
						java.sql.Date date2 = new java.sql.Date(date.getTime());
						// 如果扫描是文件就将信息保存到数据库
						System.out.println("文件名:" + files[i].getName());
						System.out.println("文件路径:" + host + temp + "/");
						System.out
								.println("文件大小:" + format(files[i].getSize()));
						System.out.println("最后修改日期:" + date2);
						System.out
								.println("-----------------------------------");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 转换文件大小的方法
	public String format(long size) {
		if (size < 1024) {
			return size + "B";
		} else if (size < 1048576) {
			double beforeNum = size / 1024;
			double temp = size % 1024;
			double afterNum = temp / 1024;
			// 取小数点后1位
			double num = new BigDecimal(beforeNum + afterNum).setScale(1,
					BigDecimal.ROUND_HALF_UP).doubleValue();
			return num + "KB";
		} else if (size < 1073741824) {
			double beforeNum = size / 1048576;
			double temp = size % 1048576;
			double afterNum = temp / 1048576;
			double num = new BigDecimal(beforeNum + afterNum).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue();
			return num + "MB";
		} else {
			double beforeNum = size / 1073741824;
			double temp = size % 1073741824;
			double afterNum = temp / 1073741824;
			double num = new BigDecimal(beforeNum + afterNum).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue();
			return num + "GB";
		}
	}
	
	

	public enum UploadStatus {
		Create_Directory_Fail, // 远程服务器相应目录创建失败

		Create_Directory_Success, // 远程服务器闯将目录成功

		Upload_New_File_Success, // 上传新文件成功

		Upload_New_File_Failed, // 上传新文件失败

		File_Exits, // 文件已经存在
		Remote_Bigger_Local, // 远程文件大于本地文件
		Upload_From_Break_Success, // 断点续传成功
		Upload_From_Break_Failed, // 断点续传失败
		Delete_Remote_Faild; // 删除远程文件失败
	}

	/**
	 * 登陆FTP服务器

	 * 
	 * @throws Exception
	 */
	public boolean ftpLogin() {
		boolean flag = false;
		try {
			this.fClient.setControlEncoding("GBK");
			this.fClient.setDefaultPort(this.port);
			// this.fClient.setDataTimeout(120000); // timeout為120秒
			this.fClient.connect(url);
			
			if (!this.fClient.isConnected()) {
				throw new Exception("NOT   CONNECT   FTP ");
			}
			if (!FTPReply.isPositiveCompletion(this.fClient.getReplyCode())) {
				this.fClient.disconnect();
				System.out.println("Connection   refused. ");
			}
			if (this.fClient.login(this.username, this.password)) {
				logger.info("成功登陆ftp"+this.username+":"+this.password);
			} else {
				throw new Exception("NOT   LOGIN ");
			}
			this.fClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//			if(this.fClient.changeWorkingDirectory(this.remoteDir)){
//				logger.info("成功登陆FTP服务器：" + this.url + " 端口号：" + this.port + " 目录："
//						+ remoteDir);
//				flag = true;
//			} else if(this.fClient.makeDirectory(this.remoteDir)){
//				logger.info("创建工作空间成功：" + this.url + " 端口号：" + this.port + " 目录："
//						+ remoteDir);
//				flag = true;
//			}
//			else {
//				logger.error("失败登陆FTP服务器：" + this.url + " 端口号：" + this.port + " 目录："
//						+ remoteDir);
//				flag = false;
//			}
			if(this.remoteDir.contains("/")){
//					String remoteFileName = this.remoteDir; 
//	                remoteFileName = this.remoteDir.substring(this.remoteDir.lastIndexOf("/") + 1); 
	                // 创建服务器远程目录结构，创建失败直接返回 
	                if (createDirecroty(this.remoteDir) == false) { 
	                    System.err.println("shibai");
	                } else{
	                	flag = true;
	                }
	        }else if(this.remoteDir.contains(File.separator)){
	        	if(createWinDirecroty(this.remoteDir) == false){
	        		System.out.println("创建windows目录失败");
	        	}else{
	        		flag = true;
	        	}
	        }
			
		} catch (SocketException e) {
			logger.info("ftp登陆错误1:"+e.getMessage());
			e.printStackTrace();
			logger.info("FTP server refused connection." + url);
		} catch (Exception e) {
			logger.info("ftp登陆错误2:"+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	
	 public Boolean createDirecroty(String remote) throws IOException { 
		remote = FileUtils.changeFTPDir(remote);
    	Boolean a = true;
    	String directory = remote.substring(0, remote.lastIndexOf("/") + 1); 
	    	//if(!directory.equalsIgnoreCase("/")&&!this.fClient.changeWorkingDirectory(new String(directory.getBytes("GBK"), "iso-8859-1"))){   
	    if(!directory.equalsIgnoreCase("/") && !this.fClient.changeWorkingDirectory(directory)){
	    // 如果远程目录不存在，则递归创建远程服务器目录 
	    int start = 0; 
	    int end = 0; 
	    if (directory.startsWith("/")) { 
	        start = 1; 
	    } else { 
	        start = 0; 
	    } 
	    end = directory.indexOf("/", start); 
	    while (true) { 
	        String subDirectory = new String(remote.substring(start, end));
	        if (!this.fClient.changeWorkingDirectory(subDirectory)) { 
	            if (this.fClient.makeDirectory(subDirectory)) { 
	            	this.fClient.changeWorkingDirectory(subDirectory); 
	            } else { 
	                System.out.println("创建目录失败"); 
	                return false; 
	            } 
	        } 

	        start = end+1; 
	        end = directory.indexOf("/", start); 

	        // 检查所有目录是否创建完毕 
	        if (end <= start) { 
	            break; 
	        } 
	    } 
	} 
	    	return a; 
	} 
	 public  Boolean createWinDirecroty(String remote) throws IOException { 
		 	remote = FileUtils.changeRection(remote);
	    	Boolean a = true;
	    	String directory = remote.substring(0, remote.lastIndexOf(File.separator) + 1); 
	    	if (!directory.equalsIgnoreCase(File.separator) 
	    	        && !this.fClient.changeWorkingDirectory(directory )) { 
	    // 如果远程目录不存在，则递归创建远程服务器目录 
	    int start = 0; 
	    int end = 0; 
	    if (directory.startsWith(File.separator)) { 
	        start = 1; 
	    } else { 
	        start = 0; 
	    } 
	    end = directory.indexOf(File.separator, start); 
	    while (true) { 
	        String subDirectory = new String(remote.substring(start, end)); 
	        if (!this.fClient.changeWorkingDirectory(subDirectory)) { 
	            if (this.fClient.makeDirectory(subDirectory)) { 
	            	this.fClient.changeWorkingDirectory(subDirectory); 
	            } else { 
	                System.out.println("创建目录失败"); 
	                return false; 
	            } 
	        } 

	        start = end+1; 
	        end = directory.indexOf(File.separator, start); 

	        // 检查所有目录是否创建完毕 
	        if (end <= start) { 
	            break; 
	        } 
	    } 
	} 
	    	return a; 
	} 
	public boolean makeDir(String dir) throws IOException{
		return this.fClient.makeDirectory(dir);
	}
	
	public void listFile(){
		try {
			String[] str = this.fClient.listNames();
			for(String str1: str){
				logger.info(str1);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 关闭FTP 连接
	 */
	public void close() {
		if (null != this.fClient && this.fClient.isConnected()) {
			try {
				this.fClient.logout();
				this.fClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			logger.info("close FTP server .");
		}
	}
	
	public void uploadDirectory(String directory, String serverPath) throws IOException {
		File file = new File(directory);
		String name = "";// 待上传文件名
		if (file.isDirectory()) {// 如果为目录，则按目录传

			File[] files = file.listFiles();
			// 循环传递目录下的所有文件与目录
			int i = 0;
			for (i = 0; i < files.length; i++) {
				File tmpFile = files[i];

				if (tmpFile.isDirectory()){
					uploadDirectory(tmpFile.getAbsolutePath(), serverPath);
				} else {
					name = tmpFile.getName();
					if(name.indexOf(".vax") < 0)
						uploadFile(directory + "/" + name, name);
				}
			}
		} else
			// 如果为文件，则按文件上传
			if(name.indexOf(".vax") < 0)
				uploadFile(directory + "/" + name, file.getName());
	}	
	
	/**
	 * 上传文件
	 * 
	 * @param localFilePath
	 *            本地文件路径及名称

	 * @param remoteFileName
	 *            FTP 服务器文件名称

	 * @return
	 */
	public boolean uploadFile(String localFilePath, String remoteFileName) {
		BufferedInputStream inStream = null;
		boolean success = false;
		try {
			inStream = new BufferedInputStream(new FileInputStream(
					localFilePath));
			success = this.fClient.storeFile(remoteFileName, inStream);
			logger.info("上传的结果为："+success);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	/**
	 * 下载文件
	 * 
	 * @param localFilePath
	 *            本地文件名及路径
	 * @param remoteFileName
	 *            远程文件名称
	 * @return
	 */
	public boolean downloadFile(String localFilePath, String remoteFileName) {
		BufferedOutputStream outStream = null;
		boolean success = false;
		try {
			outStream = new BufferedOutputStream(new FileOutputStream(
					localFilePath));
			success = this.fClient.retrieveFile(remoteFileName, outStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	public boolean downloadFileStreamByStartPos(long startPos, long endPos,
			String localFilePath, String remoteFileName) throws Exception {
		BufferedOutputStream outStream = null;
		InputStream is = null;
		boolean success = false;

		RandomAccessFile raf = null;

		try {
			// outStream = new BufferedOutputStream(new FileOutputStream(
			// localFilePath));
			raf = new RandomAccessFile(localFilePath, "rw");

			raf.seek(startPos);

			is = this.fClient.retrieveFileStream(remoteFileName);

			is.skip(startPos);

			long needRead = endPos - startPos;

			byte[] bytes = new byte[1 << 16];
			int c;
			long hasTrans = 0;

			while ((c = is.read(bytes)) != -1) {

				if ((needRead - hasTrans) > c) {

					raf.write(bytes, 0, c);

					hasTrans = hasTrans + c;
				} else {
					raf.write(bytes, 0, (int) (needRead - hasTrans));
					break;
				}
			}

			success = true;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return success;
	}

	public boolean downloadFileStreamByTask(int taskID, int taskIdex,
			String localFilePath, String remoteFileName) throws Exception {
		BufferedOutputStream outStream = null;
		InputStream is = null;
		boolean success = false;
		try {
			long remoteFileSize = getFileSize(remoteFileName);

			outStream = new BufferedOutputStream(new FileOutputStream(
					localFilePath));
			is = this.fClient.retrieveFileStream(remoteFileName);

			byte[] bytes = new byte[1 << 16];
			int c;
			Date dbegin = new Date();
			long total100 = 0;
			long hasTrans = 0;
			while ((c = is.read(bytes)) != -1) {
				outStream.write(bytes, 0, c);

				total100 = total100 + c;
				hasTrans = hasTrans + c;
				if (1024 * 1024 * 500 < total100) {
					System.out.print((hasTrans / (1024 * 1024)) + "M ");
					total100 = 0;
					Date dend = new Date();
					System.out.print((hasTrans / (1024 * 1024))
							/ ((dend.getTime() - dbegin.getTime()) / 1000)
							+ "M/S ");
					System.out.print((hasTrans * 100 / remoteFileSize) + "% ");

					System.out.print(" 剩余:"
							+ ((remoteFileSize - hasTrans) / (hasTrans / ((dend
									.getTime() - dbegin.getTime()) / 1000)))
							/ 60 + "分钟");

					System.out.println();
				}
			}

			success = true;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return success;
	}

	public long getFileSize(String fileName) {
		long fileSize = 0;

		FTPFile[] fs;
		try {
			fs = this.fClient.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName))
					return fileSize = ff.getSize();
			}
		} catch (IOException e) {
			logger.error("获取文件列表", e);
		}

		return fileSize;
	}
	
	public boolean reName(String from, String to){
		try{
			return this.fClient.rename(from, to);
		} catch(Exception e){
			logger.error("修改ftp文件名称出错：" , e);
			return false;
		}
	}
	/**
	 * 获得FTP 服务器下所有的文件名列表

	 * 
	 * @param regex
	 * @return
	 */
	public String[] getListFiels(String regex) {
		String files[] = new String[0];
		try {
			files = this.fClient.listNames(regex);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return files;
	}

	/**
	 * 返回上一级目录(父目录)
	 */
	public void ToParentDir() {
		try {
			while(true){
				String workspace = this.fClient.printWorkingDirectory();
				if( workspace.endsWith("prevs")){
					this.fClient.changeToParentDirectory();
					break ;
				}
				this.fClient.changeToParentDirectory();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回到上层目录。");
	}
public String getWorkspace(){
	String workspace = "";
	try{
		workspace = this.fClient.printWorkingDirectory();
	}catch(IOException e){
		e.printStackTrace();
	}
	return workspace;
}
//	public boolean ishasPrentSpace(String directory){
//		directory = FileUtils.changeDirection(directory);
//		logger.info("当前目录为："+ directory);
//		 	int start = 0; 
//		    int end = 0; 
//		  if (directory.startsWith(File.separator)) { 
//		        start = 1; 
//		    } else { 
//		        start = 0; 
//		    } 
//		    end = directory.indexOf(File.separator, start); 
//		   if (end <= start ){
//		    	return false;
//		    }else{
//		    	return true;
//		    }
//	}
  
	
	/**
	 * 变更工作目录
	 * 
	 * @param remoteDir
	 *            --目录路径
	 */
	public boolean changeDir(String remoteDir) {
		boolean flag = false;
		try {
			flag =	this.fClient.changeWorkingDirectory(remoteDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("变更工作目录为:" + remoteDir);
		return  flag;
	}

	/**
	 * 设置传输文件的类型[文本文件或者二进制文件]
	 * 
	 * @param fileType
	 *            --BINARY_FILE_TYPE,ASCII_FILE_TYPE
	 */
	public void setFileType(int fileType) {
		try {
			fClient.setFileType(fileType);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean deleteFile(String pathname) {
		boolean flag = false;
		try {
			flag = this.fClient.deleteFile(pathname);
		} catch (Exception e) {
			logger.info("删除FTP文件失败 文件名：" + this.remoteDir + pathname);
		}
		if (flag)
			logger.info("成功将文件删除 文件名：" + this.remoteDir + pathname);
		return flag;
	}

	/**
	 * 检查FTP 是否关闭 ，如果关闭打开FTP
	 * 
	 * @throws Exception
	 */
	public boolean openFtpConnection() {
		if (null == this.fClient)
			return false;
		boolean flag = true;
		try {
			if (!this.fClient.isConnected()) {
				flag = this.ftpLogin();
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 上传文件到FTP服务器，支持断点续传
	 * 
	 * @param local
	 *            本地文件名称，绝对路径

	 * @param remote
	 *            远程文件路径，使用/home/directory/subdirectory/file.ext或是
	 *            http://www.xx9x.cn/directory/subdirectory/file.ext
	 *            按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
	 * @return 上传结果
	 * @throws IOException
	 */
	public UploadStatus upload(String local, String remote) throws IOException {
		// 设置PassiveMode传输
		fClient.enterLocalPassiveMode();
		// 设置以二进制流的方式传输
		fClient.setFileType(FTP.BINARY_FILE_TYPE);
		fClient.setControlEncoding("GBK");
		UploadStatus result;
		// 对远程目录的处理
		String remoteFileName = remote;
		if (remote.contains("/")) {
			remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
			// 创建服务器远程目录结构，创建失败直接返回
			if (CreateDirecroty(remote, fClient) == UploadStatus.Create_Directory_Fail) {
				return UploadStatus.Create_Directory_Fail;
			}
		}
		// 检查远程是否存在文件

		FTPFile[] files = fClient.listFiles(new String(remoteFileName
				.getBytes("GBK"), "iso-8859-1"));
		if (files.length == 1) {
			long remoteSize = files[0].getSize();
			File f = new File(local);
			long localSize = f.length();
			if (remoteSize == localSize) {
				return UploadStatus.File_Exits;
			} else if (remoteSize > localSize) {
				return UploadStatus.Remote_Bigger_Local;
			}
			// 尝试移动文件内读取指针,实现断点续传
			result = uploadFile(remoteFileName, f, fClient, remoteSize);

			// 如果断点续传没有成功，则删除服务器上文件，重新上传

			if (result == UploadStatus.Upload_From_Break_Failed) {
				if (!fClient.deleteFile(remoteFileName)) {
					return UploadStatus.Delete_Remote_Faild;
				}
				result = uploadFile(remoteFileName, f, fClient, 0);
			}
		} else {
			Boolean success  = uploadNewFile(local, remote);
			if(success){
				result = UploadStatus.Upload_New_File_Success ;
			}else{
				result = UploadStatus.Upload_New_File_Failed;
			}
		}
		return result;
	}
	/**
	 * 上传文件到服务器,新上传和断点续传
	 * 
	 * @param remoteFile
	 *            远程文件名，在上传之前已经将服务器工作目录做了改变

	 * @param localFile
	 *            本地文件 File句柄，绝对路径

	 * @param processStep
	 *            需要显示的处理进度步进值

	 * @param ftpClient
	 *            FTPClient 引用
	 * @return
	 * @throws IOException
	 */
	public UploadStatus uploadFile(String remoteFile, File localFile,
			FTPClient ftpClient, long remoteSize) throws IOException {
		UploadStatus status;
		// 显示进度的上传

		long step = localFile.length() / 100;
		long process = 0;
		long localreadbytes = 0L;
		RandomAccessFile raf = new RandomAccessFile(localFile, "r");
		OutputStream out = ftpClient.appendFileStream(new String(remoteFile
				.getBytes("GBK"), "iso-8859-1"));
		// 断点续传
		if (remoteSize > 0) {
			ftpClient.setRestartOffset(remoteSize);
			process = remoteSize / step;
			raf.seek(remoteSize);
			localreadbytes = remoteSize;
		}
		byte[] bytes = new byte[1024];
		int c;
		while ((c = raf.read(bytes)) != -1) {
			out.write(bytes, 0, c);
			localreadbytes += c;
			if (localreadbytes / step != process) {
				process = localreadbytes / step;
				System.out.println("上传进度:" + process);
				// TODO 汇报上传状态

			}
		}
		out.flush();
		raf.close();
		out.close();
		boolean result = ftpClient.completePendingCommand();
		if (remoteSize > 0) {
			status = result ? UploadStatus.Upload_From_Break_Success
					: UploadStatus.Upload_From_Break_Failed;
		} else {
			status = result ? UploadStatus.Upload_New_File_Success
					: UploadStatus.Upload_New_File_Failed;
		}
		return status;
	}
	/**
	 * 递归创建远程服务器目录

	 * 
	 * @param remote
	 *            远程服务器文件绝对路径

	 * @param ftpClient
	 *            FTPClient 对象
	 * @return 目录创建是否成功
	 * @throws IOException
	 */
	public UploadStatus CreateDirecroty(String remote, FTPClient ftpClient)
			throws IOException {
		UploadStatus status = UploadStatus.Create_Directory_Success;
		String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
		if (!directory.equalsIgnoreCase("/")
				&& !ftpClient.changeWorkingDirectory(new String(directory
						.getBytes("GBK"), "iso-8859-1"))) {
			// 如果远程目录不存在，则递归创建远程服务器目录

			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			while (true) {
				String subDirectory = new String(remote.substring(start, end)
						.getBytes("GBK"), "iso-8859-1");
				if (!ftpClient.changeWorkingDirectory(subDirectory)) {
					if (ftpClient.makeDirectory(subDirectory)) {
						ftpClient.changeWorkingDirectory(subDirectory);
					} else {
						System.out.println("创建目录失败");
						return UploadStatus.Create_Directory_Fail;
					}
				}

				start = end + 1;
				end = directory.indexOf("/", start);

				// 检查所有目录是否创建完毕

				if (end <= start) {
					break;
				}
			}
		}
		return status;
	}

	public static void main(String[] args) {
		System.out.println(File.separator);

	}
	/**
	 * 上传文件
	 * 
	 * @param localFilePath
	 *            本地文件路径及名称


	 * @param remoteFileName
	 *            FTP 服务器文件名称


	 * @return
	 */
	public Boolean uploadNewFile(String localFilePath, String remoteFileName) {
		
		BufferedInputStream inStream = null;
		boolean success = false;
		try {
			inStream = new BufferedInputStream(new FileInputStream(
					localFilePath));
			success = this.fClient.storeFile(remoteFileName, inStream);
			System.out.println("上传的结果为："+success);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	
}
