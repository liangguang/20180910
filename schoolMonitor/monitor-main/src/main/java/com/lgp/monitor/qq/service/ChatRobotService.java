package com.lgp.monitor.qq.service;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.lgp.monitor.qq.model.QqChatResponse;

public class ChatRobotService {

	
	public static QqChatResponse sendMeasage(String url,Map<String, Object> params) {
		
		RestTemplate template = new RestTemplate();
		ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
		template.setRequestFactory(clientFactory);
		url = url + URIParamUtil.getUrl("?", params,false);
		URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<?> requestEntity = new HttpEntity<>(requestHeaders);
		ResponseEntity<QqChatResponse> responseEntity = template.exchange(uri, HttpMethod.POST, requestEntity, QqChatResponse.class);
		return responseEntity.getBody();
		
	}
	
}
