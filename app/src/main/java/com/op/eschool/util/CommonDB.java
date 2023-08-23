package com.op.eschool.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

//class for shared pri
public class CommonDB {
    Context context ;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;

    public CommonDB(Context context) {
    this.context = context ;
         sharedPreferences = context.getSharedPreferences("db",MODE_PRIVATE);
         editor = sharedPreferences.edit();
    }

    public   void putString(String key , String value){
        editor.putString(key, value) ;
        editor.commit() ;

    }
    public   void clearAll(){
        editor.clear();
        editor.commit() ;

    }
    public   String getString(String key){
        return  sharedPreferences.getString(key , "") ;
    }
    public   void putInt(String key , int value){
        editor.putInt(key, value) ;
        editor.commit() ;
    }
    public   Integer getInt(String key){
        return  sharedPreferences.getInt(key , 0) ;
    }
}
