package com.myself.collection.dao;

/**
 * @Created by ligang
 * @Created time 2017-8-4 11:31
 * @dese todo
 * \n
 * @UpUser by Administrator
 * @UpDate 2017-8-4 11:31
 * @dese todo
 */

public class IpModel {
    private int code;
    private IpData data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return this.code;
    }
    public void setData(IpData data) {
        this.data = data;
    }
    public IpData getData() {
        return this.data;
    }
}
