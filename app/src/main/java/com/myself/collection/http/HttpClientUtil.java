package com.myself.collection.http;

import com.myself.collection.dao.RequestParam;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ligang
 * Created 2017-7-26 17:08
 * dese  Apache 的httpClient工具类
 * <p>
 * Updata by Administrator
 * UpData 2017-7-26 17:08
 * dese  todo
 */

public class HttpClientUtil {
    //允许客户端和服务器指定与请求/响应连接有关的选项，例如这是为Keep-Alive则表示保持连接。
    private final static String CONNECTION = "Connection";
    private final static String KEEP_ALIVE = "Keep-Alive";
    private final static HttpClientUtil mInstance = new HttpClientUtil();

    public static HttpClientUtil getInstance() {
        return mInstance;
    }

    private HttpClient createHttpClient(int connTimeOut, int soTimeOut, boolean isDelay) {
        HttpParams params = new BasicHttpParams();
        //设置连接超时时间
        HttpConnectionParams.setConnectionTimeout(params, connTimeOut);
        //设置请求超时时间
        HttpConnectionParams.setSoTimeout(params, soTimeOut);
        //设置tcp连接协议是否延迟
        HttpConnectionParams.setTcpNoDelay(params, isDelay);
        //设置使用http协议的版本
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        //设置内容数据的编码格式
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpClient httpClient = new DefaultHttpClient(params);
        return httpClient;
    }

    /**
     * get请求
     */
    public String getRequest(String url, int connTimeOut, int soTimeOut, boolean isDelay) {
        String content = "";
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(CONNECTION, KEEP_ALIVE);
        try {
            HttpClient httpClient = createHttpClient(connTimeOut, soTimeOut, isDelay);
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                InputStream inputStream = response.getEntity().getContent();
                content = readStream(inputStream);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * post请求
     * @param url
     * @param connTimeOut
     * @param soTimeOut
     * @param isDelay
     * @param params  请求参数集合
     * @return
     */
    public String postRequest(String url, int connTimeOut, int soTimeOut, boolean isDelay ,
                              ArrayList<RequestParam> params) {
        String content = "";
        HttpPost httpPost = new HttpPost(url);
        try {
            HttpClient httpClient = createHttpClient(connTimeOut, soTimeOut, isDelay);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                InputStream inputStream = response.getEntity().getContent();
                content = readStream(inputStream);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    //读取流数据
    public String readStream(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


}
