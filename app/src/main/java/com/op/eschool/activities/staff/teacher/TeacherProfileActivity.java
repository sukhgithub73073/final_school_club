package com.op.eschool.activities.staff.teacher;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.activities.staff.student.StudentListActivity;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityTeacherProfileBinding;
import com.op.eschool.interfaces.CollegeInterface;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.staff.StaffModel;
import com.op.eschool.models.student.StudentModel;
import com.op.eschool.util.Constants;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherProfileActivity extends BaseActivity {
    StaffModel model ;
    ActivityTeacherProfileBinding binding ;
    GlobalLoader globalLoader ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile) ;
        binding = DataBindingUtil.setContentView(this , R.layout.activity_teacher_profile) ;
        globalLoader = new GlobalLoader(TeacherProfileActivity.this) ;
        initView() ;
        manageClicks() ;
    }
    private void designationSelection() {
        SearchableSpinner spinner = new SearchableSpinner(this);
        spinner.setWindowTitle("Select Designation") ;
        ArrayList<String> strList = new ArrayList<>() ;
        strList.add("Manager") ;
        strList.add("Principal") ;
        strList.add("Vice Principal") ;
        strList.add("Office Incharge") ;
        strList.add("Sub Office Incharge") ;
        strList.add("Exam Incharge") ;
        strList.add("Sport Incharge") ;
        strList.add("Class Teacher") ;
        strList.add("Peon") ;
        strList.add("Driver") ;
        strList.add("Sub Driver") ;
        spinner.setSpinnerListItems(strList);
        spinner.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                binding.txtDesignation.setText(selectedString) ;
                SetStaffDeg(selectedString) ;
            }
        });
        spinner.setHighlightSelectedItem(true);
        spinner.show();
    }



    private void manageClicks() {
        binding.editProfile.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , SignUpStaffActivity.class).putExtra("TYPE" ,"EDIT_STAFF")) ;
        });
        binding.txtDesignation.setOnClickListener(v->{
            designationSelection();
        });


        binding.back.setOnClickListener(v->{onBackPressed();});
        binding.ivCall.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", model.getMobileNumber(), null));
            startActivity(intent);
        });
        binding.ivDelele.setOnClickListener(v->{
            DialogModel dialogModel = new DialogModel(TeacherProfileActivity.this ,"Delete","Want to sure to Delete this user ?","Yeah, sure","Cancel", t->{
                if (t.equalsIgnoreCase("POSTIVE")){
                    UpStuTY(Constants.USER_DELETE) ;
                }
            } ) ;
            Utility.wantTOSureDialog(dialogModel) ;
        });
        binding.approve.setOnClickListener(v->{
            DialogModel dialogModel = new DialogModel(TeacherProfileActivity.this ,"Approve","Want to sure to Approve this user ?","Yeah, sure","Cancel", t->{
                if (t.equalsIgnoreCase("POSTIVE")){
                    UpStuTY(  Constants.USER_APPROVE) ;
                }
            } ) ;
            Utility.wantTOSureDialog(dialogModel) ;
        });

    }

    private void initView() {
        try {
            model = new Gson().fromJson(commonDB.getString("STAFF_DETAIL") ,StaffModel.class) ;
            binding.setModel(model) ;
            binding.cvApprove.setVisibility(model.getActionStatus().equalsIgnoreCase("PENDING")? View.VISIBLE:View.GONE) ;

            Glide.with(getApplicationContext())
                    .load((model.Image))
                    .apply(new RequestOptions().placeholder(R.drawable.students_placeholder))
                    .into(binding.ivAvatar)   ;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UpStuTY(String UpDtType) {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" , "UpStaffTY") ;
        map.put("Unqid" , commonDB.getString("Unqid")) ;
        map.put("StudentId" , ""+ model.getStaffId()) ;
        map.put("UpDtType" , ""+UpDtType) ;
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{

            runOnUiThread(()->{
                try {
                    globalLoader.dismissLoader();

                    CommonResponse commonResponse =Utility.convertResponse(res) ;
                    if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
                        String msg = UpDtType.equalsIgnoreCase(Constants.USER_APPROVE)?"Approved":"Deleted" ;
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS , TeacherProfileActivity.this , model.getFullName() + " has been successfully " + msg  ,()->{
                            if (UpDtType.equalsIgnoreCase(Constants.USER_APPROVE)){
                                binding.cvApprove.setVisibility(View.GONE) ;
                            }else{
                                finish();
                            }
                        }) ;
                    }else {
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , TeacherProfileActivity.this , ""+commonResponse.Msg ,()->{
                        }) ;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , TeacherProfileActivity.this , ""+e.getMessage() ,()->{
                    }) ;
                }
            });
        });
    }
    private void SetStaffDeg(String Designation ) {

        Map<String , String> map = new HashMap<>() ;
        map.put("type","SetStaffDeg") ;
        map.put("Unqid" , commonDB.getString("Unqid")) ;
        map.put("StaffName" , model.getFullName()) ;
        map.put("StaffId" , model.getStaffId()) ;
        map.put("Designation" , Designation.replace(" ","").trim()) ;
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            runOnUiThread(()->{
                try {
                    globalLoader.dismissLoader();
                    CommonResponse commonResponse = Utility.convertResponse(res) ;
                    if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS , TeacherProfileActivity.this , "Successfully Changed Staff Designation" ,()->{
                            //finish() ;
                        }) ;
                    }else {
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , TeacherProfileActivity.this , ""+commonResponse.getMsg() ,()->{
                        }) ;
                    }
                }catch (Exception e){e.printStackTrace();}
            });
        });

    }
}