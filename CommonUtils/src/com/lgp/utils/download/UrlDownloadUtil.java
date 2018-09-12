package com.lgp.utils.download;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lgp.utils.file.ReadFromFileUtil;

public class UrlDownloadUtil {

	/**
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{
		URL url = new URL(urlStr);  
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
		conn.setConnectTimeout(10*1000);
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		InputStream inputStream = conn.getInputStream();  
		byte[] getData = readInputStream(inputStream);    

		File saveDir = new File(savePath);
		if(!saveDir.exists()){
			saveDir.mkdir();
		}
		File file = new File(saveDir+File.separator+fileName);    
		FileOutputStream fos = new FileOutputStream(file);     
		fos.write(getData); 
		if(fos!=null){
			fos.close();  
		}
		if(inputStream!=null){
			inputStream.close();
		}

		if(ReadFromFileUtil.readFileByLines(saveDir+File.separator+fileName)){
			
			System.out.println(fileName+":"+new File(saveDir+File.separator+fileName).delete());
		}
		
		
		System.out.println("info:"+url+" download success"); 

	}



	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static  byte[] readInputStream(InputStream inputStream) throws IOException {  
		byte[] buffer = new byte[1024];  
		int len = 0;  
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		while((len = inputStream.read(buffer)) != -1) {  
			bos.write(buffer, 0, len);  
		}  
		bos.close();  
		return bos.toByteArray();  
	}  

	public static void main(String[] args) {
		
	}
	
	public static void main_temp(String[] args) throws InterruptedException {
		
		final File file = new File("E:/rent");
		long starttime = System.currentTimeMillis();
		for (File df : file.listFiles()) {
			System.out.println(df.getAbsolutePath());
		}
		long endtime = System.currentTimeMillis();
		long dxc = endtime-starttime;
		
		starttime = System.currentTimeMillis();
		
		final CountDownLatch begin = new CountDownLatch(1);
		
		final CountDownLatch end = new CountDownLatch(10);
		
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		
		for (int i = 0; i < 0; i++) {
			final int index = i;
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					try {
						begin.await();
						
						for (int j = index*1000; j <1000*(index+1) ; j++) {
							System.out.println(file.listFiles()[j].getAbsolutePath());
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						end.countDown();
					}
				}
			};
			
			executorService.submit(runnable);
		}
		
		begin.countDown();
		end.await();
		endtime = System.currentTimeMillis();
		System.out.println(""+(endtime-starttime));
		System.out.println(""+dxc);
		
		
	}
	
	static class ThreadDownload extends Thread{
		public int start;
		public int end;
		
		public ThreadDownload(int start, int end) {
			this.start = start;
			this.end = end;
		}


		@Override
		public void run() {

			for (int i = start; i < end; i++) {
				try{
				}catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
		}
	}
	
}
