package com.lgp.monitor.qq.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class URIParamUtil {

	public static String getUrl(String url, Map<String, Object> params,boolean flag) {
		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			try {
				url += key + "=" +( flag ? URLEncoder.encode(params.get(key).toString(), "utf-8") : params.get(key).toString() )+ "&";
			} catch (UnsupportedEncodingException e) {
				System.err.println(e.getMessage());
			}
		}
	
		return url;
	} 
	
}
