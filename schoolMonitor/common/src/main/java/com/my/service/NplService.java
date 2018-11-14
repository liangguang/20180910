package com.my.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.nlp.AipNlp;

public class NplService {

	public static final String APP_ID = "14677924";
	public static final String API_KEY = "ZR6etvQwOOeaEHfyKsKiSdu8";
	public static final String SECRET_KEY = "ji2N8Wr763nFxVlnsKVrfykPD56Xmsgy";

	// 获取客户端
	public static AipNlp getClient() {
		AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
		return client;
	}

	// 分词
	public static Set<String> lexer(AipNlp client, String text) {
		Set<String> cis = new HashSet<String>();
		HashMap<String, Object> options = new HashMap<String, Object>();
		JSONObject res = client.lexer(text, options);
		JSONArray items = res.getJSONArray("items");
		for (int i = 0; i < items.length(); i++) {
			JSONObject item = items.getJSONObject(i);
			String item_text = item.getString("item");
			if (item_text.length() > 1 & item_text.length() < 5) {
				cis.add(item_text);
			}
		}
		return cis;
	}

	// 相似度 1:词义 0:短文本   限制64 511字节
	public static double sim(AipNlp client, String text_f, String text_s, int type) {
		HashMap<String, Object> options = new HashMap<String, Object>();
		JSONObject res = null;
		if (type == 1) {
			res = client.wordSimEmbedding(text_f, text_s, options);
		} else {
			res = client.simnet(text_f, text_s, options);
		}
		if(res.has("error_code") &&res.getInt("error_code") > 0) {
			return -1;
		}
		//System.out.println(res.toString(2));
		double result =  res.getDouble("score");
		return result;
	}

	//文本纠错 待纠错文本，输入限制511字节
	public static void ecnet(AipNlp client, String text) {
		// 传入可选参数调用接口
	    HashMap<String, Object> options = new HashMap<String, Object>();
	    // 文本纠错
	    JSONObject res = client.ecnet(text, options);
	    System.out.println(res.toString(2));
	}
	
	public static void main(String[] args) {
		ecnet(getClient(), "百度是一家人工只能公司");
	}
	
}
