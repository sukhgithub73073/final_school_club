package com.op.eschool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import com.op.eschool.R;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityForgotBinding;
import com.op.eschool.util.FToast;
import com.op.eschool.util.Utility;

public class ForgotActivity extends BaseActivity {
    ActivityForgotBinding binding ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView(this , R.layout.activity_forgot) ;
         binding.login.setOnClickListener(v->{
             onBackPressed();
         });
         binding.submit.setOnClickListener(v->{
             if (!Utility.isNetworkConnectedMainThred(getApplication())){
                 FToast.makeText(getApplication(), "No Internet Connection.", FToast.LENGTH_SHORT).show();
             }else if (binding.etUniqe.getText().toString().equalsIgnoreCase("")){
                 FToast.makeText(getApplicationContext(), "Please enter valid Unique ID", FToast.LENGTH_SHORT).show();
             }else{

             }
         });
    }
}