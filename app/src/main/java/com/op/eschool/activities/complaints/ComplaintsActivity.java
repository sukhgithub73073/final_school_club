package com.op.eschool.activities.complaints;

import static com.op.eschool.util.Utility.fromJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.op.eschool.R;
import com.op.eschool.adapters.ComplaintsAdapter;
import com.op.eschool.adapters.LeaveAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityComplaintsBinding;
import com.op.eschool.models.complaint_model.ComplaintsModel;
import com.op.eschool.models.leave_model.LeaveModel;
import com.op.eschool.models.student.StudentModel;
import com.op.eschool.util.GlobalLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplaintsActivity extends BaseActivity {
    ActivityComplaintsBinding binding ;
    List<ComplaintsModel> list = new ArrayList<>() ;
    GlobalLoader globalLoader ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_complaints) ;
        globalLoader = new GlobalLoader(ComplaintsActivity.this) ;
        binding.back.setOnClickListener(v->{onBackPressed();});
        binding.addBtn.setOnClickListener(v->{startActivity(new Intent(getApplicationContext() ,AddComplaintActivity.class ));});
        setComplaintsAdapter() ;
    }

    private void setComplaintsAdapter() {
        binding.setComplaintsAdapter(new ComplaintsAdapter(list , (p,t)->{

        }));

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetCmplnt();
    }

    void GetCmplnt(){
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"GetCmplnt") ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        String json = new Gson().toJson(map) ;

        webSocketManager.sendMessage(map , res->{
            runOnUiThread(()->{
                try {
                    list = (ArrayList<ComplaintsModel>) fromJson(res,
                            new TypeToken<ArrayList<ComplaintsModel>>() {
                            }.getType());
                    binding.noData.setVisibility(list.size()>0? View.GONE:View.VISIBLE);
                    binding.setComplaintsAdapter(new ComplaintsAdapter(list ,  (pos, type)->{})); ;
                } catch (Exception e) {
                    e.printStackTrace() ;
                }

            });
        });

    }


}