package com.op.eschool.base;

import android.app.Application;
import android.content.Context;


import com.op.eschool.util.CustomUncaughtExceptionHandler;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    public  static List<String> studentRegisterList  = new ArrayList<>() ;
    public  static List<String> staffRegisterList  = new ArrayList<>() ;
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