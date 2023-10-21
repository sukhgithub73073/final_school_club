package com.op.eschool.activities.staff.class_section;

import static com.op.eschool.util.Utility.fromJson;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.op.eschool.R;
import com.op.eschool.adapters.StaffDashboardAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityClassListBinding;
import com.op.eschool.models.DashboardModel;
import com.op.eschool.models.class_models.CastCounterModel;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

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
    List<CastCounterModel> list = new ArrayList<>() ;
    Map<String , String> map = new HashMap<>() ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_class_list);
        globalLoader = new GlobalLoader(ClassListActivity.this) ;
        map.put("type" , "GetClassWiseCastListAll") ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        manageClicks() ;

    }

    private void manageClicks() {
        Utility.setGroupAdapter(ClassListActivity.this , getApplicationContext() , binding.txtGroup , model->{
            binding.txtClass.setText("");
            binding.noData.setVisibility(View.VISIBLE) ;
            binding.rvStudents.setVisibility(View.GONE) ;

            Utility.setClassesAdapter(ClassListActivity.this , model.getClassList() , getApplicationContext() , binding.txtClass ,CastCounterModel->{
                GetClassWiseCastListAll(CastCounterModel.getClassId()) ;
            });
        });
    }
    private void GetClassWiseCastListAll(String classId) {
        List<String> stringList = new ArrayList<>() ;
        stringList.add(classId) ;
        map.put("Class" , new Gson().toJson(stringList)) ;
        String json = new Gson().toJson(map) ;
        json =json.replace("\"[","[")
                .replace("]\"","]")
                .replace("\\","")
        ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(map , res->{
            try {
                runOnUiThread(()->{
                    globalLoader.dismissLoader();
                    list = (ArrayList<CastCounterModel>) fromJson(res,
                            new TypeToken<ArrayList<CastCounterModel>>() {
                            }.getType());
                    setClassesAdapter(list) ;
                });
            } catch (Exception e) {
                e.printStackTrace() ;
            }
        });
    }
    private void setClassesAdapter(List<CastCounterModel> listCounter) {
        if (listCounter.size()>0){
            binding.noData.setVisibility(View.GONE);
            binding.rvStudents.setVisibility(View.VISIBLE) ;
        }else{
            binding.noData.setVisibility(View.VISIBLE) ;
            binding.rvStudents.setVisibility(View.GONE) ;
        }
        List<DashboardModel> list = new ArrayList<>() ;
        list.add(new DashboardModel("Genral\n( "+listCounter.get(0).getGENRALCount()+" )" ,R.drawable.student_male,null));
        list.add(new DashboardModel("OBC\n( "+listCounter.get(0).getOBCCount()+" )" ,R.drawable.student_male,null));
        list.add(new DashboardModel("SC\n( "+listCounter.get(0).getSCCount()+" )" ,R.drawable.student_male,null));
        list.add(new DashboardModel("ST\n( "+listCounter.get(0).getSTCount()+" )" ,R.drawable.student_male,null));
        binding.setDashboardAdapter(new StaffDashboardAdapter(list, getApplicationContext() , pos->{
        }));
    }
}