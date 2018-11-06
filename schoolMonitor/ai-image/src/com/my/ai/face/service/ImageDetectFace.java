package com.my.ai.face.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.face.AipFace;

public class ImageDetectFace {

	 //设置APPID/AK/SK
    public static final String APP_ID = "14649577";
    public static final String API_KEY = "jhHmFFGOltzOEQWfYZM2aK5c";
    public static final String SECRET_KEY = "esXQzKa2eGEpuNwlh82hdRcArsIMQptb";

    public static void main(String[] args) {
    	String folder = "E:\\迅雷下载\\云盘图片\\poco-gzh-33528";
    	faceBeautyCategory(folder);
    }
    
    //文件夹文件分类
    public static void faceBeautyCategory(String folderPath) {
    	File folder = new File(folderPath);
    	for (File file: folder.listFiles()) {
    		if(file.isDirectory()) {
    			//faceBeautyCategory(file.getAbsolutePath());
    		}else {
    			try {
	            	JSONObject jsonObject = face(file.getAbsolutePath());
	            	if(jsonObject.get("result") == null) {
	            		//无人脸
	            		continue;
	            	}
	            	jsonObject = jsonObject.getJSONObject("result");
	            	JSONArray array = jsonObject.getJSONArray("face_list");
	            	int len = array.length();
	            	if(len > 0){
	            		jsonObject = array.getJSONObject(0);
	            	}
	            	double beauty = jsonObject.getDouble("beauty");
	            	//female 
	            	String gender = jsonObject.getJSONObject("gender").getString("type");
	            	int age = jsonObject.getInt("age");
	            	System.out.println("性别：" + gender);
	            	System.out.println("颜值" + beauty);
	            	System.out.println("年龄"+ age);
	            	String filename = beauty + file.getName().substring(file.getName().lastIndexOf("."));
	            	if("female".equals(gender) && age > 15) {
	            		if(beauty > 90) {
		                	Files.move(Paths.get(file.getAbsolutePath()), Paths.get(folder.getParent(), "90",filename));
		                }else if (beauty > 70) {
		                	Files.move(Paths.get(file.getAbsolutePath()), Paths.get(folder.getParent(), "70",filename));
		                }else{
		                	Files.move(Paths.get(file.getAbsolutePath()), Paths.get(folder.getParent(), "50",filename));
		                }
	            	}else {
	            		Files.move(Paths.get(file.getAbsolutePath()), Paths.get(folder.getParent(), "00",filename));
	            	}
	            	
	            	
				} catch (Throwable e) {
					System.out.println(e.getMessage());
				}
    			try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
    		}
		} 
    	
    }

    //人脸识别
    public static JSONObject face(String path) {
    	  // 初始化一个AipFace
    	AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("face_field", "age,beauty,gender,face_type");
        // 调用接口
        JSONObject res = client.detect( Base64Utils.picToBase64(path, false),"BASE64",options);
        //System.out.println(res.toString(2));
        return res;
    }
    
}
