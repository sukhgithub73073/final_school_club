package com.op.eschool.base;

import android.app.Application;
import android.content.Context;


import com.op.eschool.util.CustomUncaughtExceptionHandler;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class MyApplication extends Application {
    private static MyApplication sInstance;
    @Contract(pure = true)
    @Nullable
    public static Context getAppContext() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Thread.setDefaultUncaughtExceptionHandler(new CustomUncaughtExceptionHandler(getApplicationContext()));


    }
}