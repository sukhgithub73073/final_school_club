package com.op.eschool.activities.staff.class_section;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_SUCESS;
import static com.op.eschool.util.Utility.fromJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
import com.op.eschool.activities.staff.settings.class_setting.ClassByGrpActivity;
import com.op.eschool.activities.staff.student.PromotionActivity;
//import com.op.eschool.activities.staff.student.StudentRegisterActivity;
import com.op.eschool.adapters.SpiClassGroupAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityAddClassBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.class_models.AllClassModel;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.login_model.LoginUserModel;
import com.op.eschool.retrofit.RetrofitClient;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FLog;
import com.op.eschool.util.FToast;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddClassActivity extends BaseActivity {

    ActivityAddClassBinding binding ;
    ClassGroupModel groupModel ;
    GlobalLoader globalLoader ;
    ArrayList<AllClassModel> classList = new ArrayList<>() ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this , R.layout.activity_add_class) ;
        globalLoader = new GlobalLoader(AddClassActivity.this) ;


        binding.back.setOnClickListener(v->{onBackPressed();});
        Glide.with(getApplicationContext())
                .load(Utility.convertBase64ToBitmap(loginUserModel.getImage()))
                .apply(new RequestOptions().placeholder(R.drawable.logo))
                .into(binding.logo)   ;
        ClassGrpAdded() ;
        binding.submit.setOnClickListener(v->{
            if (groupModel==null){
                binding.etClassGrp.setError("Invalid Class Group");
            }else if (binding.className.getText().toString().equalsIgnoreCase("")){
                binding.className.setError("Invalid Class Name");
            }else{
                ClassAdd() ;
            }
        });
    }
    private void ClassAdd() {

        String UpdtGrpClsData = new Gson().toJson(classList) ;
        if (!UpdtGrpClsData.contains("CHANGE_HERE")){
            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , AddClassActivity.this , "Not More add class in this Group" ,()->{
            }) ;
        }else{
            UpdtGrpClsData = UpdtGrpClsData.replace("CHANGE_HERE" , binding.className.getText().toString().toUpperCase()) ;
            Map<String , String> map = new HashMap<>() ;
            map.put("type","UpGrpClsData") ;
            map.put("Unqid" , loginUserModel.getCollageUnqid()) ;
            map.put("UpdtGrpClsData" ,UpdtGrpClsData) ;
            String json = new Gson().toJson(map) ;
            FLog.w("ClassAdd" , "map" +json) ;
            globalLoader.showLoader();
            webSocketManager.sendMessage(json , res->{
                runOnUiThread(()->{
                    try {
                        globalLoader.dismissLoader();
                        CommonResponse commonResponse = Utility.convertResponse(res) ;
                        if (commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_SUCESS , AddClassActivity.this , "Successfully class will be added"    ,()->{
                                GetClsAndGrpDt(pos -> {
                                    Utility.gotoHome(getApplicationContext());
                                });
                            }) ;
                        }else {
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , AddClassActivity.this , ""+commonResponse.Msg ,()->{
                            }) ;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , AddClassActivity.this , ""+e.getMessage() ,()->{
                        }) ;
                    }
                });
            });
        }
    }
    public void ClassGrpAdded(){
        Utility.setGroupAdapter(AddClassActivity.this , getApplicationContext() , binding.etClassGrp , model->{
            groupModel = model ;
            binding.etClassGrp.setText(groupModel.getGroupName());
            GetClsWisGrpDt() ;
        });
    }
    private void GetClsWisGrpDt() {
        Map<String , String> map = new HashMap<>() ;
        map.put("type","GetClsWisGrpDt") ;
        map.put("Unqid" , loginUserModel.getCollageUnqid()) ;
        map.put("GroupId" ,"" + groupModel.getGroupId());
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            runOnUiThread(()->{
                try {
                    globalLoader.dismissLoader();
                    classList = (ArrayList<AllClassModel>) fromJson(res,
                            new TypeToken<ArrayList<AllClassModel>>() {
                            }.getType());
                    if (classList.size()>0){
                        if (classList.get(0).getClassName1().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName1("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName2().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName2("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName3().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName3("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName4().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName4("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName5().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName5("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName6().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName6("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName7().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName7("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName8().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName8("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName9().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName9("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName10().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName10("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName11().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName11("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName12().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName12("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName13().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName13("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName14().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName14("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName15().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName15("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName16().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName16("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName17().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName17("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName18().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName18("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName19().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName19("CHANGE_HERE") ;
                        }else if (classList.get(0).getClassName20().toLowerCase().equalsIgnoreCase("addclass")){
                            classList.get(0).setClassName20("CHANGE_HERE") ;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , AddClassActivity.this , ""+e.getMessage() ,()->{
                    }) ;
                }
            });
        });
    }

    void GetClsAndGrpDt(CommonInterface commonInterface){
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"GetClsAndGrpDt") ;
        map.put("Unqid" ,loginUserModel.getCollageUnqid()) ;
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            runOnUiThread(()->{
                try {
                    globalLoader.dismissLoader();
                    commonDB.putString("GetClsAndGrpDt" , res) ;
                    commonInterface.onItemClicked(0) ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }) ;
    }
}