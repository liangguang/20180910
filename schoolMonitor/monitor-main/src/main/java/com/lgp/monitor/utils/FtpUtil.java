package com.lgp.monitor.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.MalformedServerReplyException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil extends FTPClient {
	
	private boolean FSendForm = false;
	private boolean FNsml2 = false;

	public String[] listArray() throws IOException {
		try (Socket socket = _openDataConnection_("LIST", null)) {
			getReplayCode(_controlInput_.readLine());
			if (!FTPReply.isPositiveCompletion(_replyCode)) {
				throw new IOException("Failed to retrieve directory listing.");
			}
			try (InputStream is = socket.getInputStream()) {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, getControlEncoding()))) {
					List<String> lst = new ArrayList<String>();
					String line = "";
					while ((line = reader.readLine()) != null) {
						lst.add(line);
					}
					return lst.toArray(new String[0]);
				}
			}
		}
	}

	private void getReplayCode(String line) throws MalformedServerReplyException {
		String code = null;
		try {
			code = line.substring(0, REPLY_CODE_LEN);
			_replyCode = Integer.parseInt(code);
		} catch (NumberFormatException e) {
			throw new MalformedServerReplyException("Could not parse response code.\nServer Reply: " + line);
		}
	}

	public void setSendForm(boolean Value) throws IOException {
		String cmd = String.format("SITE SENDFORM = %d", Value ? 1 : 0);
		if (FTPReply.isPositiveCompletion(sendCommand(cmd))) {
			FSendForm = Value;
		}
	}

	public void setNsml2(boolean Value) throws IOException {
		String cmd = String.format("SITE FORMAT=%snsml", Value ? "2" : "");
		if (FTPReply.isPositiveCompletion(sendCommand(cmd))) {
			FNsml2 = Value;
		}
	}

	public boolean isSendform() {
		return FSendForm;
	}

	public boolean isNsml2() {
		return FNsml2;
	}

	@Override
	public int cdup() throws IOException {
		return cwd("..");
	}

	public void changeDirUp() throws IOException {
		changeDir("..");
	}

	public void PutNsmlText(String fileName, String nsml) throws IOException {
		storeFile(fileName, new ByteArrayInputStream(nsml.getBytes("UTF-8")));
	}

	public void PutFile(String ASourceFile) throws IOException {
		try (FileInputStream fis = new FileInputStream(ASourceFile)) {
			try (OutputStream tos = storeFileStream(ASourceFile.replaceAll("^.*[\\\\/]", ""))) {
				byte[] buf = new byte[65535];
				int len = 0;
				while ((len = fis.read(buf)) > 0) {
					tos.write(buf, 0, len);
				}
				tos.flush();
			}
		}
	}

	public void cd(String aDir) throws IOException {
		if (!changeDir(aDir)) {
			throw new IOException("Dir changed failed.");
		}
	}

	public boolean changeDir(String aDir) throws IOException {
		return super.changeWorkingDirectory(aDir);
	}

	public boolean SetMaxItemCount(int aMax) throws IOException {
		return FTPReply.isPositiveCompletion(sendCommand("SITE LISTSZ=" + Integer.toString(aMax)));
	}

	public String retrieveCurrentDir() throws IOException {
		boolean success = FTPReply.isPositiveCompletion(pwd());
		if (success) {
			// 掐头去尾
			return _replyLines.get(0).replaceAll("^[^\\\"]*\\\"|\\\".*$", "");
		} else {
			return null;
		}
	}


	public void openServer(String host) throws SocketException, IOException {
		openServer(host, 21);
	}

	public void openServer(String host, int port) throws SocketException, IOException {
		setControlEncoding("UTF-8");
		this.connect(host, port);
	}

	public static void main(String[] args) {
		FtpUtil nf = new FtpUtil();
		try {
			nf.setCharset(Charset.forName("UTF-8"));
			nf.setAutodetectUTF8(true);
			nf.openServer("192.168.10.61", 21);
			if (!nf.login("avstar", "avstar")) {
				return;
			}
			System.out.println(nf.changeDir("system"));
			// nf.changeDirUp();
			System.out.println(nf.changeDir("test.test3"));
			System.out.println(nf.SetMaxItemCount(500));
			System.out.println(nf.retrieveCurrentDir());
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
}
