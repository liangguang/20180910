package com.my.ai.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CutService {

	public static Logger logger = LoggerFactory.getLogger(CutService.class);

	public List<String> cutFile(String media_path, String ffmpeg_path) {

		List<String> audios = new ArrayList<>();
		int mediaTime = getMediaTime(media_path, ffmpeg_path); 
		int num = mediaTime / 59;
		int lastNum = mediaTime % 59;
		System.out.println(mediaTime +"|" + num + "|"+ lastNum);
		int length = 59;
		File file = new File(media_path);
		String filename = file.getName();
		for (int i = 0; i < num; i++) {
			String outputPath = file.getParent() + File.separator + i + "-"+filename;
			processCmd(media_path, ffmpeg_path, String.valueOf(length * i) , 
					String.valueOf(length), outputPath);
			audios.add(outputPath);
		}
		if(lastNum > 0) {
			String outputPath = file.getParent() + File.separator + num + "-"+filename;
			processCmd(media_path, ffmpeg_path, String.valueOf(length * num) , 
					String.valueOf(lastNum), outputPath);
			audios.add(outputPath);
		}
		return audios;
	}

	/**
	 * 获取视频总时间
	 * 
	 * @param viedo_path  视频路径
	 * @param ffmpeg_path ffmpeg路径
	 * @return
	 */
	public int getMediaTime(String video_path, String ffmpeg_path) {
		List<String> commands = new java.util.ArrayList<String>();
		commands.add(ffmpeg_path);
		commands.add("-i");
		commands.add(video_path);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			final Process p = builder.start();

			// 从输入流中读取视频信息
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			System.out.println(sb.toString());
			br.close();

			// 从视频信息中解析时长
			String regexDuration = "Duration: (.*?), bitrate: (\\d*) kb\\/s";
			Pattern pattern = Pattern.compile(regexDuration);
			Matcher m = pattern.matcher(sb.toString());
			if (m.find()) {
				int time = getTimelen(m.group(1));
				System.out
						.println(video_path + ",视频时长：" + time + ",比特率：" + m.group(2) + "kb/s");
				return time;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 格式:"00:00:10.68"
	public int getTimelen(String timelen) {
		int min = 0;
		String strs[] = timelen.split(":");
		if (strs[0].compareTo("0") > 0) {
			min += Integer.valueOf(strs[0]) * 60 * 60;// 秒
		}
		if (strs[1].compareTo("0") > 0) {
			min += Integer.valueOf(strs[1]) * 60;
		}
		if (strs[2].compareTo("0") > 0) {
			min += Math.round(Float.valueOf(strs[2]));
		}  
		return min;
	}
   
	//D:\ffmpeg4.2\bin\ffmpeg.exe -i 123.pcm -ss 0 -t 59 1-123.wav
	public String processCmd(String inputPath,String ffmpegPath,
			String startTime,String length,String outputPath) {
		List<String> commend = new java.util.ArrayList<String>();
		commend.add(ffmpegPath);
		commend.add("-i");
		commend.add(inputPath);
		commend.add("-ss");
		commend.add(startTime);
		commend.add("-t");
		commend.add(length);
		commend.add(outputPath);
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
            System.out.println(e);    
			return null;
		}
	}

	//ffmpeg -y  -i 16k.wav  -acodec pcm_s16le -f s16le -ac 1 -ar 16000 16k.pcm 
	public static String processWavToPcm(String inputPath,String ffmpegPath,String outputPath) {
		List<String> commend = new java.util.ArrayList<String>();
		commend.add(ffmpegPath);
		commend.add("-i");
		commend.add(inputPath);
		commend.add("-acodec");
		commend.add("pcm_s16le");
		commend.add("-f");
		commend.add("s16le");
		commend.add("-ac");
		commend.add("1");
		commend.add("-ar");
		commend.add("16000");
		commend.add(outputPath);
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
			return outputPath;
					//sb.toString();
		} catch (Exception e) {
            System.out.println(e);    
			return null;
		}
	}

	
	
	
	public static void main(String[] args) {
		List<String> audios = new CutService().cutFile(
				"E:\\QLDownload\\氧化还原反应中电子转移的方向和数目的表示方法\\氧化还原反应中电子转移的方向和数目的表示方法.wav",
				"D:\\ffmpeg4.2\\bin\\ffmpeg.exe");
		System.out.println(audios.size());
		
		for (String wavPath : audios) {
			String out = wavPath.substring(0,wavPath.lastIndexOf(".")) + ".pcm";
			processWavToPcm(wavPath, "D:\\ffmpeg4.2\\bin\\ffmpeg.exe", out);
		}
		
	}

}
