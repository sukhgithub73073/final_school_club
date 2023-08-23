package com.op.eschool.activities;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Utility.fromJson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.op.eschool.R;
import com.op.eschool.activities.staff.StaffMainActivity;
import com.op.eschool.activities.staff.principle.PrincipleMainActivity;
import com.op.eschool.activities.staff.register.SchoolInfoActivity;
import com.op.eschool.activities.staff.student.StudentListActivity;
//import com.op.eschool.activities.staff.student.StudentRegisterActivity;
//import com.op.eschool.activities.staff.teacher.StaffRegisterActivity;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivitySplashBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.RegisterModel;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.login_model.LoginUserModel;
import com.op.eschool.models.school_models.GetSchoolModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FLog;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class SplashActivity extends BaseActivity {
    ActivitySplashBinding binding ;
    LoginUserModel loginUserModel ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_splash);
        registerModel = new RegisterModel() ;
        commonDB.putString("STUDENT_DETAIL" , "")  ;
        checkUpdate();
    }


    void getFCMToken(){
        FLog.w("getFCMToken", "FCM ");
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<String> task) {
                if (!task.isSuccessful()) {
                    FLog.w("getFCMToken", "FCM registration token failed" + task.getException());
                    return;
                }
                String token = task.getResult();
                FLog.e("getFCMToken", ">>>>>>>>" + token);
                commonDB.putString("FCM_TOKEN" , token) ;
            }
        }) ;
    }

    void gotoNextActivity(){
        new Handler().postDelayed(()->{
            if (commonDB.getString(Constants.LOGIN_RESPONSE).equalsIgnoreCase("")){
                startActivity(new Intent(getApplicationContext() , LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)) ;
            }else {
                loginUserModel = Utility.getLoginUser(getApplicationContext()) ;
                FLog.w("CommonResponse" ,">>>" + new Gson().toJson(loginUserModel)) ;
                if (loginUserModel.type.equalsIgnoreCase("Principle")){
                    startActivity(new Intent(getApplicationContext() , PrincipleMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)) ;
                }else  if (loginUserModel.type.equalsIgnoreCase("Staff")){
                    startActivity(new Intent(getApplicationContext() , StaffMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)) ;
                }else if (loginUserModel.type.equalsIgnoreCase("Student")){
                    startActivity(new Intent(getApplicationContext() , MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)) ;
                }else if (loginUserModel.type.equalsIgnoreCase("admin")){
                    startActivity(new Intent(getApplicationContext() , StaffMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)) ;
                }else{
                    startActivity(new Intent(getApplicationContext() , StaffMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)) ;
                }
            }
        } ,3000) ;
    }
    void AllInOneDt(CommonInterface commonInterface){
        commonInterface.onItemClicked(0) ;
//        Map<String , String> map = new HashMap<>() ;
//        map.put("type" , "AllInOneDt") ;
//        String json = new Gson().toJson(map) ;
//        webSocketManager.sendMessage(json , res->{
//            runOnUiThread(()->{
//                try {
//                    commonDB.putString("AllInOneDt" , res) ;
//                    commonInterface.onItemClicked(0) ;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }) ;
    }
    void GetClsAndGrpDt(CommonInterface commonInterface){
        Map<String , String> map = new HashMap<>();
        map.put("type" ,"GetClsAndGrpDt");
        map.put("Unqid" ,""+ loginUserModel.getCollageUnqid());
        String json = new Gson().toJson(map) ;
        webSocketManager.sendMessage(json , res->{
            runOnUiThread(()->{
                try {
                    commonDB.putString("GetClsAndGrpDt" , res) ;
                    commonInterface.onItemClicked(0) ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }) ;
    }
    void checkUpdate(){
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            try {
                FLog.w("checkForUpdates>>" , "updateAvailability>>>" +appUpdateInfo.updateAvailability() ) ;
                FLog.w("checkForUpdates>>" , "isUpdateTypeAllowed>>>" +appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE) ) ;
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, IMMEDIATE , SplashActivity.this ,10 ) ;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    initAllData() ;
                }

            }catch (Exception e){
                e.printStackTrace() ;
                initAllData() ;
            }
        });
        appUpdateInfoTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                try {
                    FLog.w("checkForUpdates>>" , "onFailure>>>" +e.getMessage()) ;
                    e.printStackTrace();
                    initAllData() ;
                }catch (Exception ev){
                    ev.printStackTrace();
                }
            }
        }) ;
    }
    void initAllData(){
        AllInOneDt(pos->{
            if (!commonDB.getString(Constants.LOGIN_RESPONSE).equalsIgnoreCase("")){
                loginUserModel = Utility.getLoginUser(getApplicationContext()) ;
                Glide.with(getApplicationContext())
                        .load(Utility.convertBase64ToBitmap(loginUserModel.getImage()))
                        .apply(new RequestOptions().placeholder(R.drawable.logo))
                        .into(binding.logo)   ;
                binding.schoolName.setText("WELCOME " + loginUserModel.getCollageName()) ;
                GetClsAndGrpDt(d->{
                    getFCMToken();
                    gotoNextActivity();
                }) ;
            }else{
                gotoNextActivity();
            }
        }) ;
    }
}