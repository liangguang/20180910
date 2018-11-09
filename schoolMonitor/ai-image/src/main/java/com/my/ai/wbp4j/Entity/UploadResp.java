package com.my.ai.wbp4j.Entity;

import com.my.ai.wbp4j.Entity.upload.Data;

public class UploadResp {

    private String code;
    private Data data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UploadResp{" +
                "code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}
