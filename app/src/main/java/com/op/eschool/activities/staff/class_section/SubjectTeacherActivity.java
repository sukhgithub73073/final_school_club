package com.op.eschool.activities.staff.class_section;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Utility.fromJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.activities.staff.teacher.SignUpStaffActivity;
//import com.op.eschool.activities.staff.teacher.StaffRegisterActivity;
import com.op.eschool.activities.staff.teacher.TeacherListActivity;
import com.op.eschool.activities.staff.teacher.TeacherProfileActivity;
import com.op.eschool.adapters.TeacherAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivitySubjectTeacherBinding;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.staff.StaffModel;
import com.op.eschool.models.student.StudentFilterModel;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FLog;
import com.op.eschool.util.FToast;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubjectTeacherActivity extends BaseActivity {
    List<StaffModel> list = new ArrayList<>() ;
    ArrayList<StaffModel> allList = new ArrayList<>() ;    ActivitySubjectTeacherBinding binding ;
    GlobalLoader globalLoader ;

    String DB_STUDENT_KEY="" ;
    StudentFilterModel studentFilterModel ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_subject_teacher) ;
        globalLoader = new GlobalLoader(SubjectTeacherActivity.this) ;
        studentFilterModel = new StudentFilterModel("","","","","") ;
        binding.back.setOnClickListener(v->{onBackPressed();});
        DB_STUDENT_KEY = "DB_STAFF_KEY_" + commonDB.getString("Unqid") ;
        binding.addStaff.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , SignUpStaffActivity.class).putExtra("TYPE" ,"ADD_STAFF")) ;
        });
        binding.filter.setOnClickListener(v->{
            filterDialog() ;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        StaffTbl();
    }

    private void StaffTbl() {

        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"StaffTbl") ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            globalLoader.dismissLoader();
            list = (ArrayList<StaffModel>) fromJson(res,
                    new TypeToken<ArrayList<StaffModel>>() {
                    }.getType());
            setStaffADapter() ;
        });
    }



    private void setStaffADapter() {
        if (list.size()>0){
            binding.noData.setVisibility(View.GONE);
            binding.rvData.setVisibility(View.VISIBLE) ;
        }else{
            binding.noData.setVisibility(View.VISIBLE) ;
            binding.rvData.setVisibility(View.GONE) ;
        }
        binding.setTeacherAdapter(new TeacherAdapter(getApplicationContext() ,list , (p, type)->{
            if (type.equalsIgnoreCase("CALL")){
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", list.get(p).getMobileNumber(), null));
                startActivity(intent);
            } else if (type.equalsIgnoreCase("APPROVE")) {
                DialogModel dialogModel = new DialogModel(SubjectTeacherActivity.this ,"Approve","Want to sure to Approve this user ?","Yeah, sure","Cancel", t->{
                    if (t.equalsIgnoreCase("POSTIVE")){
                        UpStuTY(p ,list.get(p) , Constants.USER_APPROVE) ;
                    }
                } ) ;
                Utility.wantTOSureDialog(dialogModel) ;
            }else if (type.equalsIgnoreCase("DELETE")) {
                DialogModel dialogModel = new DialogModel(SubjectTeacherActivity.this ,"Delete","Want to sure to Delete this user ?","Yeah, sure","Cancel", t->{
                    if (t.equalsIgnoreCase("POSTIVE")){
                        UpStuTY(p,list.get(p) , Constants.USER_DELETE) ;
                    }
                } ) ;
                Utility.wantTOSureDialog(dialogModel) ;
            }else if (type.equalsIgnoreCase("VIEW")) {
                commonDB.putString("STAFF_DETAIL" , new Gson().toJson(list.get(p))) ;
                startActivity(new Intent(getApplicationContext() , TeacherProfileActivity.class)) ;

            }
        })); ;
    }

    private void filterDialog() {
        Dialog dialog = Utility.openAnyDialog(R.layout.staff_filter , SubjectTeacherActivity.this) ;
        dialog.show() ;
        TextView txt_status = dialog.findViewById(R.id.txt_status) ;

        txt_status.setText("" + studentFilterModel.status) ;


        txt_status.setOnClickListener(v->{
            statusSelection(txt_status) ;
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(v->{
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btn_apply).setOnClickListener(v->{

            if(txt_status.getText().toString().equalsIgnoreCase("")){
                FToast.makeText(getApplication(), "Please Select a Status", FToast.LENGTH_SHORT).show();
            }else{
                dialog.dismiss();
                studentFilterModel = new StudentFilterModel("" ,
                        "","",
                        "" ,""+txt_status.getText().toString()) ;
                filterApply() ;
            }
        });

    }
    private void statusSelection(TextView txtView) {
        SearchableSpinner caste = new SearchableSpinner(this);
        caste.setWindowTitle("Select Status") ;
        ArrayList<String> strList = new ArrayList<>() ;
        strList.add("ALL") ;
        strList.add("PENDING") ;
        strList.add("ACTIVE") ;
        strList.add("DELETE") ;

        caste.setSpinnerListItems(strList);
        caste.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                txtView.setText(selectedString) ;
            }
        });
        caste.setHighlightSelectedItem(true);
        caste.show();
    }

    public void filterApply(){
        list.clear();
        String sts = studentFilterModel.status ;
        if (sts.equalsIgnoreCase("ALL")){
            sts = "" ;

        }
        String finalSts = sts;
        list = allList.stream()
                .filter(studentModel -> finalSts.equalsIgnoreCase("") || studentModel.getActionStatus().equalsIgnoreCase(finalSts))
                .collect(Collectors.toList());
        FLog.w("Dfg" ,"DFg>>>" + new Gson().toJson(list.size())) ;

        setStaffADapter();




    }
    private void UpStuTY(int pos ,StaffModel model, String UpDtType) {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" , "UpStaffTY") ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        map.put("StaffId" , ""+model.getStaffId()) ;
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
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS , SubjectTeacherActivity.this , model.getFullName() + " has been successfully " + msg  ,()->{
                            model.setActionStatus(UpDtType.equalsIgnoreCase(Constants.USER_APPROVE)?"APPROVED":"DELETE");
                            if (UpDtType.equalsIgnoreCase(Constants.USER_DELETE)){
                                // list.remove(pos);
                            }
                            binding.getTeacherAdapter().notifyDataSetChanged() ;
                        }) ;
                    }else {
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , SubjectTeacherActivity.this , ""+commonResponse.Msg ,()->{
                        }) ;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , SubjectTeacherActivity.this , ""+e.getMessage() ,()->{
                    }) ;
                }
            });
        });
    }
}