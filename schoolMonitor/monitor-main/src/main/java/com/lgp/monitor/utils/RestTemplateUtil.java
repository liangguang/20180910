package com.lgp.monitor.utils;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestTemplateUtil {

	public static String RestTemplateGet(String url) {
		RestTemplate template = new RestTemplate();
		ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
		template.setRequestFactory(clientFactory);
		URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		requestHeaders.add(HttpHeaders.COOKIE, "sid=");
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
		ResponseEntity<String> responseEntity = template.exchange(uri, HttpMethod.GET, requestEntity, String.class);
		return responseEntity.getBody();
	}

	public static List<String> RestTemplateGetList(String url) throws Throwable {
		RestTemplate template = new RestTemplate();
		ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
		template.setRequestFactory(clientFactory);
		URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		requestHeaders.add(HttpHeaders.COOKIE, "sid=");
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
		ResponseEntity<String> responseEntity = template.exchange(uri, HttpMethod.GET, requestEntity, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);

		List<String> entity = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<String>>() {
		});
		return entity;
	}

	public static void RestTemplatePut(String url, Map<String, Object> datas) throws Exception {

		RestTemplate template = new RestTemplate();
		ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
		template.setRequestFactory(clientFactory);
		URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		requestHeaders.add(HttpHeaders.COOKIE, "sid=");
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HttpEntity<?> requestEntity = new HttpEntity(datas, requestHeaders);
		template.exchange(uri, HttpMethod.PUT, requestEntity, Object.class);
	}

	public static String RestTemplatePost(String url, Map<String, Object> datas) throws Exception {

		RestTemplate template = new RestTemplate();
		ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
		template.setRequestFactory(clientFactory);
		URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		requestHeaders.add(HttpHeaders.COOKIE, "sid=");
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
		String relevantDataStr = objectMapper.writeValueAsString(datas);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HttpEntity<String> requestEntity = new HttpEntity(relevantDataStr, requestHeaders);
		ResponseEntity<String> responseEntity = template.exchange(uri, HttpMethod.POST, requestEntity, String.class);
		return responseEntity.getBody();
	}

}
