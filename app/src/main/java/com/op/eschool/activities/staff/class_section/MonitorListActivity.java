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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_monitor_list);
        globalLoader = new GlobalLoader(MonitorListActivity.this) ;
        binding.back.setOnClickListener(v->{onBackPressed();}) ;
        ClassGrpAdded();

    }
    private void ClassGrpAdded() {

        try {
            String res = commonDB.getString("ClassGrpAdded");
            ArrayList<ClassGroupModel> list = (ArrayList<ClassGroupModel>) fromJson(res,
                    new TypeToken<ArrayList<ClassGroupModel>>() {
                    }.getType());
            setGroupAdapter(list) ;
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Map<String , String> map = new HashMap<>() ;
//        map.put("type" ,"ClassGrpAdded") ;
//        map.put("Unqid" , commonDB.getString("Unqid")) ;
//        globalLoader.showLoader();
//        String json = new Gson().toJson(map) ;
//        webSocketManager.sendMessage(json , res->{
//            runOnUiThread(()->{
//                globalLoader.dismissLoader();
//                try {
//                    ArrayList<ClassGroupModel> list = (ArrayList<ClassGroupModel>) fromJson(res,
//                            new TypeToken<ArrayList<ClassGroupModel>>() {
//                            }.getType());
//                    setGroupAdapter(list) ;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }) ;
    }
    private void setGroupAdapter(ArrayList<ClassGroupModel> groupList) {


        SearchableSpinner caste = new SearchableSpinner(this);
        caste.setWindowTitle("Select Class Group") ;
        ArrayList<String> strList = new ArrayList<>() ;
        Map<String , ClassGroupModel> classMap = new HashMap<>() ;
        for (ClassGroupModel s : groupList){
            strList.add(s.getGroupName()) ;
            classMap.put(""+s.getGroupName() ,  s) ;
        }
        caste.setSpinnerListItems(strList);
        caste.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                binding.classGrp.setText(selectedString) ;
                groupModel = classMap.get(selectedString) ;
                binding.txtClass.setText("");
                list.clear();
                setStudentADapter();
                GetClsWisGrpDt();
            }
        });
        binding.classGrp.setOnClickListener(v->{
            caste.setHighlightSelectedItem(true);
            caste.show();
        });
    }
    private void GetClsWisGrpDt() {
        Map<String , String> map = new HashMap<>() ;
        map.put("type","GetClsWisGrpDt") ;
        map.put("Unqid" , commonDB.getString("Unqid")) ;
        map.put("GroupId" ,"" + groupModel.getGroupId());
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            runOnUiThread(()->{
                try {
                    globalLoader.dismissLoader();
                    classList.clear();
                    classList =  Utility.convertClassList(res) ;
                    setClassSpinnerAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , MonitorListActivity.this , ""+e.getMessage() ,()->{
                    }) ;
                }
            });
        });
    }
    private void setClassSpinnerAdapter() {

        SearchableSpinner caste = new SearchableSpinner(this);
        caste.setWindowTitle("Select Class") ;
        ArrayList<String> strList = new ArrayList<>() ;
        Map<String , ClassModel> classMap = new HashMap<>() ;
        for (ClassModel s : classList){
            strList.add(s.getClassName()) ;
            classMap.put(""+s.getClassName() ,  s) ;
        }
        caste.setSpinnerListItems(strList);
        caste.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                binding.txtClass.setText(selectedString) ;
                classModel = classList.get(position) ;
                GetStudentMonitor() ;
            }
        });
        binding.txtClass.setOnClickListener(v->{
            caste.setHighlightSelectedItem(true);
            caste.show();
        });
    }

    private void GetStudentMonitor() {
        //GetApontdStuMonitor
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"GetApontdStuMonitor") ;
        map.put("Unqid" , commonDB.getString("Unqid")) ;
        map.put("Class" , classModel.getClassName()) ;
        String json = new Gson().toJson(map) ;
        try {
            globalLoader.showLoader();
        }catch (Exception e){
            e.printStackTrace();
        }
        webSocketManager.sendMessage(json , res->{
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