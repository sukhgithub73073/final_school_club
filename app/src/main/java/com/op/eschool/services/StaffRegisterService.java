package com.op.eschool.services;
import static com.op.eschool.base.MyApplication.staffRegisterList;
import static com.op.eschool.util.Constants.DB_STAFF_OFFINE_LIST;
import static com.op.eschool.util.Constants.DB_STAFF_OFFINE_LIST;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.op.eschool.util.CommonDB;
import com.op.eschool.util.FLog;
import com.op.eschool.util.websockets.WebSocketManager;

import java.util.ArrayList;
import java.util.List;

public class StaffRegisterService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        FLog.w("StaffRegisterService" ,"onCreate");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        FLog.w("StaffRegisterService" ,"onStartCommand");

        performAction() ;
        return START_STICKY;
    }
    private void performAction() {
        if (staffRegisterList.size() > 0 ){
            registerApi() ;
        }else {
            stopSelf();
        }
    }

    private void registerApi() {
        String json = staffRegisterList.get(0) ;
        WebSocketManager webSocketManager = new WebSocketManager();
        webSocketManager.startWebSocket();
        webSocketManager.sendMessage(json ,res->{
            staffRegisterList.remove(0) ;
            performAction();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FLog.w("StaffRegisterService" ,"onDestroy");
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        FLog.w("StaffRegisterService" ,"onBind");
        return null;
    }
}


