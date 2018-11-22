package com.my.service.qq;

import java.util.TreeMap;

import org.json.JSONObject;

import com.qcloud.QcloudApiModuleCenter;
import com.qcloud.Module.Wenzhi;

public class QQNlp {
	
	public static final String APP_ID = "1255888356";

	public static final String SECRET_ID = "AKIDiUI7mTJio2HTH7qZn4Mhv8OaBmCYxUr2";
	
	public static final String SECRET_KEY = "ji2N8Wr763nFxVlnsKVrfykPD56Xmsgy";

	
	public static void main(String[] args) {
		
		test();
		
	}
	
	
	public static void test() {
		
		/* 如果是循环调用下面举例的接口，需要从此处开始你的循环语句。切记！ */
		TreeMap<String, Object> config = new TreeMap<String, Object>();
		config.put("SecretId", SECRET_ID);
		config.put("SecretKey", SECRET_KEY);
		/* 请求方法类型 POST、GET */
		config.put("RequestMethod", "GET");
		/* 区域参数，可选: gz:广州; sh:上海; hk:香港; ca:北美;等。 */
		config.put("DefaultRegion", "gz");
		
		QcloudApiModuleCenter module = new QcloudApiModuleCenter(new Wenzhi(),config);
		TreeMap<String, Object> params = new TreeMap<String, Object>();
		// 将需要输入的参数都放入 params 里面，必选参数是必填的。
		// DescribeInstances 接口的部分可选参数如下
		params.put("text", "近日");
		// 在这里指定所要用的签名算法，不指定默认为HmacSHA1
		// params.put("SignatureMethod", "HmacSHA256");
		// generateUrl 方法生成请求串，但不发送请求。在正式请求中，可以删除下面这行代码。
	    // 如果是POST方法，或者系统不支持UTF8编码，则仅会打印host+path信息。
		// System.out.println(module.generateUrl("DescribeInstances", params));

		String result = null;
		try {
			// call 方法正式向指定的接口名发送请求，并把请求参数params传入，返回即是接口的请求结果。
			result = module.call("LexicalSynonym", params);
	        // 可以对返回的字符串进行json解析，您可以使用其他的json包进行解析，此处仅为示例
			JSONObject json_result = new JSONObject(result);
			System.out.println(json_result);
		} catch (Exception e) {
			System.out.println("error..." + e.getMessage());
		}
		
		
	}
	
}
