package com.op.eschool.activities;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_PENDING;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Constants.DB_SELECTED_GROUP_CLASS;
import static com.op.eschool.util.Utility.fromJson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.op.eschool.BuildConfig;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.activities.staff.StaffMainActivity;
import com.op.eschool.activities.staff.principle.PrincipleMainActivity;
import com.op.eschool.activities.staff.register.CollageRegisterActivity;
import com.op.eschool.activities.staff.register.SchoolInfoActivity;
import com.op.eschool.activities.staff.student.StudentProfileActivity;
//import com.op.eschool.activities.staff.student.StudentRegisterActivity;
//import com.op.eschool.activities.staff.teacher.StaffRegisterActivity;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.chatboat.ChatBoatActivity;
import com.op.eschool.databinding.ActivityLoginBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.LocationDetailModel;
import com.op.eschool.models.RegisterModel;
import com.op.eschool.models.login_model.LoginUserModel;
import com.op.eschool.models.school_models.GetSchoolModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.retrofit.WebSocketClient;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FLog;
import com.op.eschool.util.FToast;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.LocationTracker;
import com.op.eschool.util.PermissionUtil;
import com.op.eschool.util.Utility;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.WebSocket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding ;
    LocationDetailModel locationDetailModel ;
    boolean mockDialog = false ;
    GlobalLoader globalLoader ;
    LoginUserModel loginUserModel ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_login) ;
        globalLoader = new GlobalLoader(LoginActivity.this) ;
        registerModel = new RegisterModel();
        binding.chatBoat.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , ChatBoatActivity.class)) ;
        });
        binding.registerClz.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , ChatBoatActivity.class)) ;
        });
        String text = "<font color=#000000>If you have not an Account ? </font> <font color=#0C46C4>Register Here</font>";
        binding.register.setText(Html.fromHtml(text , Html.FROM_HTML_MODE_LEGACY));
        binding.txtForgot.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , ForgotActivity.class ));
        });
        binding.register.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , SchoolInfoActivity.class)) ;
        });
        if (PermissionUtil.checkPermisiion(getApplicationContext() , "android.permission.ACCESS_FINE_LOCATION")){
            initLocation();
        }else{
            PermissionUtil.requestPermission(LoginActivity.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , 100) ;
        }
        locationDetailModel = new LocationDetailModel(false , 0 , 0,0,false,0,0,0) ;

        // 190
        // 390

        // 200 *6 = 1200
        if (BuildConfig.DEBUG){
//          Govt Sen Sec School Boys
//          principle
            binding.etUniqe.setText("KS085693");
            binding.etPassword.setText("FP415178");
            //.8 width .9 height
////        Pink Perl University
            binding.etUniqe.setText("NP703304");
            binding.etPassword.setText("BT561300");
////        staff
//            binding.etUniqe.setText("CJ864532");
//            binding.etPassword.setText("QQ494440");
        }
        binding.login.setOnClickListener(v->{
//            if (locationDetailModel ==null){
//                if (PermissionUtil.checkPermisiion(getApplicationContext() , "android.permission.ACCESS_FINE_LOCATION")){
//                    initLocation();
//                }else{
//                    PermissionUtil.requestPermission(LoginActivity.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , 100) ;
//                }
//            }else
                if (!Utility.isNetworkConnectedMainThred(getApplication())){
                FToast.makeText(getApplication(), "No Internet Connection.", FToast.LENGTH_SHORT).show();
            }else if (binding.etUniqe.getText().toString().equalsIgnoreCase("")){
                FToast.makeText(getApplicationContext(), "Please enter valid Unique ID", FToast.LENGTH_SHORT).show();
            }else if (binding.etPassword.getText().toString().equalsIgnoreCase("")){
                FToast.makeText(getApplicationContext(), "Please enter valid Password", FToast.LENGTH_SHORT).show();
            }else {
                Schoologin() ;
            }
        });
    }
    private void Schoologin() {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" , "Schoologin") ;
        map.put("Unqid" , binding.etUniqe.getText().toString()) ;
        map.put("Pass" , binding.etPassword.getText().toString()) ;
        map.put("Accuracy" ,""+locationDetailModel.accuracy) ;
        map.put("Latitude " ,""+locationDetailModel.lat) ;
        map.put("Longitude " ,""+locationDetailModel.log) ;
        map.put("NetAccuracy" ,""+locationDetailModel.netAccuracy) ;
        map.put("NetLatitude " ,""+locationDetailModel.netLat) ;
        map.put("NetLongitude " ,""+locationDetailModel.netLog) ;
        map.put("FirebaseToken" ,"" + commonDB.getString("FCM_TOKEN")) ;
        map.put("IpAddress" ,"" + Utility.getLocalIpAddress()) ;
        FLog.w("Schoologin" , "map" + new Gson().toJson(map)) ;
        globalLoader.showLoader() ;
        String json = new Gson().toJson(map) ;
        webSocketManager.sendMessage(json , res->{
            runOnUiThread(()->{
                try {
                    loginUserModel = new Gson().fromJson(res , LoginUserModel.class) ;
                    if (loginUserModel.status.equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
                        commonDB.putString(Constants.LOGIN_RESPONSE , new Gson().toJson(loginUserModel)) ;
                        commonDB.putString("SELECT_ROLE" , "" +loginUserModel.type) ;
                        GetClsAndGrpDt( pos -> {
                            globalLoader.dismissLoader();
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
                        });
                    }else {
                        globalLoader.dismissLoader();
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , LoginActivity.this , ""+ loginUserModel.msg ,()->{
                        }) ;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , LoginActivity.this , "Exception>>>" + e.getMessage() ,()->{}) ;
                }
            });
        }) ;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocation();
            } else {
                FToast.makeText(getApplicationContext(),"Permission Denied",FToast.LENGTH_SHORT).show();
            }
        }
    }
    void initLocation() {
        LocationTracker.getInstance(this).connectToLocation(new LocationTracker.myListener() {
            @Override
            public void onUpdate(LocationDetailModel model) {
                if (model.isMock ||model.netIsMock && !mockDialog){
                    DialogModel dialogModel = new DialogModel(LoginActivity.this ,"Access Denied","You can't do use this app because you are using Mock Location","Exit","Cancel", type->{
                        mockDialog = false ;
                        finish() ;
                    } ) ;
                    Utility.wantTOSureDialog(dialogModel) ;
                    mockDialog = true ;
                }else{
                    mockDialog = false ;
                    locationDetailModel = model ;
                    commonDB.putString("LOCATION_MODEL" , new Gson().toJson(locationDetailModel)) ;
                }
            }
        });
    }

    void ClassGrpAdded(CommonInterface commonInterface){
        SchoolModel schoolModel = Utility.GetCollageDetail(commonDB.getString("GetCollageDetail"));
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"ClassGrpAdded") ;
        map.put("Unqid" ,""+ schoolModel.getUnqid()) ;
        String json = new Gson().toJson(map) ;
        webSocketManager.sendMessage(json , res->{
            runOnUiThread(()->{
                try {
                    commonDB.putString("ClassGrpAdded" , res) ;
                    commonInterface.onItemClicked(0) ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }) ;
    }

    void GetClsAndGrpDt(CommonInterface commonInterface){
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"GetClsAndGrpDt") ;
//        map.put("Unqid" ,""+ binding.etUniqe.getText().toString()) ;
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

    void loginWithRetrofit(){

        WebSocketClient.getWebSocketApi().connectWebSocket("").enqueue(new Callback<WebSocket>() {
            @Override
            public void onResponse(Call<WebSocket> call, Response<WebSocket> response) {
                if (response.isSuccessful()) {
                    WebSocket webSocket = response.body();
                    if (webSocket != null) {
                        webSocket.send("Hello, WebSocket!");
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<WebSocket> call, Throwable t) {
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        commonDB.putString(DB_SELECTED_GROUP_CLASS ,"") ;
    }
}