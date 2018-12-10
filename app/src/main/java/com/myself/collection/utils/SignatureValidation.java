package com.myself.collection.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @@author by Jams
 * @@date time 2018/11/13 0013 16:00
 * @@dese 签名校验:防止二次打包
 * \n
 * @@UpAuthor by Administrator
 * @@UpDate 2018/11/13 0013 16:00
 * @@dese todo
 */
public class SignatureValidation {
    private static SignatureValidation instance;
    private SignatureValidation(){}

    public static SignatureValidation getInstance(){
        if (instance == null){
            synchronized (SignatureValidation.class){
                if (instance == null){
                    instance = new SignatureValidation();
                }
            }
        }
        return instance;
    }

    public String getSignatureMD5(Context context){
        String signature = "";
        if (context == null){
            return signature;
        }
        try {
            /*通过包管理器获取指定包名包含签名的包信息*/
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            /*通过返回的包信息获取签名数组*/
            Signature[] signatures = packageInfo.signatures;
            Signature sign = signatures[0];
            byte[] bytes = sign.toByteArray();
            signature = encryptionMD5(sign.toByteArray());
        }catch (Exception e){
            e.printStackTrace();
            signature = "";
        }
        return signature;
    }

    /**
     * MD5加密
     * @param byteStr 需要加密的内容
     * @return 返回 byteStr的md5值
     */
    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }


    public String getSignatureSHA1(Context cxt){
        String sha1 = "";
        try {
            PackageInfo packageInfo = cxt.getPackageManager().getPackageInfo(cxt.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            StringBuilder builder = new StringBuilder();
            for (Signature sign : signatures){
                builder.append(sign.toCharsString());
            }
            sha1 = builder.toString();
        }catch (Exception e){
            e.printStackTrace();

        }
        return sha1;
    }

    /***
     *  true:already in using  false:not using
     * @param port
     */
    public static boolean isLoclePortUsing(int port){
        boolean flag = true;
        try {
            flag = isPortUsing("127.0.0.1", port);
        } catch (Exception e) {
        }
        return flag;
    }
    /***
     *  true:already in using  false:not using
     * @param host
     * @param port
     * @throws UnknownHostException
     */
    public static boolean isPortUsing(String host,int port) throws UnknownHostException {
        boolean flag = false;
        InetAddress theAddress = InetAddress.getByName(host);
        try {
            Socket socket = new Socket(theAddress,port);
            flag = true;
        } catch (IOException e) {
        }
        return flag;
    }

}
