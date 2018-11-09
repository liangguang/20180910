package com.my.ai.wbp4j.utils;

import java.util.Map;


public class MapParamsUtils {

    public static String toString(Map<String, Object> params){
        StringBuilder stringBuilder = new StringBuilder();
        // 参数
        for (Map.Entry<String, Object> entry : params.entrySet()) {

            stringBuilder.append(entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append(entry.getValue());
            stringBuilder.append("&");
        }
        int length = stringBuilder.length();
        return stringBuilder.substring(0, length - 1);
    }
}
