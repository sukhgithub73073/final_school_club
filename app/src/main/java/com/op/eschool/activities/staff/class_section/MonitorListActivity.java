package com.op.eschool.activities.staff.class_section;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Utility.fromJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.activities.staff.student.StudentListActivity;
import com.op.eschool.activities.staff.student.StudentProfileActivity;
import com.op.eschool.activities.timetable.ViewTimeTableActivity;
import com.op.eschool.adapters.StudentAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityMonitorListBinding;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.student.StudentModel;
import com.op.eschool.util.Constants;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitorListActivity extends BaseActivity {
    ActivityMonitorListBinding binding ;
    ClassGroupModel groupModel ;
    List<ClassModel> classList = new ArrayList<>() ;
    ClassModel classModel ;
    List<StudentModel> list  = new ArrayList<>();
    GlobalLoader globalLoader ;
    Map<String , String> map = new HashMap<>() ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_monitor_list);
        map.put("type" , "GetApontdStuMonitor") ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;

        globalLoader = new GlobalLoader(MonitorListActivity.this) ;
        binding.back.setOnClickListener(v->{onBackPressed();}) ;
        manageClicks();

    }
    private void manageClicks() {

        Utility.setGroupAdapter(MonitorListActivity.this , getApplicationContext() , binding.classGrp , model->{

            binding.txtClass.setText("");
            binding.noData.setVisibility(View.VISIBLE) ;
            binding.rvStudents.setVisibility(View.GONE) ;

            Utility.setClassesAdapter(MonitorListActivity.this , model.getClassList() , getApplicationContext() , binding.txtClass ,classModel->{
                map.put("Class" , classModel.getClassId()+"") ;
                GetApontdStuMonitor() ;
            });
        });
    }


    private void GetApontdStuMonitor() {
        String json = new Gson().toJson(map) ;
        try {
            globalLoader.showLoader();
        }catch (Exception e){
            e.printStackTrace();
        }
        webSocketManager.sendMessage(map , res->{
            try {
                runOnUiThread(()->{
                    globalLoader.dismissLoader();
                    list = (ArrayList<StudentModel>) fromJson(res,
                            new TypeToken<ArrayList<StudentModel>>() {
                            }.getType());
                    if (list.size()>0){
                        list.remove(0) ;
                    }
                    setStudentADapter() ;
                });
            } catch (Exception e) {
                e.printStackTrace() ;
            }
        });
    }
    private void setStudentADapter() {
        if (list.size()>0){
            binding.noData.setVisibility(View.GONE);
            binding.rvStudents.setVisibility(View.VISIBLE) ;
        }else{
            binding.noData.setVisibility(View.VISIBLE) ;
            binding.rvStudents.setVisibility(View.GONE) ;
        }
        binding.setStudentAdapter(new StudentAdapter(getApplicationContext() ,list , (p, type)->{
            if (type.equalsIgnoreCase("CALL")){
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", list.get(p).mobileNumber, null));
                startActivity(intent);
            } else if (type.equalsIgnoreCase("APPROVE")) {

            }else if (type.equalsIgnoreCase("DELETE")) {

            }else if (type.equalsIgnoreCase("VIEW")) {
                commonDB.putString("STUDENT_DETAIL" , new Gson().toJson(list.get(p))) ;
                startActivity(new Intent(getApplicationContext() , StudentProfileActivity.class)) ;
            }
        })) ;
    }
}