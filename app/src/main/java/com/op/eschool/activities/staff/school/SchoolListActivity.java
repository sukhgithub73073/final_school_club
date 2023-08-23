package com.op.eschool.activities.staff.school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.activities.staff.class_section.AddClassActivity;
import com.op.eschool.adapters.ClassNexusAdapter;
import com.op.eschool.adapters.SchoolNexusAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityClassListBinding;
import com.op.eschool.databinding.ActivitySchoolListBinding;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchoolListActivity extends BaseActivity {
    ActivitySchoolListBinding binding ;
    GlobalLoader globalLoader ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_school_list);
        globalLoader = new GlobalLoader(SchoolListActivity.this) ;

        binding.back.setOnClickListener(v->{onBackPressed();});
        binding.setSchoolNexusAdapter(new SchoolNexusAdapter(SchoolListActivity.this , getApplicationContext() ,new ArrayList<>())); ;
        binding.add.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , AddSchoolActivity.class));
        }) ;
        binding.filter.setOnClickListener(v->{
            filterDialog() ;
        });
        ClgList();
    }
    private void filterDialog() {
        Dialog dialog = Utility.openAnyDialog(R.layout.student_filter , SchoolListActivity.this) ;
        dialog.show() ;
    }
    private void ClgList() {
        Map<String , String> map = new HashMap<>() ;
        map.put("Unqid" , commonDB.getString("Unqid")) ;
        globalLoader.showLoader();
    }
}