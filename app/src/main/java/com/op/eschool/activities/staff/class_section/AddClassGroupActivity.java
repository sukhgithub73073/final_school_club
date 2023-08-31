package com.op.eschool.activities.staff.class_section;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Utility.fromJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
//import com.op.eschool.activities.staff.student.StudentRegisterActivity;
//import com.op.eschool.adapters.ClassNexusAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityAddClassGroupBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.login_model.LoginUserModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FToast;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddClassGroupActivity extends BaseActivity {
    ActivityAddClassGroupBinding binding ;
    ClassGroupModel classGroupModel ;
    GlobalLoader globalLoader ;
    LoginUserModel loginUserModel ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_add_class_group) ;
        globalLoader = new GlobalLoader(AddClassGroupActivity.this) ;
        loginUserModel = Utility.getLoginUser(getApplicationContext()) ;
        binding.back.setOnClickListener(v->{onBackPressed();});
        if (!getIntent().getStringExtra("CLASS_GROUP_MODEL").equalsIgnoreCase("")){
            classGroupModel = new Gson().fromJson(getIntent().getStringExtra("CLASS_GROUP_MODEL") , ClassGroupModel.class) ;
            binding.etName.setText(""+ classGroupModel.getGroupName()) ;
            binding.submit.setText("Update") ;
        }

        binding.submit.setOnClickListener(view -> {
            if (binding.etName.getText().toString().equalsIgnoreCase("")){
                binding.etName.setError("Invalid Class Group Name");
            }else{
                ClassGrpAdd() ;
            }
        });
    }

    private void ClassGrpAdd() {
        Map<String , String> map = new HashMap<>() ;
        map.put("Unqid" ,loginUserModel.getCollageUnqid()) ;
        map.put("type","ClassGrpAdd");
        map.put("SubjectCodeAllow","1");
        map.put("GroupName" , binding.etName.getText().toString().toUpperCase()) ;
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
          runOnUiThread(()->{
              try {
                  globalLoader.dismissLoader();
                  CommonResponse commonResponse = Utility.convertResponse(res) ;
                  if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
                      Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS , AddClassGroupActivity.this , "Class Group successfully Added" ,()->{
                          globalLoader.showLoader();
                          GetClsAndGrpDt( pos->{
                              Utility.gotoHome(getApplicationContext()) ;
                        }) ;
                      }) ;
                  }else {
                      Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , AddClassGroupActivity.this , ""+commonResponse.getMsg() ,()->{}) ;
                  }
              }catch (Exception e){e.printStackTrace();}
          });
        });
    }

    void GetClsAndGrpDt(CommonInterface commonInterface){
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"GetClsAndGrpDt") ;
        map.put("Unqid" ,loginUserModel.getCollageUnqid()) ;
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            runOnUiThread(()->{
                try {
                    globalLoader.dismissLoader();
                    commonDB.putString("GetClsAndGrpDt" , res) ;
                    commonInterface.onItemClicked(0) ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }) ;
    }

}