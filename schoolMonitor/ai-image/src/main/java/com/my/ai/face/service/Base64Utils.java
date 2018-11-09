package com.my.ai.face.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class Base64Utils {

	private static Logger logger = LoggerFactory.getLogger(Base64Utils.class);

	/**
	 * 图片文件转成base64码，没有缓冲，所以不适合大图片
	 * 
	 * @param picPath
	 * @return
	 * @throws IOException
	 */
	public static String picToBase64(String picPath, boolean withHead) {
		String result = null;
		try {
			Path path = Paths.get(picPath);
			byte[] imageContents = Files.readAllBytes(path);
			result = Base64.getEncoder().encodeToString(imageContents);
			logger.debug("图片 {} 转完base64后的长度为 {}", picPath, result.length());
			if (StringUtils.hasLength(result) && withHead) {
				String probeContentType = Files.probeContentType(path);
				result = "data:" + probeContentType + ";base64," + result;
			}
		} catch (IOException e) {
			logger.error("转换base64出错，原因为 {} {}", e.getMessage(), e);
		}
		return result;
	}

	public static boolean base64ToPic(String picBase64, String destination) {
		Path directoryPath = Paths.get(destination).getParent();
		if (!Files.exists(directoryPath, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
			try {
				Files.createDirectories(directoryPath);
			} catch (IOException e) {
				logger.error("创建文件夹 {} 失败，原因 {} {}", directoryPath, e.getMessage(), e);
				return false;
			}
		}
		try (FileOutputStream fos = new FileOutputStream(destination)) {
			byte[] decode = Base64.getDecoder().decode(picBase64);
			fos.write(decode);
		} catch (IOException e) {
			logger.error("转换图片出错，原因为 {} {}", e.getMessage(), e);
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		String picToBase64 = picToBase64("D:\\1234.jpg", true);
		System.out.println(picToBase64);
		// System.out.println(base64ToPic(picToBase64, "D:\\4321.jpg"));
	}
}
