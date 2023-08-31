package com.op.eschool.activities.staff.parents;

import static com.op.eschool.util.Utility.fromJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.activities.staff.student.StudentListActivity;
import com.op.eschool.adapters.ParentNexusAdapter;
import com.op.eschool.adapters.ParentsAdapter;
import com.op.eschool.adapters.StudentNexusAdapter;
import com.op.eschool.adapters.TeacherAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityParentsListBinding;
import com.op.eschool.models.parents_model.ParentModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentsListActivity extends BaseActivity {

    ArrayList<ParentModel> list ;
    ActivityParentsListBinding binding ;
    GlobalLoader globalLoader ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_parents_list) ;
        globalLoader = new GlobalLoader(ParentsListActivity.this) ;

        binding.back.setOnClickListener(v->{onBackPressed();});

        binding.filter.setOnClickListener(v->{
            filterDialog() ;
        });
        PrntTblSocket();
    }

    private void PrntTblSocket() {

        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"PrntTbl") ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        String json = new Gson().toJson(map) ;

        if (list.size() == 0){
            globalLoader.showLoader();
        }
        webSocketManager.sendMessage(json , res->{
            if (list.size() == 0){
                globalLoader.dismissLoader();
            }

            ArrayList<ParentModel> list = (ArrayList<ParentModel>) fromJson(res,
                    new TypeToken<ArrayList<ParentModel>>() {
                    }.getType());
            setParentsAdapter(list) ;
        });

    }

    private void setParentsAdapter(ArrayList<ParentModel> list) {
        binding.setParentsAdapter(new ParentsAdapter(list , getApplicationContext() , pos->{

        })) ;
    }



    private void filterDialog() {
        Dialog dialog = Utility.openAnyDialog(R.layout.student_filter , ParentsListActivity.this) ;
        dialog.show() ;
    }
}