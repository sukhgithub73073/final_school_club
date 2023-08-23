package com.op.eschool.activities.staff.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.activities.staff.class_section.AddClassActivity;
import com.op.eschool.adapters.SpiClassGroupAdapter;
import com.op.eschool.adapters.SpiSessionsAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityChangeSessionBinding;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.class_models.SessionModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.util.GlobalLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeSessionActivity extends BaseActivity {
    SessionModel sessionModel ;
    ActivityChangeSessionBinding binding ;
    GlobalLoader globalLoader ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_change_session) ;
        globalLoader = new GlobalLoader(ChangeSessionActivity.this) ;


        binding.back.setOnClickListener(v->{onBackPressed();});
        binding.submit.setOnClickListener(v->{
            if (sessionModel==null){
                binding.etSession.setError("Invalid Session") ;
            }else{
                ChangeSession();
            }
        });
    }
    private void ChangeSession() {
       //TODO add change session api here
    }

    private void setSessionAdapter(List<SessionModel> list) {

        SpiSessionsAdapter classAdapter = new SpiSessionsAdapter(ChangeSessionActivity.this  ,R.layout.operater_spinner_adapter_item  , list) ;
        binding.spiSession.setAdapter(classAdapter);
        binding.etSession.setOnClickListener(v->{
            binding.spiSession.performClick();
        });
        binding.spiSession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sessionModel = list.get(position) ;
                binding.etSession.setText(sessionModel.getSession());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}