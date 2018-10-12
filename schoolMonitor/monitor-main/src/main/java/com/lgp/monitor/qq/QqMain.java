package com.lgp.monitor.qq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.lgp.monitor.qq.model.QqChatResponse;
import com.lgp.monitor.qq.service.ChatRobotService;
import com.lgp.monitor.qq.service.SignService;

public class QqMain {

	public static void main(String[] args) {
		
		String chatUrl = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";
		int app_id = 2108993917;
		int time_stamp = (int)(System.currentTimeMillis() / 1000);
		String app_key = "5ts7qezuufWeIrce";
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("你有啥问的:");
			String speak = sc.nextLine();		
			if(speak.equals("886")) {
				sc.close();
				break;
			}
			Map<String, Object> params = new TreeMap<>();
			params.put("app_id", app_id);
			params.put("time_stamp", time_stamp);
			params.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", ""));
			params.put("session", "10000");
			params.put("question", speak);
			params.put("sign", SignService.getReqSign(params, app_key));	
			params.put("app_key", app_key);

			QqChatResponse response = ChatRobotService.sendMeasage(chatUrl, params);
			//System.out.println(response.getMsg());
			System.out.println(response.getData().getAnswer());
		}
	}

	public static void testMap() {
		TreeMap<String, String> map = new TreeMap<>();
		map.put("Z", "aaa");
		map.put("1", "4");
		map.put("2", "5");
		map.put("3", "6");
		map.put("1", "7");

		Set<String> keySet = map.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			System.out.println(key + ":" + map.get(key));
		}
	}
	
    public static void testHashMap() {
    	 Map<String, String> map = new HashMap<String, String>();
         map.put("c", "ccccc");
         map.put("a", "aaaaa");
         map.put("b", "bbbbb");
         map.put("d", "ddddd");
         
         List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(map.entrySet());
         Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
             //升序排序
             public int compare(Entry<String, String> o1,
                     Entry<String, String> o2) {
                 return o1.getValue().compareTo(o2.getValue());
             }
             
         });
         
         for(Map.Entry<String,String> mapping:list){ 
                System.out.println(mapping.getKey()+":"+mapping.getValue()); 
           } 
      }
}

//key较器
class MapKeyComparator implements Comparator<String>{

        @Override
        public int compare(String str1, String str2) {
            
            return str1.compareTo(str2);
        }
}
//值比较
class MapValueComparator implements Comparator<Map.Entry<String, String>> {

    @Override
    public int compare(Entry<String, String> me1, Entry<String, String> me2) {

        return me1.getValue().compareTo(me2.getValue());
    }
}
