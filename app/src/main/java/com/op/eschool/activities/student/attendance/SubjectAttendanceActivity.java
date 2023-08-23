package com.op.eschool.activities.student.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import com.op.eschool.R;
import com.op.eschool.adapters.attendance.SubjectAttendanceAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivitySubjectAttendanceBinding;
import com.op.eschool.models.attendance.SubjectAttendanceModel;

import java.util.ArrayList;
import java.util.List;

public class SubjectAttendanceActivity extends BaseActivity {
    List<SubjectAttendanceModel> list = new ArrayList<>() ;

    ActivitySubjectAttendanceBinding binding ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_subject_attendance) ;
        binding.back.setOnClickListener(v->{onBackPressed();});
        list.add(new SubjectAttendanceModel("Math",9.0,8.0 ,10.0,80)) ;

        binding.setSubjectAttendanceAdapter(new SubjectAttendanceAdapter(list , getApplicationContext() , pos -> {

        }));

    }
}