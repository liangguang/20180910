package com.my.ai.test;

import java.util.List;

import com.my.ai.service.CutService;
import com.my.ai.service.ExtractAudioService;
import com.my.ai.service.FileService;
import com.my.ai.service.TokenService;

public class TestService {

	
	public static void main(String[] args) {
		ExtractAudioService audioService = new ExtractAudioService();
		String outPath =  audioService.getAudioFromVideo("G:\\Youku Files\\transcode\\化学高中必修1__第2章第3节·氧化还原反应_标清.mp4", "D:\\ffmpeg4.2\\bin\\ffmpeg.exe");
		List<String> audios = new CutService().cutFile(outPath,"D:\\ffmpeg4.2\\bin\\ffmpeg.exe");
		for (String wavPath : audios) {
			String out = wavPath.substring(0,wavPath.lastIndexOf(".")) + ".pcm";
			String outPcm = CutService.processWavToPcm(wavPath, "D:\\ffmpeg4.2\\bin\\ffmpeg.exe", out);
			String result = TokenService.getResult(outPcm);
			FileService.appendFile2("G:\\Youku Files\\transcode\\化学高中必修1__第2章第3节·氧化还原反应_标清.mp4-字幕.txt", result+"\r\n");
		}
	}
	
}
