package com.my.ai.selenium;

import java.util.Random;

import org.openqa.selenium.WebDriver;

import com.my.ai.wbp4j.utils.FileUtil;

public class TouTiaoSpider {

	public static String[] urls = new String[] {
			"https://www.toutiao.com/i6617410368115327495/",
			"https://www.toutiao.com/i6618874291662357000/",
			"https://www.toutiao.com/i6615874582152741390/"
		};
	
	public static void openPhantom() throws Throwable{
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
	
	public static void test() {
		 System.setProperty("phantomjs.binary.path", "G:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
		 //PhantomUtils.useProxy = false;
		 WebDriver driver = PhantomUtils.getPhantomJs();
		 driver.get("https://www.toutiao.com/i6617410368115327495/");
		 FileUtil.fileoutputWrite("G:\\studycode\\1.html", driver.getPageSource());
		 System.out.println(driver.getCurrentUrl());
	}
	
	public static void main(String[] args) throws Throwable{
		//openPhantom();
		test();
	}
}
