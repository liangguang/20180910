package com.my.ai.selenium;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ChromeWebDriver {
	private final static String CHROME_DRIVER_PATH = "C:\\Users\\CDV\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe";

	public static boolean useProxy = true;
	
	
	public static WebDriver createWebDriver() {
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
		return new ChromeDriver();
	}

	public static WebDriver createChromeDriver() {
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("user-agent=iPhone X");
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		if(useProxy) {
			Proxy proxy = new Proxy();
			proxy.setHttpProxy("114.80.216.171:54408");
			capabilities.setCapability(CapabilityType.PROXY, proxy);
		}
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		ChromeDriver driver = new ChromeDriver(capabilities);
		return driver;
	}
}