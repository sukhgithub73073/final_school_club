package com.op.eschool.services;
import static com.op.eschool.base.BaseActivity.commonDB;
import static com.op.eschool.base.MyApplication.staffRegisterList;
import static com.op.eschool.base.MyApplication.studentRegisterList;
import static com.op.eschool.util.Constants.DB_STUDENT_OFFINE_LIST;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.op.eschool.util.CommonDB;
import com.op.eschool.util.FLog;
import com.op.eschool.util.websockets.WebSocketManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentRegisterService extends Service {
//    List<String> studentRegisterList ;
//     CommonDB commonDB ;
    @Override
    public void onCreate() {
        super.onCreate();
        FLog.w("StudentRegisterService" ,"onCreate");

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        try {
//            commonDB = new CommonDB(getApplicationContext()) ;

            FLog.w("StudentRegisterService" ,"onStartCommand>>>>" + studentRegisterList.size());
//            studentRegisterList = commonDB.getStringList(DB_STUDENT_OFFINE_LIST) ;
            performAction() ;
        }catch (Exception e){e.printStackTrace();}

        return START_STICKY;
    }

    private void performAction() {
        try {
            if (studentRegisterList.size() > 0 ){
                registerApi() ;
            }else {
//                commonDB.putStringList(DB_STUDENT_OFFINE_LIST ,new ArrayList<>()) ;
                stopSelf();
            }
        }catch (Exception e){e.printStackTrace();}

    }

    private void registerApi() {
        try {
            Map<String,String> map = studentRegisterList.get(0) ;
            WebSocketManager webSocketManager = new WebSocketManager();
            webSocketManager.startWebSocket();
            webSocketManager.sendMessage(map ,res->{
                studentRegisterList.remove(0) ;
                performAction();
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        FLog.w("StudentRegisterService" ,"onDestroy");
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        FLog.w("StudentRegisterService" ,"onBind");
        return null;
    }
}


