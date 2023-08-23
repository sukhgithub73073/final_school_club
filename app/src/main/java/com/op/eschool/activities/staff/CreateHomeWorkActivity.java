package com.op.eschool.activities.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.op.eschool.R;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityCreateHomeWorkBinding;

public class CreateHomeWorkActivity extends BaseActivity {
    ActivityCreateHomeWorkBinding binding ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_create_home_work);

        binding.etClass.setOnClickListener(v->{
            binding.spiClass.performClick();
        });
        binding.spiClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity= (String) parent.getAdapter().getItem(position);
                binding.etClass.setText(selectedCity);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}