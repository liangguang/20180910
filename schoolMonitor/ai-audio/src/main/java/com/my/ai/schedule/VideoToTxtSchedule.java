package com.my.ai.schedule;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.my.ai.service.CutService;
import com.my.ai.service.ExtractAudioService;
import com.my.ai.service.FileService;
import com.my.ai.service.TokenService;

@Component
public class VideoToTxtSchedule extends ParentSchedule{
	
	public static final Logger logger =  LoggerFactory.getLogger(VideoToTxtSchedule.class);

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public static SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute() {
				
	}

	@Override
	public void executeTimeTask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeOtherTask() {
		
	}

	public static void main(String[] args) throws Throwable {
		ExtractAudioService audioService = new ExtractAudioService();
		String dir = "E:\\QLDownload";
		File fileDir = new File(dir);
		File[] files = fileDir.listFiles();
		for (File file : files) {
			try {
				if(file.isDirectory()) {
					for (File video : file.listFiles(new FilenameFilter() {
						
						@Override
						public boolean accept(File dir, String name) {
							if(name.endsWith("mp4") || name.endsWith("flv")) {
								return true;
							}
							return false;
						}
					})){
						String outPath =  audioService.getAudioFromVideo(video.getAbsolutePath(), "D:\\ffmpeg4.2\\bin\\ffmpeg.exe");
						List<String> audios = new CutService().cutFile(outPath,"D:\\ffmpeg4.2\\bin\\ffmpeg.exe");
						for (String wavPath : audios) {
							String out = wavPath.substring(0,wavPath.lastIndexOf(".")) + ".pcm";
							String outPcm = CutService.processWavToPcm(wavPath, "D:\\ffmpeg4.2\\bin\\ffmpeg.exe", out);
							String result = TokenService.getResult(outPcm);
							FileService.appendFile2(file.getAbsolutePath()+"-字幕.txt", result+"\r\n");
						}
					}
				}
				
			} catch (Throwable e) {
				System.out.println(file.getAbsolutePath() + "|| "+ e.getMessage());
			}
		}
	
	
	}
	
}
