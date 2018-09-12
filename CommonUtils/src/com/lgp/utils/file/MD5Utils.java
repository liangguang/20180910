package com.lgp.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JProgressBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5Utils {
	private static Logger logger = LoggerFactory.getLogger(MD5Utils.class);

	/**
	 * 得到文件的MD5码,用于校验
	 * 
	 * @param file
	 * @param jpb
	 * @return
	 */
	public static String getMD5(File file, JProgressBar jpb) {
		FileInputStream fis = null;
		jpb.setMaximum((int) file.length());
		jpb.setValue(0);
		jpb.setString("正在计算:" + file.getName() + "的MD5值");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length = -1;
			System.out.println("开始算");
			int value = 0;
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
				value += length;
				jpb.setValue(value);
			}
			System.out.println("算完了");
			return bytesToString(md.digest());
		} catch (IOException ex) {
			logger.debug("生成md5出错："+ex.getMessage());
			logger.error("生成md5出错：",ex);
			return null;
		} catch (NoSuchAlgorithmException ex) {
			logger.debug("生成md5出错了。。。。。。。。。。。。。。。。"+ex.getMessage());
			logger.error("生成md5出错。。。。。。。。。。。。。。。。",ex);
			return null;
		} finally {
			try {
				fis.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static String getMD5(File file) {
		FileInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length = -1;
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			return bytesToString(md.digest());
		} catch (IOException ex) {
			return null;
		} catch (NoSuchAlgorithmException ex) {
			return null;
		} finally {
			try {
				fis.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * 得到文件的SHA码,用于校验
	 * 
	 * @param file
	 * @return
	 */
	public static String getSHA(File file) {
		FileInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			fis = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length = -1;
			System.out.println("开始算");
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			System.out.println("算完了");
			return bytesToString(md.digest());
		} catch (IOException ex) {
			return null;
		} catch (NoSuchAlgorithmException ex) {
			return null;
		} finally {
			try {
				fis.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * 
	 * @param file
	 * @param jpb
	 * @return
	 */
	public static String getSHA(File file, JProgressBar jpb) {
		FileInputStream fis = null;
		jpb.setMaximum((int) file.length());
		jpb.setValue(0);
		jpb.setString("正在计算:" + file.getName() + "的MD5值");
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			fis = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length = -1;
			System.out.println("开始算");
			int value = 0;
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
				value += length;
				jpb.setValue(value);
			}
			System.out.println("算完了");
			return bytesToString(md.digest());
		} catch (IOException ex) {
			return null;
		} catch (NoSuchAlgorithmException ex) {
			return null;
		} finally {
			try {
				fis.close();
			} catch (IOException ex) {
			}
		}
	}

	public static String bytesToString(byte[] data) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		char[] temp = new char[data.length * 2];
		for (int i = 0; i < data.length; i++) {
			byte b = data[i];
			temp[i * 2] = hexDigits[b >>> 4 & 0x0f];
			temp[i * 2 + 1] = hexDigits[b & 0x0f];
		}
		return new String(temp);

	}

	public static String encode(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// System.out.println("result: " + buf.toString());// 32位的加密
			// System.out.println("result: " + buf.toString().substring(8,
			// 24));// 16位的加密
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block e.printStackTrace();
			return "";
		}
	}

	public static void main(String[] args) {
		// String s = System.getProperties().getProperty("java.io.tmpdir");
		// System.out.println(s);

	}
}
