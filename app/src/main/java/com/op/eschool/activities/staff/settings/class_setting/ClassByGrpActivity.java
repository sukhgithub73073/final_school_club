package com.op.eschool.activities.staff.settings.class_setting;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.op.eschool.R;
import com.op.eschool.activities.staff.class_section.AddClassActivity;
import com.op.eschool.adapters.ClassAdapter;
import com.op.eschool.adapters.SettingClassAdapter;
import com.op.eschool.adapters.SpiClassGroupAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityClassByGrpBinding;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.SubCasteModel;
import com.op.eschool.models.chatboat_model.ChatboatModel;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.login_model.LoginUserModel;
import com.op.eschool.models.student.StudentModel;
import com.op.eschool.util.FLog;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassByGrpActivity extends BaseActivity {
    ActivityClassByGrpBinding binding ;
    List<ClassModel> list = new ArrayList<>() ;
    GlobalLoader globalLoader ;
    LoginUserModel loginUserModel ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_class_by_grp) ;
        globalLoader = new GlobalLoader(ClassByGrpActivity.this) ;
        loginUserModel = Utility.getLoginUser(getApplicationContext()) ;
        binding.back.setOnClickListener(v->{onBackPressed();});
        binding.add.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , AddClassActivity.class));
        }) ;
        setClassesAdapter();
        setGroupAdapter();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    private void setGroupAdapter() {
        Utility.setGroupAdapter(ClassByGrpActivity.this , getApplicationContext() , binding.classGrp , model->{
            list.clear();
            list.addAll(model.getClassList()) ;
            binding.getClassAdapter().notifyDataSetChanged() ;
        });
    }
    private void setClassesAdapter() {
        binding.setClassAdapter(new SettingClassAdapter(list , (p, type)->{
            if (type.equalsIgnoreCase("DELETE")) {
                DialogModel dialogModel = new DialogModel(ClassByGrpActivity.this ,"Delete","Want to sure to Delete this class ?","Yeah, sure","Cancel", t->{
                    if (t.equalsIgnoreCase("POSTIVE")){
                    }
                } ) ;
                Utility.wantTOSureDialog(dialogModel) ;
            }else  if (type.equalsIgnoreCase("EDIT")) {
                startActivity(new Intent(getApplicationContext() , AddClassActivity.class)
                        .putExtra("CLASS_GROUP_MODEL" , new Gson().toJson(list.get(p))));
            }

        }));
    }


}