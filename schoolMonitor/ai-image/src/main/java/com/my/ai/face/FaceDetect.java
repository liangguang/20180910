package com.my.ai.face;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.imageclassify.AipImageClassify;
import com.my.ai.face.service.Base64Utils;

public class FaceDetect {

	 //设置APPID/AK/SK
    public static final String APP_ID = "14649577";
    public static final String API_KEY = "jhHmFFGOltzOEQWfYZM2aK5c";
    public static final String SECRET_KEY = "esXQzKa2eGEpuNwlh82hdRcArsIMQptb";

    
    
    public static void main(String[] args) {
    	String folder = "E:\\迅雷下载\\原始图片\\gank-658";
    	faceBeautyCategory(folder);
    	//String path = "E:\\123.jpg";
    	//face(path);
    }
    
    //文件夹文件分类
    public static void faceBeautyCategory(String folderPath) {
    	File folder = new File(folderPath);
    	for (File file: folder.listFiles()) {
    		if(file.isDirectory()) {
    			faceBeautyCategory(file.getAbsolutePath());
    		}else {
    			try {
	            	JSONObject jsonObject = face(file.getAbsolutePath());
	            	jsonObject = jsonObject.getJSONObject("result");
	            	JSONArray array = jsonObject.getJSONArray("face_list");
	            	int len = array.length();
	            	if(len > 0){
	            		jsonObject = array.getJSONObject(0);
	            	}
	            	double beauty = jsonObject.getDouble("beauty");
	            	//female 
	            	System.out.println("性别：" + jsonObject.getJSONObject("gender").getString("type"));
	            	System.out.println("颜值" + beauty);
	            	System.out.println("年龄"+ jsonObject.getInt("age"));
	            	String filename = beauty + file.getName().substring(file.getName().lastIndexOf("."));
	            	if(beauty > 90) {
	                	Files.move(Paths.get(file.getAbsolutePath()), Paths.get(folder.getParent(), "90",filename));
	                }else if (beauty > 70) {
	                	Files.move(Paths.get(file.getAbsolutePath()), Paths.get(folder.getParent(), "70",filename));
	                }else{
	                	Files.move(Paths.get(file.getAbsolutePath()), Paths.get(folder.getParent(), "50",filename));
	                }
				} catch (Throwable e) {
					System.out.println(e.getMessage());
				}finally {
					try {
						Thread.sleep(3500);
					} catch (InterruptedException e) {
						System.err.println(e.getMessage());
					}
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
        System.out.println(res.toString(2));
        return res;
    }
    //物体识别
    public static void image() {
  	  // 初始化一个AipFace
      AipImageClassify client = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);
      // 可选：设置网络连接参数
      client.setConnectionTimeoutInMillis(2000);
      client.setSocketTimeoutInMillis(60000);
      // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
      //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
      //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
      // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
      // 也可以直接通过jvm启动参数设置此环境变量
      //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

      // 调用接口
      String path = "E:\\123.jpg";
      JSONObject res = client.advancedGeneral(path, new HashMap<String, String>());
      System.out.println(res.toString(2));  
    }
    
}