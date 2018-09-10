package com.lgp.monitor.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(FileUtils.class);

	public static enum FileOperations {
		copy, move, none
	}

	public static long move(String srcFile, String destPath) throws IOException {
		File src = new File(srcFile);
		File dst = new File(destPath);
		src.renameTo(dst);
		return dst.length();
	}

	public static void deleteFileByStrategy(String path, long day){
		long strategy = day * 24 * 60 * 60 * 1000;
		File file = new File(path);

		if (file.exists()
				&& (System.currentTimeMillis() - file.lastModified() > strategy)) {
			if (file.isDirectory() && (file.listFiles().length == 0)) {
				logger.info("delete expire path：{}", path);
				file.delete();
			} else if (file.isFile()) {
				logger.info("delete expire file：{}", path);
				file.delete();
			}
		}
	}

	public static boolean isFileOpened(String source)
			throws  InterruptedException {
		File file = new File(source);
		return !file.renameTo(file);
	}

	public static Document loadXMLFile(String strFileName)
			throws DocumentException, IOException {
		return DocumentHelper
				.parseText(filterInvalidCharacter(readFileContent(strFileName)));
	}

	private static String filterInvalidCharacter(String xmlStr) {
		StringBuilder sb = new StringBuilder();
		char[] chs = xmlStr.toCharArray();
		for (char ch : chs) {
			if ((ch >= 0x00 && ch <= 0x08) || (ch >= 0x0b && ch <= 0x0c)
					|| (ch >= 0x0e && ch <= 0x1f)) {
			} else {
				sb.append(ch);
			}
		}
		return sb.toString().replaceAll(
				"xsi:schemaLocation=\"http://www.cptn.tv cptn.xsd\"", "");
	}

	private static String readFileContent(String fileName) throws IOException {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName), "UTF-8"));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString);
			}
			reader.close();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return sb.toString();
	}

	public static String fileMd5(String path) {
		FileInputStream fis = null;
		String md5 = null;
		try {
			fis = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			logger.error(path + "计算MD5出错", e);
		}
		try {
			md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
		} catch (IOException e) {
			logger.error(path + "计算MD5出错", e);
		}
		IOUtils.closeQuietly(fis);
		return md5;
	}

	
	public static String getFileName(String fileFullPath) {
		fileFullPath = fileFullPath.replace("/", "\\");
		return fileFullPath.substring(fileFullPath.lastIndexOf("\\") + 1,
				fileFullPath.length());
	}
	
	public static String getFileNameWithoutSuffix(String fileFullPath) {
		fileFullPath = fileFullPath.replace("/", "\\");
		return fileFullPath.substring(fileFullPath.lastIndexOf("\\") + 1,
				fileFullPath.lastIndexOf("."));
	}
	
	public static String getRelativePathFromURL(URL url) {
		String ss = url.getPath();
		if (ss.startsWith("/")) {
			ss = ss.substring(1);
		}
		int i = ss.indexOf('/');
		if (i > 0) {
			ss = ss.substring(i + 1);
		}
		i = ss.indexOf('/');
		if (i > 0) {
			ss = ss.substring(i + 1);
		}
		i = ss.lastIndexOf('/');
		if (i >= 0) {
			ss = ss.substring(0, i);
		} else {
			ss = "";
		}
		if ("".equals(ss)) {
			return ss;
		} else {
			return ss + "/";
		}
	}

	public static String getRelativePath(String path) {
		String result = "";
		if (path.indexOf(":") == 1) {
			result = path.substring(2, path.lastIndexOf("\\")).replaceAll("\\",
					"/");
		} else {
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
			int i = path.indexOf('/');
			if (i > 0) {
				path = path.substring(i + 1);
			}
			i = path.lastIndexOf('/');
			if (i >= 0) {
				path = path.substring(0, i);
			} else {
				path = "";
			}
			result = path;
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		System.out.println(end - begin);
	}
}
