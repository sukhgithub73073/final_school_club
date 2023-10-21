package com.op.eschool.activities.staff.leave;

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
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.adapters.LeaveAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityLeaveListBinding;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.leave_model.LeaveModel;
import com.op.eschool.util.GlobalLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LeaveListActivity extends BaseActivity {
    ActivityLeaveListBinding binding ;
    ArrayList<LeaveModel> list = new ArrayList<>() ;
    GlobalLoader globalLoader ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_leave_list);
        globalLoader = new GlobalLoader(LeaveListActivity.this) ;

        binding.back.setOnClickListener(v->{onBackPressed();});
        binding.addLeave.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , AddLeaveActivity.class)) ;
        });
        GetLeave() ;
    }

    private void GetLeave() {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" , "GetLeave");
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        String json = new Gson().toJson(map) ;
        webSocketManager.sendMessage(map , res->{
            try {
                list = (ArrayList<LeaveModel>) fromJson(res,
                        new TypeToken<ArrayList<LeaveModel>>() {
                        }.getType());
                binding.noData.setVisibility(list.size()>0? View.GONE:View.VISIBLE);
                binding.setLeaveAdapter(new LeaveAdapter(list ,  (pos,type)->{})) ;
            } catch (Exception e) {
                e.printStackTrace() ;
            }
        }) ;
    }
}