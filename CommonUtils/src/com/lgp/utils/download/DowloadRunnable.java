package com.lgp.utils.download;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

//下载线程
class DowloadThread extends Thread{

	private int threadIndex;
	
	private int startIndex;
	
	private int endIndex;
	
	private String url;
	
	private String filePath;
	
	public int downloadLength; //已下载大小
	
	public DowloadThread(String url, String filePath,
			int threadIndex, int startIndex, int endIndex) {
		this.url = url;
		this.filePath = filePath;
		this.threadIndex = threadIndex;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
				
	}
	
	@Override
	public void run() {

			try {
				System.out.println("线程"+threadIndex+"任务开始");
				URL urlPath = new URL(url);
				HttpURLConnection conn = (HttpURLConnection)urlPath.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestProperty("Range", "bytes=" + startIndex + "-"  
                        + endIndex);
				conn.setRequestMethod("GET");
				
				int status = conn.getResponseCode();
				if(status==206 || status == 200){
					InputStream in = conn.getInputStream();
					RandomAccessFile raf = new RandomAccessFile(filePath, "rwd");
					raf.seek(startIndex);
					
					byte[] buffer = new byte[2048];
					int length = 0;
					while((length = in.read(buffer)) != -1){
						raf.write(buffer, 0, length);
						downloadLength += length;
						
					}
					raf.close();
					in.close();
				}else{
					throw new Exception("文件下载失败,状态码："+status);
				}
				System.out.println("线程"+threadIndex+"任务完成");
			} catch (Throwable e) {
				e.printStackTrace();
			}
	}
}