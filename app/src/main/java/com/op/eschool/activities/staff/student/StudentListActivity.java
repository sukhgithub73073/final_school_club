package com.op.eschool.activities.staff.student;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Utility.fromJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.activities.staff.StaffMainActivity;
import com.op.eschool.activities.staff.register.CollageRegisterActivity;
import com.op.eschool.activities.staff.settings.class_setting.ClassByGrpActivity;
import com.op.eschool.adapters.ParentNexusAdapter;
import com.op.eschool.adapters.ParentsAdapter;
import com.op.eschool.adapters.StudentAdapter;
import com.op.eschool.adapters.StudentNexusAdapter;
import com.op.eschool.adapters.TeacherAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityParentsListBinding;
import com.op.eschool.databinding.ActivityStudentListBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.bankNames.Datum;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.models.student.StudentFilterModel;
import com.op.eschool.models.student.StudentModel;
import com.op.eschool.retrofit.RetrofitClient;
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
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentListActivity extends BaseActivity {

    List<StudentModel> list  = new ArrayList<>();
    ArrayList<StudentModel> allList  = new ArrayList<>();
    ActivityStudentListBinding binding ;
    String DB_STUDENT_KEY="" ;
    SchoolModel schoolModel ;
    GlobalLoader globalLoader ;
    StudentFilterModel studentFilterModel ;
    Map<String , String> map = new HashMap<>() ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentListBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this) ;
        map.put("type" , "StdntTblByClss") ;
        map.put("Unqid" , commonDB.getString("Unqid")) ;

        //binding = DataBindingUtil.setContentView(this , R.layout.activity_student_list) ;
        globalLoader= new GlobalLoader(StudentListActivity.this) ;
        binding.back.setOnClickListener(v->{onBackPressed();});
        DB_STUDENT_KEY = "DB_STUDENT_KEY_" + commonDB.getString("Unqid") ;
        schoolModel = new Gson().fromJson(commonDB.getString("SCHOOL_DETAILS") , SchoolModel.class) ;
        studentFilterModel = new StudentFilterModel("","","","","") ;
        //StdntTblSocket("onCreate") ;
        manageClicks() ;
    }
    private void manageClicks() {
        Utility.setGroupAdapter(StudentListActivity.this , getApplicationContext() , binding.txtGroup , model->{
            studentFilterModel.group =model.getGroupName() ;
            binding.txtClass.setText("");
            binding.noData.setVisibility(View.VISIBLE) ;
            binding.rvStudents.setVisibility(View.GONE) ;

            Utility.setClassesAdapter(StudentListActivity.this , model.getClassList() , getApplicationContext() , binding.txtClass ,classModel->{
                studentFilterModel.className = classModel.getClassName() ;
                map.put("ClassId" , classModel.getClassId()+"") ;
                // filterApply() ;
                StdntTblByClss() ;
            });
        });
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                list = allList.stream()
                        .filter(studentModel -> studentModel._class.equalsIgnoreCase(studentFilterModel.className))
                        .filter(studentModel -> studentFilterModel.gender.equalsIgnoreCase("") || studentModel.gender.equalsIgnoreCase(studentFilterModel.gender))
                        .filter(studentModel -> charSequence.toString().equalsIgnoreCase("") || studentModel.fullName.contains(charSequence.toString().toUpperCase()))
                        .filter(studentModel -> studentFilterModel.caste.equalsIgnoreCase("") || studentModel.casteData.equalsIgnoreCase(studentFilterModel.caste))
                        .collect(Collectors.toList());


                setStudentADapter();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.addStudent.setOnClickListener(v->{
            commonDB.putString("STUDENT_DETAIL" ,"") ;
            startActivity(new Intent(getApplicationContext() , SignUpStudentActivity.class).putExtra("TYPE" ,"ADD_STUDENT")) ;
        });
    }

    private void StdntTblByClss() {
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            try {
                runOnUiThread(()->{
                    list.clear();
                    allList.clear();
                    globalLoader.dismissLoader();
                    list = (ArrayList<StudentModel>) fromJson(res,
                            new TypeToken<ArrayList<StudentModel>>() {
                            }.getType());
                    allList.addAll(list) ;
                    setStudentADapter();

                });
            } catch (Exception e) {
                e.printStackTrace() ;
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    private void StdntTblSocket(String type) {
//        Map<String , String> map = new HashMap<>() ;
//        map.put("type" ,"StdntTbl") ;
//        map.put("Unqid" , commonDB.getString("Unqid")) ;
//        String json = new Gson().toJson(map) ;
//        try {
//            globalLoader.showLoader();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        webSocketManager.sendMessage(json , res->{
//            try {
//                runOnUiThread(()->{
//
//                    globalLoader.dismissLoader();
//                    list = (ArrayList<StudentModel>) fromJson(res,
//                            new TypeToken<ArrayList<StudentModel>>() {
//                            }.getType());
//                    allList.addAll(list) ;
//                });
//            } catch (Exception e) {
//                e.printStackTrace() ;
//            }
//        });
//    }
    private void setStudentADapter() {
        if (list.size()>0){
            binding.noData.setVisibility(View.GONE);
            binding.rvStudents.setVisibility(View.VISIBLE) ;
        }else{
            binding.noData.setVisibility(View.VISIBLE) ;
            binding.rvStudents.setVisibility(View.GONE) ;
        }
        binding.setStudentAdapter(new StudentAdapter(getApplicationContext() ,list , (p,type)->{
            if (type.equalsIgnoreCase("CALL")){
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", list.get(p).mobileNumber, null));
                startActivity(intent);
            } else if (type.equalsIgnoreCase("APPROVE")) {
                DialogModel dialogModel = new DialogModel(StudentListActivity.this ,"Approve","Want to sure to Approve this user ?","Yeah, sure","Cancel", t->{
                    if (t.equalsIgnoreCase("POSTIVE")){
                        UpStuTY(p ,list.get(p) , Constants.USER_APPROVE) ;
                    }}) ;
                Utility.wantTOSureDialog(dialogModel) ;
            }else if (type.equalsIgnoreCase("DELETE")) {
                DialogModel dialogModel = new DialogModel(StudentListActivity.this ,"Delete","Want to sure to Delete this user ?","Yeah, sure","Cancel", t->{
                    if (t.equalsIgnoreCase("POSTIVE")){
                        UpStuTY(p,list.get(p) , Constants.USER_DELETE) ;
                    }}) ;
                Utility.wantTOSureDialog(dialogModel) ;
            }else if (type.equalsIgnoreCase("VIEW")) {
                commonDB.putString("STUDENT_DETAIL" , new Gson().toJson(list.get(p))) ;
                startActivity(new Intent(getApplicationContext() , StudentProfileActivity.class)) ;
            }
        })) ;
    }
    public void filterApply(){
        list.clear();
        String sts = studentFilterModel.status ;
        if (sts.equalsIgnoreCase("ALL")){
            sts = "" ;
        }
        String finalSts = sts;
        list = allList.stream()
                .filter(studentModel -> studentModel._class.equalsIgnoreCase(studentFilterModel.className))
                .filter(studentModel -> studentFilterModel.gender.equalsIgnoreCase("") || studentModel.gender.equalsIgnoreCase(studentFilterModel.gender))
                .filter(studentModel -> finalSts.equalsIgnoreCase("") || studentModel.actionStatus.equalsIgnoreCase(finalSts))
                .filter(studentModel -> studentFilterModel.caste.equalsIgnoreCase("") || studentModel.casteData.equalsIgnoreCase(studentFilterModel.caste))
                .collect(Collectors.toList());
        FLog.w("Dfg" ,"DFg>>>" + new Gson().toJson(list.size())) ;
        setStudentADapter();
    }
    private void UpStuTY(int pos ,StudentModel model, String UpDtType) {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" , "UpStuTY") ;
        map.put("Unqid" , commonDB.getString("Unqid")) ;
        map.put("StudentId" , ""+model.studentId) ;
        map.put("UpDtType" , ""+UpDtType) ;
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            globalLoader.dismissLoader();
            runOnUiThread(()->{
                try {

                    CommonResponse commonResponse =Utility.convertResponse(res) ;
                    if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
                        String msg = UpDtType.equalsIgnoreCase(Constants.USER_APPROVE)?"Approved":"Deleted" ;
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS , StudentListActivity.this , model.fullName + " has been successfully " + msg  ,()->{
                            model.actionStatus =UpDtType.equalsIgnoreCase(Constants.USER_APPROVE)?"APPROVED":"DELETE" ;
                            if (UpDtType.equalsIgnoreCase(Constants.USER_DELETE)){
                                list.remove(pos);
                            }
                            binding.getStudentAdapter().notifyDataSetChanged() ;
                        }) ;
                    }else {
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , StudentListActivity.this , ""+commonResponse.Msg ,()->{
                        }) ;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , StudentListActivity.this , ""+e.getMessage() ,()->{
                    }) ;
                }
            });
        });
    }



}