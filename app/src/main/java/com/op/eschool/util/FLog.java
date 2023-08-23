package com.op.eschool.util;


import android.util.Log;



public class FLog {

    static  boolean showingFlag = true ;
    public static void w(String tag , String message){
       if (showingFlag) {
           Log.w("" + tag, "" + message);
       }

    }
    public static void v(String tag , String message){
        if (showingFlag)
        Log.v(""+tag , ""+message) ;
    }
    public static void e(String tag , String message){
        if (showingFlag) {Log.e(""+tag , ""+message) ;}
    }
    public static void d(String tag , String message){
        if (showingFlag) {Log.d(""+tag , ""+message) ;}
    }
    public static void i(String tag , String message){
        if (showingFlag) {Log.i(""+tag , ""+message) ;}
    }



}
