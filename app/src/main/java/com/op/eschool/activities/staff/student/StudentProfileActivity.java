package com.op.eschool.activities.staff.student;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Utility.fromJson;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityStudentProfileBinding;
import com.op.eschool.interfaces.CollegeInterface;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.student.StudentModel;
import com.op.eschool.util.Constants;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentProfileActivity extends BaseActivity {
    ActivityStudentProfileBinding binding ;
    StudentModel model ;
    String DB_STUDENT_KEY="" ;
    GlobalLoader globalLoader ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_student_profile) ;
        globalLoader = new GlobalLoader(StudentProfileActivity.this) ;

        binding.back.setOnClickListener(v->{onBackPressed();});
        DB_STUDENT_KEY = "DB_STUDENT_KEY_" + commonDB.getString("Unqid") ;
        try {
            model = new Gson().fromJson(commonDB.getString("STUDENT_DETAIL") ,StudentModel.class) ;
            binding.setModel(model) ;
            Glide.with(getApplicationContext())
                    .load((model.Image))
                    .apply(new RequestOptions().placeholder(R.drawable.students_placeholder))
                    .into(binding.ivAvatar)   ;
            binding.cvApprove.setVisibility(model.actionStatus.equalsIgnoreCase("PENDING")? View.VISIBLE:View.GONE) ;

        } catch (Exception e) {
          e.printStackTrace();
        }

        binding.editProfile.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , SignUpStudentActivity.class).putExtra("TYPE" ,"EDIT_STUDENT")) ;
        });

        binding.ivCall.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", model.mobileNumber, null));
            startActivity(intent);
        });
        binding.ivDelele.setOnClickListener(v->{
            DialogModel dialogModel = new DialogModel(StudentProfileActivity.this ,"Delete","Want to sure to Delete this user ?","Yeah, sure","Cancel", t->{
                if (t.equalsIgnoreCase("POSTIVE")){
                    UpStuTY(Constants.USER_DELETE) ;
                }
            } ) ;
            Utility.wantTOSureDialog(dialogModel) ;
        });
        binding.approve.setOnClickListener(v->{
            DialogModel dialogModel = new DialogModel(StudentProfileActivity.this ,"Approve","Want to sure to Approve this user ?","Yeah, sure","Cancel", t->{
                if (t.equalsIgnoreCase("POSTIVE")){
                    UpStuTY(  Constants.USER_APPROVE) ;
                }
            } ) ;
            Utility.wantTOSureDialog(dialogModel) ;
        });
    }
    private void UpStuTY(String UpDtType) {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" , "UpStuTY") ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        map.put("StudentId" , ""+ model.studentId) ;
        map.put("UpDtType" , ""+UpDtType) ;
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            StdntTblSocket(()->{
                runOnUiThread(()->{
                    try {
                        globalLoader.dismissLoader();
                        CommonResponse commonResponse =Utility.convertResponse(res) ;
                        if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
                            String msg = UpDtType.equalsIgnoreCase(Constants.USER_APPROVE)?"Approved":"Deleted" ;
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS , StudentProfileActivity.this , model.fullName + " has been successfully " + msg  ,()->{
                                if (UpDtType.equalsIgnoreCase(Constants.USER_APPROVE)){
                                    binding.cvApprove.setVisibility(View.GONE) ;
                                }else{
                                    finish();
                                }
                            }) ;
                        }else {
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , StudentProfileActivity.this , ""+commonResponse.Msg ,()->{
                            }) ;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , StudentProfileActivity.this , ""+e.getMessage() ,()->{
                        }) ;
                    }
                });
            }) ;

        });
    }
    private void StdntTblSocket(CollegeInterface collegeInterface) {
        collegeInterface.onRegisterClicked() ;
//        Map<String , String> map = new HashMap<>() ;
//        map.put("type" ,"StdntTbl") ;
//        map.put("Unqid" , loginUserModel.collageUnqid) ;
//        String json = new Gson().toJson(map) ;
//        webSocketManager.sendMessage(json , res->{
//            runOnUiThread(()->{
//
//                collegeInterface.onRegisterClicked() ;
//
//            });
//
//        });
    }
}