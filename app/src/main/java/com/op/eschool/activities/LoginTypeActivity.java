package com.op.eschool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.op.eschool.R;
import com.op.eschool.activities.staff.StaffMainActivity;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityLoginTypeBinding;

public class LoginTypeActivity extends BaseActivity {

    ActivityLoginTypeBinding binding ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_login_type) ;
        binding.linStudent.setOnClickListener(v->{
           commonDB.putString("login_type" ,"student") ;
           startActivity(new Intent(getApplicationContext() , LoginActivity.class)) ;
        });
        binding.linTeacher.setOnClickListener(v->{
            commonDB.putString("login_type" ,"staff") ;
           startActivity(new Intent(getApplicationContext() , LoginActivity.class)) ;
        });

        binding.linPrinciple.setOnClickListener(v->{
            commonDB.putString("login_type" ,"collage") ;
            startActivity(new Intent(getApplicationContext() , LoginActivity.class)) ;
        });
    }
}