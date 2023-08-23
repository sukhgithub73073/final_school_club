package com.op.eschool.activities.staff.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.adapters.AttendanceAdapter;
import com.op.eschool.adapters.AttendanceMonthlyAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityAttendanceMonthly2Binding;
import com.op.eschool.util.GlobalLoader;

import java.util.ArrayList;
import java.util.List;

public class AttendanceMonthlyActivity extends BaseActivity {

    ActivityAttendanceMonthly2Binding binding ;
    GlobalLoader globalLoader ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_attendance_monthly2) ;
        globalLoader = new GlobalLoader(AttendanceMonthlyActivity.this) ;

        List<Integer> datesList =new ArrayList<>() ;
        datesList.add(1) ;
        datesList.add(2) ;
        datesList.add(3) ;
        datesList.add(4) ;
        datesList.add(5) ;
        datesList.add(6) ;
        datesList.add(7) ;
        datesList.add(8) ;
        datesList.add(9) ;
        datesList.add(10) ;
        datesList.add(11) ;
        datesList.add(12) ;
        datesList.add(13) ;
        datesList.add(14) ;
        datesList.add(15) ;
        datesList.add(16) ;
        datesList.add(17) ;
        datesList.add(18) ;
        datesList.add(19) ;
        datesList.add(20) ;
        datesList.add(21) ;
        datesList.add(22) ;
        datesList.add(23) ;
        datesList.add(24) ;
        datesList.add(25) ;
        datesList.add(26) ;
        datesList.add(27) ;
        datesList.add(28) ;
        datesList.add(29) ;
        datesList.add(30) ;
        binding.setAttendanceMonthlyAdapter(new AttendanceMonthlyAdapter(getApplicationContext() ,datesList )); ;

    }
}