package com.lgp.monitor.model;

import java.io.Serializable;

public class FileRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7532094227465864096L;
	private String fileName;
	private long fileSize;
	private long lastModify;
	private boolean found = false;
	private boolean changing = false;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public long getLastModify() {
		return lastModify;
	}

	public void setLastModify(long lastModify) {
		this.lastModify = lastModify;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public boolean isChanging() {
		return changing;
	}

	public void setChanging(boolean changing) {
		this.changing = changing;
	}
}