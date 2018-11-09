package com.my.ai.face.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;

import com.baidu.aip.contentcensor.AipContentCensor;

public class ImageCensorService {

    public static final String APP_ID = "14664668";
    public static final String API_KEY = "akCvlQTGptQDHiFHHO5Zbuu9";
    public static final String SECRET_KEY = "QQAD3mSmPavp95V40cE01Z0nybYuY77Q";
	
    public static void main(String[] args) {
		//float f = (float) 7.0E-6;
		//System.out.println(f > 0.000007);
    	int one = 0;
    	int two = 0;
    	int three = 0;
    	File folder = new File("E:\\迅雷下载\\原始图片\\youmeitu-6146");
    	for( File img:folder.listFiles()) {
    		if(img.isDirectory()) {
    			continue;
    		}
    		try {
    			String result = pornCensor(img.getAbsolutePath());
        		Path path = Paths.get(folder.getAbsolutePath(), result);
        		if(!Files.exists(path)) {
        			Files.createDirectory(path);
        		}
        		System.out.println(img.getName() +"=检测结果为："+ result);
        		Files.move(Paths.get(img.getAbsolutePath()),Paths.get(path.toString(), img.getName()));
        		if("正常".equals(result)) {
        			one++;
        		}else if("性感".equals(result)) {
        			two++;
        		}else if("色情".equals(result)) {
        			three++;
        		} 
        		Thread.sleep(1000);
    		} catch (Throwable e) {
				System.err.println(e.getMessage());
			}
    	}	
    	
    	System.out.println("正常："+one+"|性感："+two+"|色情："+three+"|总数:"+(one+three+two));
    	//String path = "E:\\112.jpg";
    	
	}
	
	public static String pornCensor(String path) {
		
		AipContentCensor client = new AipContentCensor(APP_ID,API_KEY,SECRET_KEY);
		// 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        JSONObject res = client.antiPorn(path);
        System.out.println(res.toString(2));
        String conclusion = res.getString("conclusion");
        //System.out.println(conclusion);
        //正常，性感，色情
        return conclusion;
	}
	
	/**
	 * {
  "result": [
    {
      "probability": 0.999998,
      "class_name": "性感"
    },
    {
      "probability": 1.0E-6,
      "class_name": "色情"
    },
    {
      "probability": 1.0E-6,
      "class_name": "正常"
    }
  ],
  "conclusion": "性感",
  "log_id": 7733706444544530285,
  "result_fine": [
    {
      "probability": 1.0E-6,
      "class_name": "一般色情"
    },
    {
      "probability": 0,
      "class_name": "卡通色情"
    },
    {
      "probability": 0,
      "class_name": "SM"
    },
    {
      "probability": 0,
      "class_name": "艺术品色情"
    },
    {
      "probability": 0,
      "class_name": "儿童裸露"
    },
    {
      "probability": 0,
      "class_name": "低俗"
    },
    {
      "probability": 0,
      "class_name": "性玩具"
    },
    {
      "probability": 0.999998,
      "class_name": "女性性感"
    },
    {
      "probability": 0,
      "class_name": "卡通女性性感"
    },
    {
      "probability": 0,
      "class_name": "男性性感"
    },
    {
      "probability": 0,
      "class_name": "自然男性裸露"
    },
    {
      "probability": 0,
      "class_name": "亲密行为"
    },
    {
      "probability": 0,
      "class_name": "卡通亲密行为"
    },
    {
      "probability": 0,
      "class_name": "特殊类"
    },
    {
      "probability": 0,
      "class_name": "一般正常"
    },
    {
      "probability": 0,
      "class_name": "卡通正常"
    }
  ],
  "result_num": 16,
  "confidence_coefficient": "确定"
}
	 * */
	
}
