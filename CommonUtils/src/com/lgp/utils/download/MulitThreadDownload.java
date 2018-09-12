package com.lgp.utils.download;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * 多线程文件下载,提高大文件的下载速度 
 * <p>
 * */
public class MulitThreadDownload {


	private final int threadSize = 5; 
	
	private int downloadTimeout = 5000;
	
	static boolean flag = true;

	private DowloadThread[] dowloadRunnables = new DowloadThread[threadSize];
	
	final static CountDownLatch countDownLatch = new CountDownLatch(1);
	
	public static void main(String[] args) throws Throwable {
		System.out.println("开始下载时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		new MulitThreadDownload().downloadFile(args[0], args[1]);
		countDownLatch.await();
	}
	
	public boolean downloadFile(String url,String filePath){
		
		System.out.println("下载地址"+url+",目标文件路径："+filePath);
		try {
			
			URL urlPath = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)urlPath.openConnection();
			conn.setConnectTimeout(downloadTimeout);
			conn.setRequestMethod("GET");
			
			int status = conn.getResponseCode(); 
			if(status == 200){ //200返回所有，206返回部分 
				//文件长度	
				int length = conn.getContentLength(); 
				System.out.println("获取文件大小:"+length);
				
				//创建下载文件 指定大小 
				RandomAccessFile raf = new RandomAccessFile(new File(filePath), "rwd");
				raf.setLength(length);
				raf.close(); //释放资源
				
				//分块大小
				int blockSize = length / threadSize;
					
				//创建工作线程
				for (int i = 1; i <= threadSize; i++) {
					int startIndex = blockSize*(i-1);
					int endIndex = startIndex + blockSize - 1;
					if(i == threadSize){
						endIndex = length;
					}
					dowloadRunnables[i-1] = new DowloadThread(url, filePath, i, startIndex, endIndex);
					dowloadRunnables[i-1].start();
				}
				conn.disconnect();
				
				moniterLength(length);
				
				return flag;
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
			File file = new File(filePath);
			if(file.exists()){
				//file.delete(); //下载失败 删除临时文件 
			}
			return false;
		}
		return false;
	}
	//输出下载进度
	public void moniterLength(final int length) {

		new Thread(new Runnable() {
			
			@Override
			public void run() {
					while(getDownloadLength() < length){
						
						System.out.println("文件大小"+length+"，目前下载大小："+getDownloadLength()+"进度"+ getDownloadLength()* 1.0 / (long)length);
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(getDownloadLength() >=length){
						System.out.println("下载完成时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						countDownLatch.countDown();
					}
				
			}
		}).start();
	}

	//监控下载进度
	public int getDownloadLength(){
		int length = 0;
		
		for (int i = 0; i < dowloadRunnables.length; i++) {
			length += dowloadRunnables[i].downloadLength;
		}
		return length;
	}
	
}

