package com.op.eschool.activities.staff.class_section;

import static com.op.eschool.util.Utility.fromJson;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.op.eschool.R;
import com.op.eschool.activities.staff.teacher.TeacherProfileActivity;
import com.op.eschool.adapters.ClassTeacherAdapter;
import com.op.eschool.adapters.TeacherAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityClassTeacherListBinding;
import com.op.eschool.models.staff.StaffModel;
import com.op.eschool.models.staff.TeacherModel;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassTeacherListActivity extends BaseActivity {
    ActivityClassTeacherListBinding binding ;
    List<TeacherModel> list = new ArrayList<>() ;
    ArrayList<TeacherModel> allList = new ArrayList<>() ;

    GlobalLoader globalLoader ;

    Map<String , String> map = new HashMap<>() ;
    String TYPE ="" ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_class_teacher_list) ;
        globalLoader = new GlobalLoader(ClassTeacherListActivity.this) ;
        TYPE = getIntent().getStringExtra("type") ;
        map.put("type" ,"" + TYPE) ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        binding.back.setOnClickListener(v->{onBackPressed();});
        manageClicks() ;
    }

    private void manageClicks() {

        Utility.setGroupAdapter(ClassTeacherListActivity.this , getApplicationContext() , binding.txtGroup , model->{
            binding.txtClass.setText("");
            binding.noData.setVisibility(View.VISIBLE) ;
            binding.rvData.setVisibility(View.GONE) ;

            Utility.setClassesAdapter(ClassTeacherListActivity.this , model.getClassList() , getApplicationContext() , binding.txtClass ,classModel->{
                map.put("Class" , classModel.getClassId()+"") ;
                GetSubTechrDtAll() ;
            });
        });
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                list = allList.stream()
                        .filter(studentModel -> charSequence.toString().equalsIgnoreCase("") || studentModel.getTeacher().contains(charSequence.toString().toUpperCase()))
                        .collect(Collectors.toList());
                setStaffADapter();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    private void GetSubTechrDtAll() {
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(map , res->{
            try {
                runOnUiThread(() -> {
                    globalLoader.dismissLoader();
                    list = (ArrayList<TeacherModel>) fromJson(res,
                            new TypeToken<ArrayList<TeacherModel>>() {
                            }.getType());
                    allList.addAll(list);
                    setStaffADapter();
                });
            }catch (Exception e){e.printStackTrace();}

        });
    }
     void setStaffADapter() {
        if (list.size()>0){
            binding.noData.setVisibility(View.GONE);
            binding.rvData.setVisibility(View.VISIBLE) ;
        }else{
            binding.noData.setVisibility(View.VISIBLE) ;
            binding.rvData.setVisibility(View.GONE) ;
        }




        binding.setClassTeacherAdapter(new ClassTeacherAdapter(getApplicationContext() ,list , (p, type)->{
            if (type.equalsIgnoreCase("CALL")){
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", list.get(p).getMobileNumber(), null));
                startActivity(intent);
            }
        }));
    }




}