package com.sibyl.mirainikki.MyApplication;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Sasuke on 2016/5/12.
 */
public class MyApplication extends Application{
    public ExecutorService executor;//线程池
    public static MyApplication app;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        app.executor = Executors.newCachedThreadPool();//启动的时候就建立线程池

        //int progress = Runtime.getRuntime().availableProcessors();
        //Log.d("SasukeLog",this.toString()+"可用线程数："+progress);
       //app.executor = Executors.newFixedThreadPool(progress*2);//开progress个固定线程
    }

    public static MyApplication getInstance(){
        return app;
    }
}
