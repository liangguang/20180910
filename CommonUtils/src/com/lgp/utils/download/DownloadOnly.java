package com.lgp.utils.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;

/**
 * <p>
 * 文件下载,可以支持断点续传
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class DownloadOnly {

	boolean ddxc = true;

	int startIndex = 0;

	long downloadSize = 0;

	boolean downloadFinish = false;

	int totleSize = 0;

	public boolean download(String url, String file_path, int downloadTimeout) {

		// 起一个线程 检测下载进度
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					NumberFormat nt = NumberFormat.getPercentInstance();
					// 设置百分数精确度3即保留三位小数
					nt.setMinimumFractionDigits(1);
					while (!downloadFinish) {
						Thread.sleep(30000);
						System.out.println(
								"已下载大小" + getDownloadSize() + ",进度" + nt.format(getDownloadSize() * 1.0 / totleSize));
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}).start();

		System.out.println("下载文件：源路径" + url + ",目标路径:" + file_path);
		RandomAccessFile raf = null;
		InputStream in = null;

		try {
			URL file_url = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) file_url.openConnection();
			conn.setConnectTimeout(downloadTimeout);
			conn.setRequestMethod("GET");
			File tmpFile = new File(file_path + "_tmp");
			if (ddxc) {
				if (tmpFile.exists() && tmpFile.isFile()) {
					downloadSize = tmpFile.length();
					startIndex = (int) downloadSize;
				}
				conn.setRequestProperty("Range", "bytes=" + startIndex + "-");
			} else {
				if (tmpFile.exists() && tmpFile.isFile())
					tmpFile.delete();
			}
			int status = conn.getResponseCode();
			totleSize = (int) downloadSize + conn.getContentLength();
			System.out
					.println("文件总大小" + totleSize + "下载请求获得的返回状态码：" + status + "=需要下载的大小:" + (totleSize - downloadSize));
			if (status == 200 || status == 206) {
				raf = new RandomAccessFile(tmpFile, "rwd");
				raf.seek(startIndex);
				in = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int size = 0;
				while ((size = in.read(buffer)) != -1) {
					raf.write(buffer, 0, size);
					downloadSize += size;
				}
				raf.close();
				in.close();
				File dest = new File(file_path);
				return tmpFile.renameTo(dest);
			}
		} catch (Throwable e) {
			System.out.println("文件下载失败：" + e.getMessage());
		} finally {
			downloadFinish = true; // 下载完成或中断
		}
		return false;
	}

	public long getDownloadSize() {
		return downloadSize;
	}

	public static void main(String[] args) {
		DownloadOnly downloadOnly = new DownloadOnly();
		downloadOnly.download(args[0], args[1], 10000);
	}

}
