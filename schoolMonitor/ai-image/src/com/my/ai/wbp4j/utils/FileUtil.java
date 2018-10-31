package com.my.ai.wbp4j.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	
	public static void main(String[] args) {
		List<String> list = FileUtil.readTxt(new File(""));
		System.out.println(list.get(4));
	}
	
	
	public static List<String> readTxt(File file){
		List<String> list = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				list.add(line);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static void readFile(File file){
		FileReader fr = null;
		BufferedReader bufferedreader = null;
		try {
			fr = new FileReader(file);
			bufferedreader = new BufferedReader(fr);
			String instring;
			while ((instring = bufferedreader.readLine().trim()) != null) {
				if (0 != instring.length()) {
					System.out.println(instring);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}finally {
			try {
				bufferedreader.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	
	public static void fileoutputWrite(String filePath, String line) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(filePath));
			out.write(line.getBytes());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void fileBufferoutputWrite(String filePath, String line) {
		FileOutputStream out = null;
		BufferedOutputStream buff = null;
		try {
			out = new FileOutputStream(new File(filePath));
			buff = new BufferedOutputStream(out);
			buff.write(line.getBytes());
			buff.flush();
			buff.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buff.close();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void fileWriter(String filePath, String line, boolean append) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(filePath, append); // append = true 追加方式写入文件
			fw.write(line);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void appendFileContent(String fileName, String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content + "\r\n");
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void downloadPicture(String urlList,String imageName)throws Throwable {
			File file = new File(imageName);
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			URL url = null;
			url = new URL(urlList);
			DataInputStream dataInputStream = new DataInputStream(url.openStream());

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int length;

			while ((length = dataInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			fileOutputStream.write(output.toByteArray());
			dataInputStream.close();
			fileOutputStream.close();
	}

}