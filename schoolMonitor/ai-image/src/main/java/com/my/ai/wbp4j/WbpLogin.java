package com.my.ai.wbp4j;

import com.my.ai.wbp4j.Entity.PreLogin;
import com.my.ai.wbp4j.exception.Wbp4jException;
import com.my.ai.wbp4j.utils.RSAEncodeUtils;
import com.my.ai.wbp4j.utils.WbpRequest;
import com.my.ai.wbp4j.utils.WbpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WbpLogin {

    private static final Logger logger = Logger.getLogger(WbpLogin.class);

    /**
     * 预登陆url
     */
    private static final String preLoginUrl = "https://login.sina.com.cn/sso/prelogin.php?";

    /**
     * 登陆url
     */
    private static final String loginUrl = "https://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.19)";

    private static String USERNAME = "924933384@qq.com";
    private static String PASSWORD = "mm123456";

    private static PreLogin preLogin(String username) throws IOException {
        WbpRequest wbpRequest = new WbpRequest();
        String base64Username = Base64.getEncoder().encodeToString(username.getBytes());
        String params = "entry=weibo&su=" + base64Username + "MTIzNDU2&rsakt=mod&checkpin=1&client=ssologin.js(v1.4.19)&_=";
        String url = preLoginUrl + params + System.currentTimeMillis();
        WbpResponse wbpResponse = wbpRequest.doGet(url);
        ObjectMapper objectMapper = new ObjectMapper();
        PreLogin preLogin = null;
        try {
            logger.debug("pre login response info : \n " + wbpResponse.getBody());
            preLogin = objectMapper.readValue(wbpResponse.getBody(), PreLogin.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Wbp4jException("预登陆失败,可能返回了一些奇怪的东西导致无法解析.:" + e.getMessage());
        }
        return preLogin;
    }

    /**
     * 重新登陆，获取新的cookie
     */
    public static void reLogin() throws IOException {
        if (USERNAME != null && PASSWORD != null) {
            logger.debug("username:" + USERNAME + " password:" + PASSWORD);
            login(USERNAME, PASSWORD);
        }else {
            throw new Wbp4jException("内存中不存在用户名密码，重新登陆失败");
        }
    }

    public static void login(String username, String password) throws Wbp4jException, IOException {

        PreLogin preLogin = null;
        preLogin = preLogin(username);
        logger.info("预登陆成功!");
        // 根据微博加密js中密码拼接的方法
        String pwd = preLogin.getServertime() + "\t" + preLogin.getNonce() + "\n" + password;

        Map<String, Object> map = new HashMap<>();
        map.put("encoding", "UTF-8");
        map.put("entry", "weibo");
        map.put("from", "");
        map.put("gateway", 1);
        map.put("nonce", preLogin.getNonce());
        map.put("pagerefer", "https://login.sina.com.cn/crossdomain2.php?action=logout&r=https%3A%2F%2Fweibo.com%2Flogout.php%3Fbackurl%3D%252F");
        map.put("prelt", "76");
        map.put("pwencode", "rsa2");
        map.put("qrcode_flag", "false");
        map.put("returntype", "META");
        map.put("rsakv", preLogin.getRsakv());
        map.put("savestate", "7");
        map.put("servertime", preLogin.getServertime());
        map.put("service", "miniblog");
        try {
            map.put("sp", RSAEncodeUtils.encode(pwd, preLogin.getPubkey(), "10001"));
            logger.info("密码加密成功!");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeySpecException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            logger.error("密码加密失败", new Wbp4jException());
        }
        map.put("sr", "1920*1080");
        map.put("su", Base64.getEncoder().encodeToString(username.getBytes()));
        map.put("url", "https://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack");
        map.put("useticket", 1);
        map.put("vsnf", 1);

        WbpRequest wbpRequest = new WbpRequest();
        WbpResponse wbpResponse = null;
        try {
            wbpResponse = wbpRequest.doPost(loginUrl, map);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("请求失败，可能cookie读取不到，也可能是其他原因," + e.getMessage());
        }

        String body = Objects.requireNonNull(wbpResponse).getBody();
        String location = getLocation(body);
        String[] split = location.split("&");
        String reason = "";
        for (String s :
                split) {
            if (s.contains("reason")) {
                reason = s.substring(7);
                try {
                    reason = URLDecoder.decode(reason, "GBK");
                    logger.error("登陆失败，原因：" + reason, new Wbp4jException(reason));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    logger.error("无法解析该原因");
                }
            }
        }

        // 这里就登陆成功了
        logger.info("登陆成功！");
        // 获取cookie
        String cookie = getCookie(wbpResponse.getHeaders());
        try {
            CookieHolder.setCOOKIE(cookie);
            logger.info("写入cookie缓存成功!缓存地址[" + CookieHolder.getCookieFilePathAndName() + "]");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("写入cookie缓存失败", new IOException());
        }
    }

    private static String getLocation(String responseHtml) {
        int replace = responseHtml.indexOf("replace");
        String substring = responseHtml.substring(replace + 9);
        int i = substring.lastIndexOf(");");
        return substring.substring(0, i - 1);
    }

    private static String getCookie(Header[] headers) {
        StringBuilder cookie = new StringBuilder();
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("set-cookie")) {
                String temp = header.getValue();
                String substring = temp.substring(0, temp.indexOf(";"));
                cookie.append(substring);
                cookie.append("; ");
            }
        }
        String cookieStr = cookie.toString();
        return cookieStr.substring(0, cookieStr.length() - 1);
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static void setUSERNAME(String USERNAME) {
        WbpLogin.USERNAME = USERNAME;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static void setPASSWORD(String PASSWORD) {
        WbpLogin.PASSWORD = PASSWORD;
    }
}
