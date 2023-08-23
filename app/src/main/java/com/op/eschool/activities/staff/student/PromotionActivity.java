package com.op.eschool.activities.staff.student;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Utility.fromJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.adapters.SpiClassAdapter;
import com.op.eschool.adapters.StudentAdapter;
import com.op.eschool.adapters.StudentSelectAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityPromotionBinding;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.models.student.StudentModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FLog;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromotionActivity extends BaseActivity {
    ActivityPromotionBinding binding ;
    ClassModel selectedClass ;
    ArrayList<StudentModel> studentList = new ArrayList<>() ;
    GlobalLoader globalLoader ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_promotion);
        globalLoader = new GlobalLoader(PromotionActivity.this) ;

        binding.back.setOnClickListener(v->{onBackPressed();});
        SchoolModel schoolModel = new Gson().fromJson(commonDB.getString("SCHOOL_DETAILS") , SchoolModel.class) ;
        //GetClassLstByClg(schoolModel.getCollageCode());
        manageClicks() ;
    }

    private void manageClicks() {
        binding.submit.setOnClickListener(v->{
            String StudentId ="" ;
            for (StudentModel model :studentList){
                if (model.Checked){
                    if (StudentId.equalsIgnoreCase("")){
                        StudentId = model.studentId ;
                    }else {
                        StudentId = StudentId+ ","+ model.studentId  ;
                    }
                }
            }

            Map<String , String> map = new HashMap<>() ;
            map.put("type" ,"UpStuClss") ;
            map.put("Unqid" , commonDB.getString("Unqid")) ;
            map.put("StudentId" ,""+StudentId) ;
            map.put("Class" ,""+ selectedClass.getClassId()) ;
            String json = new Gson().toJson(map) ;
            globalLoader.showLoader();
            webSocketManager.sendMessage(json , res->{

                runOnUiThread(()->{
                    try {
                        globalLoader.dismissLoader();
                        CommonResponse commonResponse =Utility.convertResponse(res) ;
                        if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS , PromotionActivity.this , "Successfully Promote Students"    ,()->{
                                studentList.clear() ;
                                setStudentADapter() ;
                            }) ;
                        }else {
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , PromotionActivity.this , ""+commonResponse.Msg ,()->{
                            }) ;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , PromotionActivity.this , ""+e.getMessage() ,()->{
                        }) ;
                    }
                });
            });
        });
        binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                selectAll(b) ;
            }
        });
    }
    private void setClassesAdapter(List<ClassModel> list) {
        SpiClassAdapter classAdapter = new SpiClassAdapter(PromotionActivity.this  ,R.layout.operater_spinner_adapter_item  , list) ;
        binding.spiClass.setAdapter(classAdapter);

        binding.etClass.setOnClickListener(v->{
            binding.spiClass.performClick();
        });
        binding.spiClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.etClass.setText(list.get(position).getName());
                selectedClass = list.get(position) ;
                getStudentList() ;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getStudentList() {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"StdntTbl") ;
        map.put("Unqid" , commonDB.getString("Unqid")) ;
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            globalLoader.dismissLoader();
            studentList.clear() ;
            studentList.addAll((ArrayList<StudentModel>) fromJson(res,
                    new TypeToken<ArrayList<StudentModel>>() {
                    }.getType())) ;
            setStudentADapter() ;
        });
    }
    private void setStudentADapter() {
        binding.setStudentSelectAdapter(new StudentSelectAdapter(getApplicationContext() , studentList , (p,type)->{
        }));
    }
    void selectAll(boolean flag){
        for (StudentModel model :studentList){
            model.Checked = flag ;
        }
        binding.getStudentSelectAdapter().notifyDataSetChanged() ;
    }
}