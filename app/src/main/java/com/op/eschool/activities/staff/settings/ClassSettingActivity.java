package com.op.eschool.activities.staff.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.op.eschool.R;
import com.op.eschool.activities.staff.class_section.ClassGroupListActivity;
import com.op.eschool.activities.staff.class_section.ClassListActivity;
import com.op.eschool.activities.staff.settings.class_setting.ClassByGrpActivity;
import com.op.eschool.activities.staff.settings.class_setting.SubjectByGrpActivity;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityClassSettingBinding;

public class ClassSettingActivity extends BaseActivity {
    ActivityClassSettingBinding binding ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_class_setting) ;
        binding.back.setOnClickListener(v->{onBackPressed();});

        binding.linGroup.setOnClickListener(v->{startActivity(new Intent(getApplicationContext() , ClassGroupListActivity.class));});
        binding.linClass.setOnClickListener(v->{ startActivity(new Intent(getApplicationContext() , ClassByGrpActivity.class).putExtra("TYPE" , "GetClsWisGrpDt"));});
        binding.linSubject.setOnClickListener(v->{startActivity(new Intent(getApplicationContext() , SubjectByGrpActivity.class));});

    }
}