package com.op.eschool.activities.staff.class_section;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Utility.fromJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.activities.staff.settings.class_setting.SubjectByGrpActivity;
import com.op.eschool.adapters.SpiClassGroupAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityAddSubjectBinding;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.class_models.AllSubjectModel;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.class_models.SubjectModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FLog;
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

public class AddSubjectActivity extends BaseActivity {
    ClassGroupModel groupModel ;
    ActivityAddSubjectBinding binding ;
    GlobalLoader globalLoader ;
    ArrayList<AllSubjectModel> subjectList = new ArrayList<>() ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this , R.layout.activity_add_subject);
        globalLoader = new GlobalLoader(AddSubjectActivity.this) ;

        binding.back.setOnClickListener(v->{onBackPressed();});
        Glide.with(getApplicationContext())
                .load(Utility.convertBase64ToBitmap(loginUserModel.getImage()))
                .apply(new RequestOptions().placeholder(R.drawable.logo))
                .into(binding.logo)   ;        ClassGrpAdded() ;
        binding.submit.setOnClickListener(v->{
            if (groupModel==null){
                binding.etSubjectGrp.setError("Invalid Class Group");
            }else if (binding.subjectName.getText().toString().equalsIgnoreCase("")){
                binding.subjectName.setError("Invalid Subject Name");
            }else if (binding.subjectCode.getText().toString().equalsIgnoreCase("")){
                binding.subjectCode.setError("Invalid Subject Code");
            }else{
                AddSubjectAndSubCodWisGrpData() ;
            }
        });
    }

    private void AddSubjectAndSubCodWisGrpData() {

        String UpdtSubjectData = new Gson().toJson(subjectList) ;
        if (!UpdtSubjectData.contains("CHANGE_SUBJECT")){
            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , AddSubjectActivity.this , "Not More add subject in this Group" ,()->{
            }) ;
        }else {
            UpdtSubjectData = UpdtSubjectData.replace("CHANGE_SUBJECT", binding.subjectName.getText().toString().toUpperCase());
            UpdtSubjectData = UpdtSubjectData.replace("CHANGE_CODE", binding.subjectCode.getText().toString().toUpperCase());
            FLog.w("ClassAdd", "UpdtSubjectData" + UpdtSubjectData);
            Map<String, String> map = new HashMap<>();
            map.put("type", "UpdtGrpSubjtsData");
            map.put("Unqid" , loginUserModel.getCollageUnqid()) ;
            map.put("UpdtGrpSubjtsData", UpdtSubjectData);
            String json = new Gson().toJson(map);
            FLog.w("ClassAdd", "map" + json);

            globalLoader.showLoader();
            webSocketManager.sendMessage(json, res -> {
                runOnUiThread(() -> {
                    try {
                        globalLoader.dismissLoader();
                        CommonResponse commonResponse = Utility.convertResponse(res);
                        if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)) {
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS, AddSubjectActivity.this, "Successfully Subject will be added", () -> {
                                Utility.gotoHome(getApplicationContext());
                            });
                        } else {
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED, AddSubjectActivity.this, "" + commonResponse.Msg, () -> {
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED, AddSubjectActivity.this, "" + e.getMessage(), () -> {
                        });
                    }
                });
            });
        }


    }
    private void ClassGrpAdded() {
        Utility.setGroupAdapter(AddSubjectActivity.this , getApplicationContext() , binding.etSubjectGrp , model->{
            binding.etSubjectGrp.setText(model.getGroupName()) ;
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
                    subjectList = (ArrayList<AllSubjectModel>) fromJson(res,
                            new TypeToken<ArrayList<AllSubjectModel>>() {
                            }.getType());
                    if (subjectList.size()>0){
                        if (subjectList.get(0).getSubjectName1().toLowerCase().equalsIgnoreCase("AddSubject")){
                            subjectList.get(0).setSubjectName1("CHANGE_SUBJECT") ;
                            subjectList.get(0).setSubjectCode1("CHANGE_CODE");
                        }else if (subjectList.get(0).getSubjectName2().toLowerCase().equalsIgnoreCase("AddSubject")){
                            subjectList.get(0).setSubjectName2("CHANGE_SUBJECT") ;
                            subjectList.get(0).setSubjectCode2("CHANGE_CODE");
                        }else if (subjectList.get(0).getSubjectName3().toLowerCase().equalsIgnoreCase("AddSubject")){
                            subjectList.get(0).setSubjectName3("CHANGE_SUBJECT") ;
                            subjectList.get(0).setSubjectCode3("CHANGE_CODE");

                        }else if (subjectList.get(0).getSubjectName4().toLowerCase().equalsIgnoreCase("AddSubject")){
                            subjectList.get(0).setSubjectName4("CHANGE_SUBJECT") ;
                            subjectList.get(0).setSubjectCode4("CHANGE_CODE");

                        }else if (subjectList.get(0).getSubjectName5().toLowerCase().equalsIgnoreCase("AddSubject")){
                            subjectList.get(0).setSubjectName5("CHANGE_SUBJECT") ;
                            subjectList.get(0).setSubjectCode5("CHANGE_CODE");

                        }else if (subjectList.get(0).getSubjectName6().toLowerCase().equalsIgnoreCase("AddSubject")){
                            subjectList.get(0).setSubjectName6("CHANGE_SUBJECT") ;
                            subjectList.get(0).setSubjectCode6("CHANGE_CODE");

                        }else if (subjectList.get(0).getSubjectName7().toLowerCase().equalsIgnoreCase("AddSubject")){
                            subjectList.get(0).setSubjectName7("CHANGE_SUBJECT") ;
                            subjectList.get(0).setSubjectCode7("CHANGE_CODE");
                        }else if (subjectList.get(0).getSubjectName8().toLowerCase().equalsIgnoreCase("AddSubject")){
                            subjectList.get(0).setSubjectName8("CHANGE_SUBJECT") ;
                            subjectList.get(0).setSubjectCode8("CHANGE_CODE");
                        }else if (subjectList.get(0).getSubjectName9().toLowerCase().equalsIgnoreCase("AddSubject")){
                            subjectList.get(0).setSubjectName9("CHANGE_SUBJECT") ;
                            subjectList.get(0).setSubjectCode9("CHANGE_CODE");
                        }else if (subjectList.get(0).getSubjectName10().toLowerCase().equalsIgnoreCase("AddSubject")){
                            subjectList.get(0).setSubjectName10("CHANGE_SUBJECT") ;
                            subjectList.get(0).setSubjectCode10("CHANGE_CODE");
                        }else if (subjectList.get(0).getSubjectName11().toLowerCase().equalsIgnoreCase("AddSubject")){
                            subjectList.get(0).setSubjectName11("CHANGE_SUBJECT") ;
                            subjectList.get(0).setSubjectCode11("CHANGE_CODE");
                        }else if (subjectList.get(0).getSubjectName12().toLowerCase().equalsIgnoreCase("AddSubject")){
                            subjectList.get(0).setSubjectName12("CHANGE_SUBJECT") ;
                            subjectList.get(0).setSubjectCode12("CHANGE_CODE");
                        }else if (subjectList.get(0).getSubjectName13().toLowerCase().equalsIgnoreCase("AddSubject")){
                            subjectList.get(0).setSubjectName13("CHANGE_SUBJECT") ;
                            subjectList.get(0).setSubjectCode13("CHANGE_CODE");
                        }else if (subjectList.get(0).getSubjectName14().toLowerCase().equalsIgnoreCase("AddSubject")){
                            subjectList.get(0).setSubjectName14("CHANGE_SUBJECT") ;
                            subjectList.get(0).setSubjectCode14("CHANGE_CODE");
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , AddSubjectActivity.this , ""+e.getMessage() ,()->{
                    }) ;
                }
            });
        });
    }

}