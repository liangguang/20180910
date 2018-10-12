package com.my.ai.service;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileService {

	public static Logger logger = LoggerFactory.getLogger(FileService.class);
	
	
	//最慢
	public static void writeFile1(String file,String content) throws IOException {
		FileOutputStream out = null;
		out = new FileOutputStream(new File(file));
		long begin = System.currentTimeMillis();
		out.write(content.getBytes());
		out.close();
		long end = System.currentTimeMillis();
		System.out.println("FileOutputStream执行耗时:" + (end - begin) + " 毫秒");
	}
	//中
	public static void writeFile2(String file,String content) throws IOException{
		FileWriter fw = null;
		fw = new FileWriter(file);
		long begin3 = System.currentTimeMillis();
		fw.write(content);
		fw.close();
		long end3 = System.currentTimeMillis();
		System.out.println("FileWriter执行耗时:" + (end3 - begin3) + " 毫秒");
	}
	//最快
	public static void writeFile3(String file,String content) throws IOException{
		FileOutputStream outSTr = null;
		BufferedOutputStream buff = null;
		outSTr = new FileOutputStream(new File(file));
		buff = new BufferedOutputStream(outSTr);
		long begin0 = System.currentTimeMillis();
		buff.write(content.getBytes());
		buff.flush();
		buff.close();
		long end0 = System.currentTimeMillis();
		System.out.println("BufferedOutputStream执行耗时:" + (end0 - begin0) + " 毫秒");
	}

	public static void main(String[] args) {
		for (int i = 0; i < 7; i++) {
			String result = TokenService.getResult("E:\\QLDownload\\氧化还原反应中电子转移的方向和数目的表示方法\\" + i +"-氧化还原反应中电子转移的方向和数目的表示方法.pcm");
			appendFile2("E:\\QLDownload\\氧化还原反应中电子转移的方向和数目的表示方法\\氧化还原反应中电子转移的方向和数目的表示方法.txt", result+"\r\n");
		}
	}

	public static void appendFile1(String file, String conent) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(conent);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 追加文件：使用FileWriter
	 * 
	 * @param fileName
	 * @param content
	 */
	public static void appendFile2(String fileName, String content) {
		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(fileName, true);
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *  追加文件：使用RandomAccessFile
	 * 
	 * @param fileName 文件名
	 * @param content  追加的内容
	 */
	public static void appendFile3(String fileName, String content) {
		RandomAccessFile randomFile = null;
		try {
			// 打开一个随机访问文件流，按读写方式
			randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
