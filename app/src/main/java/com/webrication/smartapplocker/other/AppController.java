package com.webrication.smartapplocker.other;

import android.app.Application;
import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.LogInterceptor;

/**
 * Created by virinchi on 3/11/17.
 */

public class AppController extends Application {

    private Thread.UncaughtExceptionHandler defaultUEH;
    @Override
    public void onCreate() {
        super.onCreate();
        init();

    }
    public void init()
    {
        long startTime = System.currentTimeMillis();

        Hawk.init(this).setLogInterceptor(new LogInterceptor() {
            @Override public void onLog(String message) {
                Log.d("HAWK", message);
            }
        }).build();

        long endTime = System.currentTimeMillis();
    }




}
