package com.myself.collection.http;

import android.os.Build;
import android.text.TextUtils;

import com.myself.collection.dao.RequestParam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ligang
 * Created 2017-7-26 20:09
 * dese  java的httpUrlConnection工具类  Android2.2版本之前，HttpUrlConnection会存在一些bug，比如对一个
 * 可读流用close()方法是，可能会导致连接池失效。
 * dese httpUrlConnection的API简单，体积小，因而非常适用于Android项目。压缩和缓存机制可以有效的减少网络
 * 访问的流量，在提升速度和省电方面也起到了较大的作用。
 * <p>
 * Updata by Administrator
 * UpData 2017-7-26 20:09
 * dese  todo
 */

public class HttpUrlConnectionUtil {

    private final static String CONNECTION = "Connection";
    private final static String KEEP_ALIVE = "Keep-Alive";
    private final static HttpUrlConnectionUtil mInstance = new HttpUrlConnectionUtil();

    public static HttpUrlConnectionUtil getInstance() {
        disableConnectionReuseIfNecessary();
        return mInstance;
    }

    private static void disableConnectionReuseIfNecessary() {
        // 这是一个2.2版本之前的bug,禁止调连接池的功能。
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    /**
     * get请求时拼接参数
     * @param url
     * @param params
     * @return
     */
    public URL getParams(String url, ArrayList<RequestParam> params) {
        try {
            StringBuffer stringBuffer = new StringBuffer(url);
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    stringBuffer.append("&");
                    stringBuffer.append(params.get(i).getName());
                    stringBuffer.append("=");
                    stringBuffer.append(params.get(i).getValue());
                }
            }

            String sb = stringBuffer.toString();
            URL realUrl = new URL(sb);
            return realUrl;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * post 请求时写入参数
     * @param os
     * @param params
     * @throws IOException
     */
    public void postParams(OutputStream os, List<RequestParam> params) throws IOException {
        StringBuffer buffer = new StringBuffer();
        if (params != null && params.size() > 0) {
            for (RequestParam param : params) {
                if (!TextUtils.isEmpty(buffer)) {
                    buffer.append("&");
                }
                buffer.append(URLEncoder.encode(param.getName(), "utf-8"));
                buffer.append("=");
                buffer.append(URLEncoder.encode(param.getValue(), "utf-8"));
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
            writer.write(buffer.toString());
            writer.flush();
            writer.close();
        }
    }

    public String netWorkRequest(String url, ArrayList<RequestParam> params, int timeOut, String method ) {
        try {
            String content = "";
            HttpURLConnection httpConnection = null;
            URL realUrl = null;
            if (TextUtils.isEmpty(method)) {
                method = "GET";
            }
            if (method.equals("POST")) {
                realUrl = new URL(url);
                // postParams(httpConnection.getOutputStream(), params);
            } else if (method.equals("GET")) {
                realUrl = getParams(url, params);
            }
            httpConnection = (HttpURLConnection) realUrl.openConnection();
            httpConnection.setRequestMethod(method);
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setReadTimeout(timeOut);
            httpConnection.setConnectTimeout(timeOut);

            if (method.equals("POST")){
                postParams(httpConnection.getOutputStream() , params);
            }
            httpConnection.connect();
            int code = httpConnection.getResponseCode();
            if (code == 200) {
                InputStream inputStream = httpConnection.getInputStream();
                content = readStream(inputStream);
                inputStream.close();
            }
            return content;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public String readStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();
        return buffer.toString();
    }
}
