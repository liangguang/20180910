package com.lgp.monitor.utils;

import org.springframework.util.StringUtils;

public class MyServiceConfig {
	
	public static void assertPassParameter(String param, String key) {
		if (StringUtils.isEmpty(param)) {
			throw new IllegalStateException("请配置" + key + "参数!");
		}
	}
	
	public static void main(String[] args) {
		
	}
}
