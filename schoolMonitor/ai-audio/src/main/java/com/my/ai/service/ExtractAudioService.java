package com.my.ai.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//视频抽取音频
@Service
public class ExtractAudioService {

	public static Logger logger  = LoggerFactory.getLogger(ExtractAudioService.class);
	
	public static void main(String[] args) {
		new ExtractAudioService().getAudioFromVideo("E:\\QLDownload\\氧化还原反应中电子转移的方向和数目的表示方法\\氧化还原反应中电子转移的方向和数目的表示方法.mp4",
				"D:\\ffmpeg4.2\\bin\\ffmpeg.exe");
	}
	
	
	public String getAudioFromVideo(String videoPath,String ffmpegPath) {
		File video = new File(videoPath);
		if(video.exists() && video.isFile()){
			String format = "wav";
			String outPath = videoPath.substring(0,videoPath.lastIndexOf(".")) + ".wav";
			processCmd(videoPath, ffmpegPath, format, outPath);
			return outPath;
		}
		return null;
	}
	
	//D:\ffmpeg4.2\bin\ffmpeg.exe -i 氧化还原反应中电子转移的方向和数目的表示方法.mp4 -f wav -vn -y 3.wav
	public String processCmd(String inputPath,String ffmpegPath,String format,String outPath) {
		List<String> commend = new java.util.ArrayList<String>();
		commend.add(ffmpegPath);
		commend.add("-i");
		commend.add(inputPath);
		commend.add("-y");
		commend.add("-vn");
		commend.add("-f");
		commend.add(format);
		commend.add(outPath);
		try {

			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			builder.redirectErrorStream(true);
			Process p = builder.start();

			// 1. start
			BufferedReader buf = null; // 保存ffmpeg的输出结果流
			String line = null;
			// read the standard output

			buf = new BufferedReader(new InputStreamReader(p.getInputStream()));

			StringBuffer sb = new StringBuffer();
			while ((line = buf.readLine()) != null) {
				System.out.println(line);
				sb.append(line);
				continue;
			}
			p.waitFor();// 这里线程阻塞，将等待外部转换进程运行成功运行结束后，才往下执行
			// 1. end
			return sb.toString();
		} catch (Exception e) {
//		            System.out.println(e);    
			return null;
		}
	}

}
