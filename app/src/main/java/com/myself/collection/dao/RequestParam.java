package com.myself.collection.dao;

import org.apache.http.NameValuePair;

/**
 * Created by ligang
 * Created 2017-7-26 17:37
 * dese  请求参数封装类
 * <p>
 * Updata by Administrator
 * UpData 2017-7-26 17:37
 * dese  todo
 */

public class RequestParam implements NameValuePair {
    private String value;
    private String key;
    public RequestParam(String key , String name){
        this.value = name;
        this.key = key;
    }

    @Override
    public String getName() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
