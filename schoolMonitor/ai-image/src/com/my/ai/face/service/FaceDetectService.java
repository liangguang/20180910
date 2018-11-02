package com.my.ai.face.service;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;

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

public class FaceDetectService {
	
	public static String token_url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials";
	
	public static String detect_url = "https://aip.baidubce.com/rest/2.0/face/v3/detect";
	
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
		//System.out.println(responseEntity.getBody().toString());
		return responseEntity.getBody();
	}
	
	public static void main(String[] args) {
	/*	
	 	String tokenStr = getAccessToken(FaceDetect.API_KEY, FaceDetect.SECRET_KEY);
		JSONObject jsonObject = new JSONObject(tokenStr);
        String access_token = jsonObject.getString("access_token");
        System.out.println(access_token);
        Long expires_in = jsonObject.getLong("expires_in");
        System.out.println(expires_in);
        map.put("access_token", access_token);
        map.put("expires_in", expires_in);
        map.put("start_date", System.currentTimeMillis());
      */
		detectFace("E:\\123.jpg");
        
	}
	
	
	public static String detectFace(String filePath) {
		
		String access_token = map.get("access_token") == null?"":map.get("access_token").toString();
		if(StringUtils.isEmpty(access_token)) {
			String tokenStr = getAccessToken(FaceDetect.API_KEY, FaceDetect.SECRET_KEY);
			JSONObject jsonObject = new JSONObject(tokenStr);
	        access_token = jsonObject.getString("access_token");
	        map.put("access_token", access_token);
		}
		
		HashMap<String, Object> body = new HashMap<>();
		body.put("image", Base64Utils.picToBase64(filePath, false));
		body.put("image_type", "BASE64");
		//body.put("max_face_num", 2);
		body.put("face_field", "age,beauty,gender,face_type");

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();  
		requestFactory.setConnectTimeout(2000);  
		requestFactory.setReadTimeout(60000);  
		RestTemplate template = new RestTemplate(requestFactory);
//		RestTemplate template = new RestTemplate();
//		ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
//		template.setRequestFactory(clientFactory);
		
		String url = detect_url +"?access_token="+access_token;
		System.out.println(url);
		URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<?> requestEntity = new HttpEntity<>(body,requestHeaders);
		ResponseEntity<String> responseEntity = template.exchange(uri, HttpMethod.POST, requestEntity, String.class);
		//System.out.println(responseEntity.getBody().toString());
		return responseEntity.getBody();
	}
	
	
}
