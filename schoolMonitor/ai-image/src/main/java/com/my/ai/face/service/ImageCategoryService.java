package com.my.ai.face.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.imageclassify.AipImageClassify;

public class ImageCategoryService {


    public static final String APP_ID = "14664668";
    public static final String API_KEY = "akCvlQTGptQDHiFHHO5Zbuu9";
    public static final String SECRET_KEY = "QQAD3mSmPavp95V40cE01Z0nybYuY77Q";
	
    public static void main(String[] args) {
    	
    	//String f = "E:\\迅雷下载\\原始图片\\youmeitu-6146";
    	String file = "E:\\迅雷下载\\原始图片\\poco-35000\\15239720707630147_175258336.jpg";
    	imageClassify(file);
	}
    
    public void folderClassify(String f) {
    	File folder = new File(f);
    	for( File img:folder.listFiles()) {
    		if(img.isDirectory()) {
    			continue;
    		}
    		try {
    			String result = imageClassify(img.getAbsolutePath());
        		Path path = Paths.get(folder.getAbsolutePath(), result);
        		if(!Files.exists(path)) {
        			Files.createDirectory(path);
        		}
        		System.out.println(img.getName() +"=检测结果为："+ result);
        		Files.move(Paths.get(img.getAbsolutePath()),Paths.get(path.toString(), img.getName()));
        		Thread.sleep(1000);
    		} catch (Throwable e) {
				System.err.println(e.getMessage());
			}
    	}	
    }
	
	public static String imageClassify(String path) {
		
		AipImageClassify  client = new AipImageClassify(APP_ID,API_KEY,SECRET_KEY);
		// 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        JSONObject res = client.advancedGeneral(path,new HashMap<>());
        System.out.println(res.toString(2));
        JSONArray conclusion = res.getJSONArray("result");
        //System.out.println(conclusion);
        //正常，性感，色情
        return conclusion.toString();
	}
	
}
