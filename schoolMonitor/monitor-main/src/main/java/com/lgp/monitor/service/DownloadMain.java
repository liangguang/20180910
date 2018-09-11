package com.lgp.monitor.service;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
 
public class DownloadMain {
 
	private static final BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
	private static String storeDir = null;
	public static void main(String args[]) throws Exception {
		storeDir = getInput("请先设置你的文件存储目录:");
		int taskNum = getIntInput("请输入你的开启下载的线程数:");
		while(true) {
			String url = getInput("请输入文件链接地址:");
			Job job = new Job(url,storeDir,taskNum);
			job.startJob();
		}
	}
	
 
	private static int getIntInput(String message) throws IOException {
		String number = getInput(message);
		while(!number.matches("\\d+")) {
			System.out.println("线程数必须是1个整数");
			number = getInput(message);
		}
		return Integer.parseInt(number);
	}
 
	private static String getInput(String message) throws IOException {
		System.out.print(message);
		String line = in.readLine();
		while(line == null || line.trim().length()<1) {
			System.out.print(message);
			line = in.readLine();
		}
		return line.trim();
	}
}
