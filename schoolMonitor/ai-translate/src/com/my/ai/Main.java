package com.my.ai;

import com.my.ai.translate.TransApi;

public class Main {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20181016000220072";
    private static final String SECURITY_KEY = "rDYSK0y8kii9m9m5CVzk";

    public static void main(String[] args) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        String query = "高度600米";
        System.out.println(api.getTransResult(query, "auto", "en"));
    }

}
