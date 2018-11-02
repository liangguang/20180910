package com.my.ai.face;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.baidu.aip.imageclassify.AipImageClassify;
import com.my.ai.face.service.FaceDetectService;

public class FaceDetect {

	 //设置APPID/AK/SK
    public static final String APP_ID = "14649577";
    public static final String API_KEY = "jhHmFFGOltzOEQWfYZM2aK5c";
    public static final String SECRET_KEY = "esXQzKa2eGEpuNwlh82hdRcArsIMQptb";

    
    
    public static void main(String[] args) {
    	String folder = "E:\\迅雷下载\\原始图片\\gank-658";
    	faceBeautyCategory(folder);
    }
    
    //文件夹文件分类
    public static void faceBeautyCategory(String folderPath) {
    	File folder = new File(folderPath);
    	for (File file: folder.listFiles()) {
    		if(file.isDirectory()) {
    			faceBeautyCategory(file.getAbsolutePath());
    		}else {
    			String result = FaceDetectService.detectFace(file.getAbsolutePath());
            	System.out.println(result);
            	JSONObject jsonObject = new JSONObject(result);
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
            	try {
            		if(beauty > 90) {
                		Files.move(Paths.get(file.getAbsolutePath()), Paths.get(folder.getParent(), "90"));
                	}else if (beauty > 70) {
                		Files.move(Paths.get(file.getAbsolutePath()), Paths.get(folder.getParent(), "70"));
                	}else{
                		Files.move(Paths.get(file.getAbsolutePath()), Paths.get(folder.getParent(), "50"));
                	}
				} catch (Throwable e) {
					System.out.println(e.getMessage());
				}
            	
            	
    		}
		} 
    	
    }
    
    public static void test() {
    	String result = FaceDetectService.detectFace("E:\\123.jpg");
    	System.out.println(result);
    	JSONObject jsonObject = new JSONObject(result);
    	jsonObject = jsonObject.getJSONObject("result");
    	JSONArray array = jsonObject.getJSONArray("face_list");
    	int len = array.length();
    	if(len > 0){
    		jsonObject = array.getJSONObject(0);
    	}
    	//female 
    	System.out.println("性别：" + jsonObject.getJSONObject("gender").getString("type"));
    	System.out.println("颜值" + jsonObject.getDouble("beauty"));
    	System.out.println("年龄"+ jsonObject.getInt("age"));
    }
    
    
    //人脸识别
    public static void face() {
    	  // 初始化一个AipFace
    	AipBodyAnalysis  client = new AipBodyAnalysis(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("type", "gender,age");
        // 调用接口
        String path = "E:\\test.jpg";
        JSONObject res = client.bodyAttr(path, options);
        System.out.println(res.toString(2));
        
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