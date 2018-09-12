package com.lgp.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.util.StringUtils;

public class FileUtils {

	public static String getFilePath(String fileName) {
		String path = "";
		if (StringUtils.isEmpty(fileName))
			return "";
		if (fileName.contains("\\") || fileName.contains("/")) {
			fileName = fileName.replaceAll("\\\\", "\\/");
			int k = fileName.lastIndexOf("/");
			if (k > 0) {
				path = fileName.substring(0, k + 1);
			}
		}
		return path;
	}

	public static String getFileNameOne(String fileName) {
		String name = "";
		if (StringUtils.isEmpty(fileName))
			return "";
		if (fileName.contains("\\") || fileName.contains("/")) {
			fileName = fileName.replaceAll("\\\\", "\\/");
			int k = fileName.lastIndexOf("/");
			if (k > 0) {
				name = fileName.substring(k + 1, fileName.length());
			}
		}
		return name;
	}

	public static String getFileNameWithoutExt(String filename) {
		String nameWithoutExt = "";
		if (StringUtils.isEmpty(filename)) {
			return "";
		}
		int index = filename.lastIndexOf(".");
		if (index > 0) {
			nameWithoutExt = filename.substring(0, index);
		}
		return nameWithoutExt;
	}
	
	// 复制文件
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();

		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}

	// 复制文件夹
	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(new File(targetDir)
						.getAbsolutePath()
						+ File.separator + file[i].getName());
				if(sourceFile.getName().indexOf(".vax") < 0)
					copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}

	public static String getFileExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i + 1).toLowerCase();
			}
		}
		return null;
	}

	public static String extractFileExt(String fileNM) {
		int i = fileNM.lastIndexOf('.');
		if (i >= 0)
			return fileNM.substring(i, fileNM.length());
		else
			return "";
	}

	public static String extractFileExtWithoutDot(String fileNM) {
		int i = fileNM.lastIndexOf('.');
		if (i >= 0)
			return fileNM.substring(i + 1, fileNM.length());
		else
			return "";
	}
	public static String getFileName(String fileNM) {
		int i = fileNM.lastIndexOf('.');
		if (i >= 0)
			return fileNM.substring(0, i);
		else
			return fileNM;
	}
	/**
	 * 获得文件的后缀名
	* @Title: getFileExt 
	* @Description: TODO 
	* @return String   
	* @throws
	 */
	public static String getFileExt(String fileNM) {
		int i = fileNM.lastIndexOf('.');
		if (i >= 0)
			return fileNM.substring(i+1);
		else
			return fileNM;
	}
	
	public static String getFileNameWithFullPath(String filePath){
		int i = filePath.lastIndexOf('/');
		int j = filePath.lastIndexOf("\\");
		int k;
		if (i >= j) {
			k = i;
		} else {
			k = j;
		}
		int n = filePath.lastIndexOf('.');
		if(n>0){
			return filePath.substring(k+1, n);
		}else{
			return filePath.substring(k+1);
		}
	}

	public static String extractFilePath(String fileNM) {
		int i = fileNM.lastIndexOf('/');
		int j = fileNM.lastIndexOf("\\");
		int k;
		if (i >= j) {
			k = i;
		} else {
			k = j;
		}
		return fileNM.substring(0, k + 1);
	}
	public static String getFileNameWithExt(String fileNM){
		if(! fileNM.contains("/") && !fileNM.contains("\\")){
			return fileNM;
		}else{
		int i = fileNM.lastIndexOf('/');
		int j = fileNM.lastIndexOf("\\");
		int k;
		if (i >= j) {
			k = i;
		} else {
			k = j;
		}
	   	    return fileNM.substring(k+1);
		}
	}
	
	public static String getSysName(String sysName){
		if(sysName != null && !"".equals(sysName)&& sysName.contains("(")) {
		int i = sysName.lastIndexOf('(');
		return  sysName.substring(0, i);
		}else{
			return sysName;
		}
		
	}

	public static String getFileNameWithoutExt(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(0, i);
			}
		}
		return null;
	}

	public static String changeFileExt(String fileNM, String ext) {
		int i = fileNM.lastIndexOf('.');
		if (i >= 0)
			return (fileNM.substring(0, i) + ext);
		else
			return fileNM;
	}
	
	public static boolean existsDirectory(String strDir) {
		File file = new File(strDir);
		return file.exists() && file.isDirectory();
	}
	public static boolean existsFile(String strDir){
		File file = new File(strDir);
		return file.exists();
	}

	public static boolean forceDirectory(String strDir) {
		File file = new File(strDir);
		file.mkdirs();
		return existsDirectory(strDir);
	}	
	public static String  changeDirection(String strDir){
		String s = "/";
		String a = "\\";
		if(strDir != null && !" ".equals(strDir)){
			if(strDir.contains(s)){
				strDir = strDir.replace(s, a);
			}
		}
		return strDir;
	}
	
	public static String changeFTPDir(String strDir){
		String s = "/";
		String a ="\\";
		if(strDir != null && !" ".equals(strDir)){
			if(strDir.contains(a)){
				strDir = strDir.replace(a, s);
			}
		}
		return strDir;
		
	}
	public static String  changeRection(String strDir){
		String s = "/";
		String a = File.separator;
		if(strDir != null && !" ".equals(strDir)){
			if(strDir.contains(a)){
				strDir = strDir.replace(s, a);
			}
		}
		return strDir;
	}
	public static String getTranscode(String strDir){
		int i = strDir.lastIndexOf('_');
		if (i >= 0)
			return strDir.substring(0, i);
		else
			return strDir;
	}
	
	
    public static String removeLastMark(String filePath){
    	if(filePath.endsWith("/") || filePath.endsWith("\\")){
			int i = filePath.lastIndexOf('/');
			int j = filePath.lastIndexOf("\\");
			int k;
			if (i >= j) {
				k = i;
			} else {
				k = j;
			}
		    return changeFTPDir(filePath.substring(0, k)) ;
		}else{
			return changeFTPDir(filePath);
		}

    }
    public static String getMoviePath(String path){
    	int i = path.lastIndexOf("/");
    	int j = path.lastIndexOf("\\");
    	int k ;
    	if (i >= j) {
			k = i;
		} else {
			k = j;
		}
    	if(k>0){
    		return path.substring(0, k+1) ;
    	}else{
    		return path;
    	}
    }
    
    /**
     * 去除文件路径的盘符（D://123.txt---//123.txt）
     * */
    public static String removePanFu(String path){
    	int loc = path.indexOf(":");
    	return path.substring(loc+1);
    }
    
    /**
     * ftp://user02:123456@172.26.103.22:21/test/1684_0213测试4.ts,截取视频名称
    * @Title: getFtpRemoteName 
    * @Description: TODO 
    * @return String   
    * @throws
     */
    public static String getFtpRemoteNameWithExt(String name){
    	int i = name.lastIndexOf('/');
    	int j = name.lastIndexOf("\\");
    	int k;
		if (i >= j) {
			k = i;
		} else {
			k = j;
		}
		int n = name.lastIndexOf('.');
		return name.substring(k+1, n);
    }
    /**
     * ftp://user02:123456@172.26.103.22:21/test/1684_0213测试4.ts,截取ftp路径下的视频路径test
    * @Title: getPath 
    * @Description: TODO 
    * @return String   
    * @throws
     */
    public static String getPath(String name){
    	int i = name.lastIndexOf('@');
    	if(i>=0){
    		String subname = name.substring(i+1);
    		int j = subname.indexOf('/');
    		int k = subname.lastIndexOf('/');
    			if(j>0 && k>j){
    				return subname.substring(j,k+1);
    			}else{
    				return name;
    			}
    	}else{
    		return name;
    	}
    	
    }
  public static String getFtpStr(String name){
	  int j = name.indexOf("//");
	  int k = name.indexOf("\\\\");
		int n;
		if (j >= k) {
			n = j;
		} else {
			n = k;
		}
	  
	  int i = name.indexOf("/", n+2);
	  if(i>=0){
		  name = name.substring(i);
	  }
	  return name;
  }
  
  public static String removeLast(String str){
	  if(str.endsWith("/") || str.endsWith("\\")){
		    int i = str.lastIndexOf('/');
		  	int j = str.lastIndexOf("\\");
		  	int k;
				if (i >= j) {
					k = i;
				} else {
					k = j;
				}
			return str.substring(0, k);
	  }else{
		  	return str;
	  }
  }
    /**
     * 去除字符串中 头和尾的空格，中间的空格保留
    * @Title: trim 
    * @Description: TODO 
    * @return String   
    * @throws
     */
    public static String trim(String s) {
		  int i = s.length();// 字符串最后一个字符的位置
		  int j = 0;// 字符串第一个字符
		  int k = 0;// 中间变量
		  char[] arrayOfChar = s.toCharArray();// 将字符串转换成字符数组
		  while ((j < i) && (arrayOfChar[(k + j)] <= ' '))
		   ++j;// 确定字符串前面的空格数
		  while ((j < i) && (arrayOfChar[(k + i - 1)] <= ' '))
		   --i;// 确定字符串后面的空格数
		  return (((j > 0) || (i < s.length())) ? s.substring(j, i) : s);// 返回去除空格后的字符串
		 }
    public static String getBrackets(String str){
    	  int a = str.indexOf("{");
		  int c = str.indexOf("}");
		  if(a>=0 && c>=0 & c>a){
			return( str.substring(a+1, c));
		  }else{
			return str;
		  }
    }
    /**
     * 将字符串中所有的，替换成|
     * @param str
     * @return
     */
    public static String commaToVerti(String str){
    	if(str != null && !"".equals(str) && str.contains(",")){
    		return str.replaceAll(",", "|");
    	}else{
    		 return str;
    	}
    }
    /**
     * 去掉字符串中、前、后的空格
     * @param args
     * @throws IOException
     */
    public static String extractBlank(String name){
    	if(name != null &&  !"".equals(name) ){
    		return name.replaceAll(" +","");
    	}else{
    		return name;
    	}
    	
    }
    /**
     * 将null换成""
     * @param str
     * @return
     */
    public static String ConvertStr(String str){
    	return str != null && !"null".equals(str) ? str.trim() : "" ;
    	}

    
    public static void main(String[] args) {
		String path = "Y:/lixue/test/test.mp3";
		System.out.println(getMoviePath(path));
				
	}
}
