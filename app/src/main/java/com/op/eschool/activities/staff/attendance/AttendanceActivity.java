package com.op.eschool.activities.staff.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.adapters.AttendanceAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityAttendanceBinding;
import com.op.eschool.util.GlobalLoader;

public class AttendanceActivity extends BaseActivity {

    ActivityAttendanceBinding binding ;
    GlobalLoader globalLoader ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_attendance) ;
        globalLoader = new GlobalLoader(AttendanceActivity.this) ;

        binding.setAttendanceAdapter(new AttendanceAdapter()) ;

    }
}