package com.my.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.nlp.AipNlp;

public class BaiduAipNlp {

	public static final String APP_ID = "14677924";
    public static final String API_KEY = "ZR6etvQwOOeaEHfyKsKiSdu8";
    public static final String SECRET_KEY = "ji2N8Wr763nFxVlnsKVrfykPD56Xmsgy";

    public static void main(String[] args) {
        // 初始化一个AipNlp
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        HashMap<String, Object> options = new HashMap<String, Object>();
        
        // 调用接口
        String text = "当年阿杜凭借一首《他一定很爱你》火遍大陆，他独特的沙哑嗓唱这种苦情歌就是很合适啊。"
        		+ "本以来他会好好火几年，没想到娱乐圈更新换代的速度太快，阿杜的“火”到底成了过去式。"
        		+ "视频里的阿杜虎背熊腰，早已没有了当年的清爽模样，不过他也人到中年，发福长胖在所难免。";
        JSONObject res = client.lexer(text, options);
        //System.out.println(res.toString(2));
        JSONArray items = res.getJSONArray("items");
        Set<String> cis = new HashSet<String>();
        for (int i = 0; i < items.length(); i++) {
			JSONObject item = items.getJSONObject(i);
			String item_text = item.getString("item");
			if(item_text.length() > 1 & item_text.length() < 5) {
				//System.out.println(item_text);
				cis.add(item_text);
			}
		}
        //System.out.println(text.length());
        System.out.println(cis.size());
        
        for (String ci : cis) {
			System.out.println(ci);
		}
    }
}
