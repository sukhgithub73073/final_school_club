package com.op.eschool.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public   void putLong(String key , long value){
        editor.putLong(key, value) ;
        editor.commit() ;
    }
    public   Long getLong(String key){
        return  sharedPreferences.getLong(key , 0) ;
    }


    public void putStringList(String key, List<String> customList) {
        Gson gson = new Gson();
        String json = gson.toJson(customList);
        editor.putString(key, json);
        editor.commit() ;
    }

    public List<String> getStringList(String key) {
        String json = sharedPreferences.getString(key, null);
        if (json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, new TypeToken<List<String>>() {}.getType());
        }
        return new ArrayList<>() ;
    }


    public void putMap(String key, Map<String, String> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        editor.putString(key, json);
        editor.apply();
    }

    public Map<String, String> getMap(String key) {
        String json = sharedPreferences.getString(key, null);
        if (json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
        }
        return new HashMap<>();
    }
}
