package com.lgp.monitor.qq.model;

public class QqChatResponse extends BaseResponse{

	private ChatResponse data;

	public ChatResponse getData() {
		return data;
	}

	public void setData(ChatResponse data) {
		this.data = data;
	}
}
