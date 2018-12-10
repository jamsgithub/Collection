package com.myself.collection;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.myself.collection.utils.CrashHandler;
import com.myself.collection.utils.SignatureValidation;

/**
 * @author by ligang
 * @date 2017-6-16 11:50
 * @dese
 * <p>
 * @Updata by Administrator
 * UpData 2017-6-16 11:50
 * dese
 */

public class MyApplication extends Application {
    //获取应用的签名值信息:keytool -list -v -keystore collection.jks
    private final String APP_SIGN_MD5 = "cde29f94062eefbb756b73dfb3287ea9";
    private final String APP_SIGN_SHA1 = "308201dd30820146020101300d06092a864886f70d010105050030373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b3009060355040613025553301e170d3138313030383036343833335a170d3438303933303036343833335a30373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b300906035504061302555330819f300d06092a864886f70d010101050003818d00308189028181008d426f4f027b07dc41c78d696f9d2a955d786d4b55062105d718b7e564e1d9186d2458687d539524a2f31c79ff72325f4c0b9bc60131899db0e1c14cd3ebfec52f12180a3d92ad8025a43ca63bffcde8201fac0ddd9c152c96d3b1761d76c126fc97886b84e8875ef310bf9f5b5ccff939ab238e11cab215f2f3b35f345528530203010001300d06092a864886f70d0101050500038181001cb6f387246b9d6fca1f07f286d529bb6d03c7daa79f9b044e60f27efd44d500949e4cf632435f8f3d290e11908fd6a31afec1e1d65c61368142e8321e0e849e9b6ad7e29dddb383b87a7d6a8abeffb168c62b2fc794d8620b697ac4622e4f611af30a128f51740c00a0508381d4e30b4eddac8916b90e46fa1bbcd3c7813f9c";
    private final String APP_SIGN_SHA256 = "61:18:37:7A:BE:F3:AC:F9:EF:A2:56:75:A5:DB:4B:87:A2:4F:1D:38:AC:C8:E0:B0:AC:27:9D:8A:50:9D:F7:A4";
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(getApplicationContext());
        String signatureMD5 = SignatureValidation.getInstance().getSignatureMD5(this);
        String signatureSHA1 = SignatureValidation.getInstance().getSignatureSHA1(this);
        if (TextUtils.isEmpty(signatureSHA1) || !signatureSHA1.equals(APP_SIGN_SHA1)){
            Log.i("jw" , "it not is own app...exit app!");
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
