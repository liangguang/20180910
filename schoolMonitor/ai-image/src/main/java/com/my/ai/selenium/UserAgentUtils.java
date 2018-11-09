package com.my.ai.selenium;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UserAgentUtils {

	public static List<String> readTxt(File file){
		List<String> list = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				System.out.println(line);
				list.add(line);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	public static void main(String[] args) {
		UserAgentUtils.readTxt(new File("config/phone-user-agent.txt"));
	}
	
}
