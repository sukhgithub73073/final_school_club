package com.op.eschool.activities.staff.school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.activities.staff.class_section.AddClassActivity;
import com.op.eschool.adapters.SchoolNexusAdapter;
import com.op.eschool.adapters.SpiClassGroupAdapter;
import com.op.eschool.adapters.SpiCollageAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityAddSessionsBinding;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.util.FToast;
import com.op.eschool.util.GlobalLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSessionsActivity extends BaseActivity {
    ActivityAddSessionsBinding binding ;
    SchoolModel schoolModel ;
    GlobalLoader globalLoader ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_add_sessions) ;
        globalLoader = new GlobalLoader(AddSessionsActivity.this) ;

        binding.back.setOnClickListener(v->{onBackPressed();});
        ClgList() ;
        binding.submit.setOnClickListener(v->{
           if (binding.SessionName.getText().toString().equalsIgnoreCase("")){
                binding.SessionName.setError("Invalid Session Name");
            }else{
               AddSchlSession() ;
            }
        });

    }

    private void AddSchlSession() {



        String url = "AddSchlSession" ;
        Map<String , String> map = new HashMap<>() ;
        map.put("Unqid" , commonDB.getString("Unqid")) ;
        map.put("Session" ,"" + binding.SessionName.getText().toString()) ;
        map.put("SchoolId" ,schoolModel.getUid()) ;
        globalLoader.showLoader();
    }


    private void ClgList() {
        Map<String , String> map = new HashMap<>() ;
        map.put("Unqid" , commonDB.getString("Unqid")) ;
        globalLoader.showLoader();
    }

    private void setCollageAdapter(List<SchoolModel> list) {
        SpiCollageAdapter classAdapter = new SpiCollageAdapter(AddSessionsActivity.this  ,R.layout.operater_spinner_adapter_item  , list) ;
        binding.spiClg.setAdapter(classAdapter);
        binding.etClg.setOnClickListener(v->{
            binding.spiClg.performClick();
        });
        binding.spiClg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                schoolModel = list.get(position) ;
                binding.etClg.setText(schoolModel.getCollageName()) ;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}