package com.op.eschool.activities.staff.class_section;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Utility.fromJson;

import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
//import com.op.eschool.activities.staff.student.StudentRegisterActivity;
import com.op.eschool.adapters.class_group.ClassGroupAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityClassGroupListBinding;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FLog;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassGroupListActivity extends BaseActivity {
    ActivityClassGroupListBinding binding ;
    GlobalLoader globalLoader ;

    @Override
    protected void onResume() {
        super.onResume();
        ClassGrpAdded();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_class_group_list) ;
        globalLoader = new GlobalLoader(ClassGroupListActivity.this) ;

        binding.add.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , AddClassGroupActivity.class)
                    .putExtra("CLASS_GROUP_MODEL" , "")) ;
        }) ;
        binding.filter.setOnClickListener(v->{
            filterDialog() ;
        });
    }
    private void filterDialog() {
        Dialog dialog = Utility.openAnyDialog(R.layout.student_filter , ClassGroupListActivity.this) ;
        dialog.show() ;
    }
    private void ClassGrpAdded() {

        List<ClassGroupModel> groupModelList  = Utility.getGroupClassList(getApplicationContext()) ;
        setClassGroupAdapter(groupModelList) ;

    }
    private void setClassGroupAdapter(List<ClassGroupModel> list) {
        binding.setClassGroupAdapter(new ClassGroupAdapter( getApplicationContext() ,list , (p,type)->{
            if (type.equalsIgnoreCase("DELETE")) {
                DialogModel dialogModel = new DialogModel(ClassGroupListActivity.this ,"Delete","Want to sure to Delete this class group ?","Yeah, sure","Cancel", t->{
                    if (t.equalsIgnoreCase("POSTIVE")){
                    }
                } ) ;
                Utility.wantTOSureDialog(dialogModel) ;
            }else  if (type.equalsIgnoreCase("EDIT")) {
                startActivity(new Intent(getApplicationContext() , AddClassGroupActivity.class)
                        .putExtra("CLASS_GROUP_MODEL" , new Gson().toJson(list.get(p))));
            }
        }));
    }
}