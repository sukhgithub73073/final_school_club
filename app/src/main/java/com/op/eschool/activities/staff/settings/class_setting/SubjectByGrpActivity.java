package com.op.eschool.activities.staff.settings.class_setting;

import static com.op.eschool.base.BaseActivity.commonDB;

import static com.op.eschool.base.BaseActivity.webSocketManager;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Utility.fromJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.activities.staff.class_section.AddClassActivity;
import com.op.eschool.activities.staff.class_section.AddSubjectActivity;
import com.op.eschool.adapters.ClassAdapter;
import com.op.eschool.adapters.SpiClassGroupAdapter;
import com.op.eschool.adapters.SubjectAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivitySubjectByGrpBinding;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.class_models.SubjectModel;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;

public class SubjectByGrpActivity extends BaseActivity {
    ActivitySubjectByGrpBinding binding ;
    ClassGroupModel groupModel ;
    List<SubjectModel> list = new ArrayList<>() ;
    GlobalLoader globalLoader ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_subject_by_grp) ;
        globalLoader = new GlobalLoader(SubjectByGrpActivity.this) ;

        binding.back.setOnClickListener(v->{onBackPressed();});
        binding.add.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , AddSubjectActivity.class));
        }) ;
        setClassesGroupAdapter();

    }


    @Override
    protected void onResume() {
        super.onResume();
        ClassGrpAdded();
    }

    private void setClassesGroupAdapter() {
        binding.setSubjectAdapter(new SubjectAdapter(list , (p, type)->{
            if (type.equalsIgnoreCase("DELETE")) {
                DialogModel dialogModel = new DialogModel(SubjectByGrpActivity.this ,"Delete","Want to sure to Delete this class ?","Yeah, sure","Cancel", t->{
                    if (t.equalsIgnoreCase("POSTIVE")){
                        //TODO Api Required
                        // UpStuTY(p,list.get(p) , Constants.USER_DELETE) ;
                    }
                } ) ;
                Utility.wantTOSureDialog(dialogModel) ;
            }else  if (type.equalsIgnoreCase("EDIT")) {
                startActivity(new Intent(getApplicationContext() , AddClassActivity.class)
                        .putExtra("CLASS_GROUP_MODEL" , new Gson().toJson(list.get(p))));
            }

        }));
    }

    private void ClassGrpAdded() {
        Utility.setGroupAdapter(SubjectByGrpActivity.this , getApplicationContext() , binding.classGrp , model->{
            binding.classGrp.setText(model.getGroupName()) ;
            groupModel = model;
            GetSubjctWisGrpDt();
        });
    }
    private void GetSubjctWisGrpDt() {
        Map<String , String> map = new HashMap<>() ;
        map.put("type","GetSubjctWisGrpDt") ;
        map.put("Unqid" , loginUserModel.getCollageUnqid()) ;
        map.put("GroupId" ,"" + groupModel.getGroupId());
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            runOnUiThread(()->{
                try {
                    globalLoader.dismissLoader();
                    list.clear();
                    List<SubjectModel> classList =  Utility.convertSubjectList(res) ;
                    list.addAll(classList) ;
                    binding.getSubjectAdapter().notifyDataSetChanged() ;
                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , SubjectByGrpActivity.this , ""+e.getMessage() ,()->{
                    }) ;
                }
            });
        });
    }

    
}