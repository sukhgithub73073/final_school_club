package com.op.eschool.activities.staff.class_section;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Utility.fromJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.activities.staff.parents.ParentsListActivity;
import com.op.eschool.adapters.ClassAdapter;
import com.op.eschool.adapters.ClassNexusAdapter;
import com.op.eschool.adapters.ParentNexusAdapter;
import com.op.eschool.adapters.SpiClassGroupAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityClassListBinding;
import com.op.eschool.databinding.FilterClassBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.class_models.AllClassModel;
import com.op.eschool.models.class_models.ClassCountModel;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassListActivity extends BaseActivity {
    ActivityClassListBinding binding ;
    List<ClassGroupModel> groupModelList  =new ArrayList<>() ;

    ClassGroupModel groupModel ;
    GlobalLoader globalLoader ;
    List<ClassModel> list = new ArrayList<>() ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_class_list);
        globalLoader = new GlobalLoader(ClassListActivity.this) ;
        binding.add.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , AddClassActivity.class));
        }) ;
        ClassGrpAdded();
        manageClicks() ;

    }

    private void manageClicks() {
        Utility.setGroupAdapter(ClassListActivity.this , getApplicationContext() , binding.txtGroup , model->{
            setClassesAdapter(model.getClassList()) ;
        });

    }

    private void ClassGrpAdded() {
        try {
            String res = commonDB.getString("ClassGrpAdded");
            groupModelList = (ArrayList<ClassGroupModel>) fromJson(res,
                    new TypeToken<ArrayList<ClassGroupModel>>() {
                    }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setClassesAdapter(List<ClassModel> list) {
        binding.setClassAdapter(new ClassAdapter(list , (p,type)->{
            if (type.equalsIgnoreCase("DELETE")) {
                DialogModel dialogModel = new DialogModel(ClassListActivity.this ,"Delete","Want to sure to Delete this class ?","Yeah, sure","Cancel", t->{
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