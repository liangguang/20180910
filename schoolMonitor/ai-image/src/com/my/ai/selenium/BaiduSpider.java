package com.my.ai.selenium;

import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class BaiduSpider {

	public String[] urls = new String[] {
		"https://mbd.baidu.com/newspage/data/landingshare?context=%7B%22nid%22%3A%22news_8862722619224238393%22%2C%22sourceFrom%22%3A%22bjh%22%7D",
		"https://rs.mbd.baidu.com/t7hmzrq?f=cp",
		"https://mbd.baidu.com/newspage/data/landingshare?context=%7B%22nid%22%3A%22news_8596056054041232690%22%2C%22sourceFrom%22%3A%22bjh%22%7D"
	};
	
	public void open() throws Throwable {
		
		WebDriver driver = ChromeWebDriver.createChromeDriver();
		for (String url : urls) {
			driver.get(url);
			//String html = driver.getPageSource();
			//System.out.println(html);
			System.out.println(driver.manage().toString());
			Actions actions = new Actions(driver);
			actions.moveByOffset(800, 1).perform();
			Thread.sleep( 5 * 1000L);
			actions.moveByOffset(800, 1).perform();
			Thread.sleep( 5 * 1000L);
			//driver.close();
		}
	}
	
	public void openPhantom() throws Throwable{
		WebDriver driver = PhantomUtils.getPhantomJs();
		for (int i=0; i < 1000; i++) {
			System.out.println("当前计数:"+i);
			for (String url : urls) {
				driver.get(url);
				System.out.println("当前URL:" + url);
				Thread.sleep( new Random().nextInt(10) * 1000L);
			}
			
		}	
		driver.quit();
	}
	
	public void test() {
		 System.setProperty("phantomjs.binary.path", "G:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
		 WebDriver driver = new PhantomJSDriver();
		 driver.get("https://rs.mbd.baidu.com/t7hmzrq?f=cp");
		 System.out.println(driver.getCurrentUrl());
	}
	
	public static void main(String[] args) throws Throwable{
		new BaiduSpider().openPhantom();
	}
}
