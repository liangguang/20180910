package com.my.ai.selenium;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class PhantomUtils {

	public static boolean useProxy = true;

	public static WebDriver getPhantomJs() {
			
			String osname = System.getProperties().getProperty("os.name");
	    	if (osname.equals("Linux")) {//判断系统的环境win or Linux
	    		System.setProperty("phantomjs.binary.path", "/usr/bin/phantomjs");
	    	} else {
	    		System.setProperty("phantomjs.binary.path", "G:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");//设置PhantomJs访问路径
	    	}
	    	DesiredCapabilities desiredCapabilities = DesiredCapabilities.phantomjs();
	    	String[] phantomArgs = new String[] { "--webdriver-loglevel=NONE" }; 
	    	desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomArgs);
	    	desiredCapabilities.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
	    	desiredCapabilities.setCapability("phantomjs.page.customHeaders.User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:50.0) Gecko/20100101 　　Firefox/50.0");
	    	if (useProxy) {//是否使用代理
	    		 Proxy proxy = new Proxy();
	    		 proxy.setProxyType(ProxyType.MANUAL);
	    		 proxy.setAutodetect(false);
	    		 String proxyStr = "114.225.170.178:53128";
	    		 proxy.setHttpProxy(proxyStr);
	    		 desiredCapabilities.setCapability(CapabilityType.PROXY, proxy);
	    	}
	    	return new PhantomJSDriver(desiredCapabilities);
	    }
}
