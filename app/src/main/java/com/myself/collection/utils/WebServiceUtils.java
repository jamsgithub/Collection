package com.myself.collection.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  by ligang
 * Created 2017-5-24 16:38
 * dese   WebService网络请求工具类   WebService是一种基于xml协议的网络请求
 * <p>
 * Updata by Administrator
 * UpData 2017-5-24 16:38
 * dese
 */
public class WebServiceUtils {
    //单例模式
    private WebServiceUtils mInstance = new WebServiceUtils();
    private final String NAMESPACE = "http://www.w3.org/2001/12/soap-envelope";
    private final String URL = "http://";
    private final String HOST = "10.248.71.196";
    private final int POST = 7001;
    private final String FILE = "/services/GwMobileService";
    private final String SERVICE_URL = URL + HOST + POST + FILE;

    private WebServiceUtils() {
    }

    public WebServiceUtils getInstance() {
        return mInstance;
    }

    static class webParam {
        public String name;
        public Object value;

        /**
         * @param name  参数名
         * @param value 参数值
         */
        public webParam(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }

    static class webParamsBuilder {
        List<webParam> params;

        public webParamsBuilder() {
            params = new ArrayList<>();
        }

        public webParamsBuilder addParam(String name, Object value) {
            params.add(new webParam(name, value));
            return this;
        }

        public List<webParam> build() {
            return params;
        }
    }

    /**
     * @param action 请求的方法名
     * @param params 请求参数集合
     * @return 返会网络请求返回的数据
     */
    public String doWebService(String action, List<webParam> params) {
        SoapObject request = new SoapObject(NAMESPACE, action);
        for (webParam param : params) {
            if (param.value == null) {
                return "";
            }
            request.addProperty(param.name, param.value);
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(request);
//        HttpTransportSE
        //是使用https协议
//        HttpsTransportSE transport = new HttpsTransportSE(HOST , POST ,FILE , 5000);
        AndroidHttpTransport transport = new AndroidHttpTransport(SERVICE_URL);
        try {
            transport.call(action , envelope);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        Object bodyIn = envelope.bodyIn;
        if (bodyIn instanceof SoapObject){
            SoapObject object = (SoapObject) bodyIn;
            String result = object.getProperty(0).toString();
            return result;
        }else if (bodyIn instanceof SoapFault){
            return "";
        }
        return "";
    }

    /**
     * @param action  请求服务器的方法名
     * @param param1  请求的参数一
     * @param param2  请求的参数二
     * @return  返回网络请求的数据
     * @des in0  in1  参数名
     */
    public String getMsg(String action , String param1 , String param2){
        List<webParam> params = new webParamsBuilder()
                .addParam("in0" , param1)
                .addParam("in1" , param2).build();
        return doWebService(action , params);
    }

    /**
     * @des  将服务器返回的json数据转换成实体类
     * @param action  方法名
     * @param param1  参数一
     * @param param2 参数二
     * @param clazz  转换的数据类
     * @param <T>    抽象数据类
     * @return    数据解析的实体类
     */
    public  <T>Object getDataObject(String action , String param1 , String param2 , Class<T> clazz){
        String msg = getMsg(action, param1, param2);
        if (!TextUtils.isEmpty(msg)){
            T t = JSON.parseObject(msg, clazz);
            return t;

        }
        return null;
    }

    /**
     * @des  将服务器返回的json数据转换成集合实体类
     * @param action  方法名
     * @param param1  参数一
     * @param param2 参数二
     * @param clazz  转换的数据类
     * @param <T>    抽象数据类
     * @return    数据解析的实体类
     */
    public <T>  ArrayList<T> getDataArray(String action , String param1 , String param2 , Class<T> clazz){
        String msg = getMsg(action, param1, param2);
        if (!TextUtils.isEmpty(msg)){
            return (ArrayList<T>) JSON.parseArray(msg , clazz);
        }
        return new ArrayList<>();
    }
}
