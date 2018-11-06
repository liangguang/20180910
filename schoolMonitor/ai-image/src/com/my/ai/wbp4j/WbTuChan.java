package com.my.ai.wbp4j;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.my.ai.wbp4j.WbpUpload;

@SpringBootApplication
public class WbTuChan {

	public static void main(String[] args) throws IOException {
		//SpringApplication.run(Application.class, args);
		//WbpLogin.reLogin();
		//new WbpUpload().upload("E:\\迅雷下载\\newpic\\123.jpg");
		
		//String folder = "E:\\迅雷下载\\云盘图片\\Win4000-gzh-3868_page_";
		WbpUpload up = new WbpUpload();
		long start = System.currentTimeMillis();
		/*for(int i=1;i < 10; i++) {
			String folder_l = folder + i;
			up.uploadFolder(folder_l);
		}*/
		String recordFile = "E:\\迅雷下载\\云盘图片\\poco-50-70.txt";
		up.uploadFolder("E:\\迅雷下载\\云盘图片\\50",recordFile,true);
		System.out.println("耗時"+ (System.currentTimeMillis() - start) / 1000 + "秒");
		
	}
	
	
	
}
