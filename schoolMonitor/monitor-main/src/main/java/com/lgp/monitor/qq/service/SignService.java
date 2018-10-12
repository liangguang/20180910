package com.lgp.monitor.qq.service;

import java.security.MessageDigest;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class SignService {

	public final static int Sign_Validity_period = 5 * 50 * 1000;

	public static Map<String, Object> map = null;

	public static String getReqSign(Map<String, Object> params, String appkey) {
		String urlparam = "";
		urlparam = URIParamUtil.getUrl(urlparam, params,true);
		urlparam += "app_key=" + appkey;
		//System.out.println(urlparam);
		String sign = MD5(urlparam).toUpperCase();
		//System.out.println(sign);
		return sign;
	}

	public static String getMd5(byte[] buffer) {
		try {
			String s = null;
			char hexDigist[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(buffer);
			byte[] datas = md.digest(); // 16个字节的长整数
			char[] str = new char[2 * 16];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte b = datas[i];
				str[k++] = hexDigist[b >>> 4 & 0xf];// 高4位
				str[k++] = hexDigist[b & 0xf];// 低4位
			}
			s = new String(str);
			return s;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 获取MD5加密后的字符串
	 * 
	 * @param str
	 *            明文
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public static String getMD5(String str) {
		try {
			/** 创建MD5加密对象 */
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			/** 进行加密 */
			md5.update(str.getBytes("GBK"));
			/** 获取加密后的字节数组 */
			byte[] md5Bytes = md5.digest();
			String res = "";
			for (int i = 0; i < md5Bytes.length; i++) {
				int temp = md5Bytes[i] & 0xFF;
				if (temp <= 0XF) { // 转化成十六进制不够两位，前面加零
					res += "0";
				}
				res += Integer.toHexString(temp);
			}
			return res;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static String MD5(String s) {
		try {
			byte[] btInput = s.getBytes("UTF-8");
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			StringBuffer buf = new StringBuffer();
			for (byte b : md) {
				buf.append(String.format("%02x", b & 0xff));
			}
			return buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(MD5("app_id=2108993917&nonce_str=7cc4a6452e2d484e8722c1a9a73504d5&question=1111&session=10000&time_stamp=1539325768&app_key=5ts7qezuufWeIrce"));
		System.out.println(getMD5("12312"));
	}
}
