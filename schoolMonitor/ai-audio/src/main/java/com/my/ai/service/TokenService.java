package com.my.ai.service;

import java.util.HashMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.baidu.aip.speech.AipSpeech;

@Service
public class TokenService {

	public static Logger logger = LoggerFactory.getLogger(TokenService.class);
	
	 //设置APPID/AK/SK
    public static final String APP_ID = "9449387";
    public static final String API_KEY = "YGouKqxd5CDXs3byEHquVZ3Y";
    public static final String SECRET_KEY = "25a189df5666fe98e0de51a0f3f9ece8";
    static AipSpeech client = null;
    static {
    	if(client == null) {
    		 client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
    	}
    }
    
    public static void main(String[] args) {
        getResult("E:\\QLDownload\\氧化还原反应中电子转移的方向和数目的表示方法\\0-氧化还原反应中电子转移的方向和数目的表示方法.pcm");
    }
    
    public static String getResult(String file) {
    	 
          // 可选：设置网络连接参数
          client.setConnectionTimeoutInMillis(2000);
          client.setSocketTimeoutInMillis(60000);
          // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
          //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
          //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
          JSONObject res = client.asr(file, "pcm", 16000, null);
          //System.out.println(res.toString(2));
          System.out.println(res.get("result").toString());
          return res.get("result").toString();
    }
	
    
    public static void sample(AipImageClassify client) {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("baike_num", "5");
        
        // 参数为本地路径
        String image = "test.jpg";
        JSONObject res = client.plantDetect(image, options);
        System.out.println(res.toString(2));
    }
	
	
}
