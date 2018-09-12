package com.lgp.utils.system;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class SoundPlayUtil {

	public static void playSound(String mp3){

		try {
			    File file = new File(mp3);
				AudioInputStream cin = AudioSystem.getAudioInputStream(file);
				AudioFormat format = cin.getFormat();
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
				SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
				line.open(format);//或者line.open();format参数可有可无
				line.start();
				int nBytesRead = 0;
				byte[] buffer = new byte[512];
				while (true) {
				    nBytesRead = cin.read(buffer, 0, buffer.length);
				    if (nBytesRead <= 0)
				        break;
				    line.write(buffer, 0, nBytesRead);
				}
				line.drain();
				line.close(); 
			} catch (Throwable e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}		
	}
}
