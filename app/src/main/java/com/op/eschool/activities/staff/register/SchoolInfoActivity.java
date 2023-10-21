package com.op.eschool.activities.staff.register;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.op.eschool.R;
import com.op.eschool.activities.LoginActivity;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.activities.staff.StaffMainActivity;
import com.op.eschool.activities.staff.student.SignUpStudentActivity;
//import com.op.eschool.activities.staff.student.StudentRegisterActivity;
import com.op.eschool.activities.staff.teacher.SignUpStaffActivity;
//import com.op.eschool.activities.staff.teacher.StaffRegisterActivity;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivitySchoolInfoBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.school_models.GetSchoolModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FToast;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import java.util.HashMap;
import java.util.Map;

public class SchoolInfoActivity extends BaseActivity {
    ActivitySchoolInfoBinding binding ;
    String RESPONSE ="" ;
    GlobalLoader globalLoader ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_school_info) ;
        globalLoader = new GlobalLoader(SchoolInfoActivity.this) ;

        binding.back.setOnClickListener(v->{onBackPressed();});

        binding.btnNext.setOnClickListener(v->{
            if (!Utility.isNetworkConnectedMainThred(getApplicationContext())){
                FToast.makeText(getApplicationContext(), "No Internet Connection.", FToast.LENGTH_SHORT).show();
            }else if (binding.etCode.getText().toString().equalsIgnoreCase("")){
                FToast.makeText(getApplicationContext(), "Please enter valid School Code", FToast.LENGTH_SHORT).show();
            }else{
                ChkSchlCode() ;
            }
        });
    }
    private void ChkSchlCode() {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"ChkSchlCode") ;
        map.put("SchoolCode" , binding.etCode.getText().toString().toUpperCase()) ;
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(map , res->{
            RESPONSE = res ;
            runOnUiThread(()->{
                try {
                    RESPONSE = RESPONSE.replace("'" ,"\"") ;
                    commonDB.putString("ChkSchlCode" , RESPONSE) ;
                    GetSchoolModel  getSchoolModel =   Utility.ChkSchlCode(res) ;
                    if (getSchoolModel.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
                        GetCollageDetail(getSchoolModel.getCollageId() , psos->{
                            GetClsAndGrpDt(getSchoolModel.getUnqid() , pos->{
                                globalLoader.dismissLoader();
                                String msg ="You are type of school_name" ;
                                if (binding.etCode.getText().toString().toLowerCase().contains("s")){
                                    msg =msg.replace("type","Staff") ;
                                }else{
                                    msg =msg.replace("type","Student") ;
                                }
                                msg =msg.replace("school_name",""+getSchoolModel.getCollageName() ) ;
                                Utility.showAnimatedDialogButton("OK" ,ANIMATED_DAILOG_TYPE_SUCESS , SchoolInfoActivity.this , "" + msg ,()->{
                                    finish() ;
                                    String code =binding.etCode.getText().toString().substring(0,1) ;
                                    if (code.toLowerCase().equalsIgnoreCase("s")){
                                        startActivity(new Intent(getApplicationContext() , SignUpStaffActivity.class).putExtra("TYPE" ,"REGISTER_STAFF")) ;
                                    }else{
                                        startActivity(new Intent(getApplicationContext() , SignUpStudentActivity.class).putExtra("TYPE" ,"REGISTER_STUDENT")) ;
                                    }
                                }) ;
                            }) ;
                        });
                    }else{
                        globalLoader.dismissLoader();
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , SchoolInfoActivity.this , "" + getSchoolModel.getMsg() ,()->{
                        }) ;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        });
    }

    void GetCollageDetail(String CollageId , CommonInterface commonInterface){
        Map<String , String> map = new HashMap<>() ;
        map.put("type" , "GetCollageDetail") ;
        map.put("CollageId" ,CollageId) ;
        String json = new Gson().toJson(map) ;
        webSocketManager.sendMessage(map , res->{
            runOnUiThread(()->{
                try {
                    commonDB.putString("GetCollageDetail" , res) ;
                    commonInterface.onItemClicked(0) ;
                } catch (Exception e) {
                    e.printStackTrace();

                }
            });


        }) ;
    }

    void GetClsAndGrpDt(String Unqid , CommonInterface commonInterface){
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"GetClsAndGrpDt") ;
        map.put("Unqid" ,""+ Unqid) ;
        String json = new Gson().toJson(map) ;
        webSocketManager.sendMessage(map , res->{
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

}