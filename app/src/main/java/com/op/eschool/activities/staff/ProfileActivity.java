package com.op.eschool.activities.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.op.eschool.R;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityProfileBinding;

public class ProfileActivity extends BaseActivity {

    ActivityProfileBinding binding ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_profile) ;


    }
}