package com.my.ai.face.service;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.my.ai.face.FaceDetect;

public class GetPersonAttrService {

public static String token_url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials";
	
	public static String detect_url = "https://aip.baidubce.com/rest/2.0/face/v3/detect?access_token=24.c0b4c3ca2962d8e00623f1446a3f061c.2592000.1543815070.282335-14649577";
	
	public static HashMap<String, Object> map = new HashMap<>();
	
	public static String getAccessToken(String appId,String secretId) {

		RestTemplate template = new RestTemplate();
		ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
		template.setRequestFactory(clientFactory);
		String url = token_url + "&client_id="+ appId + "&client_secret=" + secretId;
		System.out.println(url);
		URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<?> requestEntity = new HttpEntity<>(requestHeaders);
		ResponseEntity<String> responseEntity = template.exchange(uri, HttpMethod.GET, requestEntity, String.class);
		JSONObject jsonObject = new JSONObject(responseEntity.getBody());
        String access_token = jsonObject.getString("access_token");
        System.out.println(access_token);
        //过期时间官网说是一个月，所以获取一次可以用很长时间,一个月内可以指定token
        Long expires_in = jsonObject.getLong("expires_in");
        System.out.println(expires_in);
        map.put("access_token", access_token);
        map.put("expires_in", expires_in); 
        map.put("start_date", System.currentTimeMillis());
		return access_token;
	}
	
	
	public static Map<String,Object> faceInfo(String filePath) {
		
		String access_token = map.get("access_token") == null?"":map.get("access_token").toString();
		if(StringUtils.isEmpty(access_token)) {
			String tokenStr = getAccessToken(FaceDetect.API_KEY, FaceDetect.SECRET_KEY);
			JSONObject jsonObject = new JSONObject(tokenStr);
	        access_token = jsonObject.getString("access_token");
	        map.put("access_token", access_token);
		}
		try {
			HashMap<String, Object> body = new HashMap<>();
			body.put("image", Base64Utils.picToBase64(filePath, false));
			body.put("image_type", "BASE64");
			body.put("face_field", "age,beauty,gender,face_type");

			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();  
			requestFactory.setConnectTimeout(2000);  
			requestFactory.setReadTimeout(60000);  
			RestTemplate template = new RestTemplate(requestFactory);
			String url = detect_url ;
			//+"?access_token="+access_token;
			System.out.println(url);
			URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<?> requestEntity = new HttpEntity<>(body,requestHeaders);
			ResponseEntity<String> responseEntity = template.exchange(uri, HttpMethod.POST, requestEntity, String.class);
			//System.out.println(responseEntity.getBody().toString());
			JSONObject jsonObject = new JSONObject(responseEntity.getBody());
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
			Map<String, Object> map = new HashMap<>();
			//性别，female:女 male:男
        	map.put("gender", jsonObject.getJSONObject("gender").getString("type"));
        	map.put("beauty", beauty); //颜值
        	map.put("age", jsonObject.getInt("age")); //年龄
			return map;
		} catch (Throwable e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	
	
}
