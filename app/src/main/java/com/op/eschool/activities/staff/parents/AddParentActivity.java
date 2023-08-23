package com.op.eschool.activities.staff.parents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityAddParentBinding;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

public class AddParentActivity extends BaseActivity {
    ActivityAddParentBinding  binding ;
    GlobalLoader globalLoader ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_add_parent);
        globalLoader = new GlobalLoader(AddParentActivity.this) ;

        binding.back.setOnClickListener(v->{onBackPressed();});

        Utility.commonSpinnerClicks(binding.etFccupation , binding.spiFccupation);
        Utility.commonSpinnerClicks(binding.etBgroup , binding.spiBgroup);
        Utility.commonSpinnerClicks(binding.etRelign , binding.spiRelign);



    }
}