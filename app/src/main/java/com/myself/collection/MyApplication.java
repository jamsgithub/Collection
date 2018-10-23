package com.myself.collection;

import android.app.Application;

import com.myself.collection.utils.CrashHandler;

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
    @Override
    public void onCreate() {
        super.onCreate();

        CrashHandler handler = CrashHandler.getInstance();
        handler.init(getApplicationContext());
    }
}
