package com.op.eschool.activities.staff.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import com.op.eschool.R;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityFeesCollectionBinding;

public class FeesCollectionActivity extends BaseActivity {

    ActivityFeesCollectionBinding binding ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView(this , R.layout.activity_fees_collection) ;
         binding.back.setOnClickListener(v -> {onBackPressed();}) ;
    }
}