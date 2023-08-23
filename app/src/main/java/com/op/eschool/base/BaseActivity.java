package com.op.eschool.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.op.eschool.databinding.FragmentCollegeOneBinding;
import com.op.eschool.databinding.FragmentCollegeTwoBinding;
import com.op.eschool.databinding.FragmentStaffOneBinding;
import com.op.eschool.databinding.FragmentStaffThreeBinding;
import com.op.eschool.databinding.FragmentStaffTwoBinding;
import com.op.eschool.databinding.FragmentStudentOneBinding;
import com.op.eschool.databinding.FragmentStudentThreeBinding;
import com.op.eschool.databinding.FragmentStudentTwoBinding;
import com.op.eschool.models.RegisterModel;
import com.op.eschool.models.login_model.LoginUserModel;
import com.op.eschool.reciever.NetworkChangeReceiver;
import com.op.eschool.util.CommonDB;
import com.op.eschool.util.FLog;
import com.op.eschool.util.Utility;
import com.op.eschool.util.websockets.WebSocketManager;

import org.jetbrains.annotations.Nullable;

public class BaseActivity  extends AppCompatActivity {
    public static CommonDB commonDB ;
    public static RegisterModel registerModel ;
   // public static GlobalLoader globalLoader ;
   public  static Activity activity ;

    public  static WebSocketManager webSocketManager;
//    public  static SchlLoginSocket schlLoginSocket;
    public static FragmentCollegeOneBinding bindingOne ;
    public static FragmentCollegeTwoBinding bindingTwo ;
    public static FragmentStaffOneBinding bindingStaffOne ;
    public static FragmentStaffTwoBinding bindingStaffTwo ;
    public static FragmentStaffThreeBinding bindingStaffThree ;
    public static FragmentStudentOneBinding bindingStudentOne ;
    public static FragmentStudentTwoBinding bindingStudentTwo ;
    public static FragmentStudentThreeBinding bindingStudentThree ;
    public static LoginUserModel loginUserModel ;

    private BroadcastReceiver broadcastReceiver;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FLog.w("BaseActivity" , "onCreate") ;
        activity = this ;
        loginUserModel = Utility.getLoginUser(getApplicationContext()) ;
        broadcastReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(broadcastReceiver, intentFilter);
        commonDB = new CommonDB(getApplicationContext()) ;

        // connectWebSocket() ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        FLog.w("BaseActivity" , "onResume") ;
        connectWebSocket();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FLog.w("BaseActivity" , "onDestroy") ;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }
    public  static void  connectWebSocket(){
        webSocketManager = new WebSocketManager();
        webSocketManager.startWebSocket();
      }
}