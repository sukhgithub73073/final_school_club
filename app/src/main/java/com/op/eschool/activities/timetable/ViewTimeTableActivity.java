package com.op.eschool.activities.timetable;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Utility.fromJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.adapters.time_table.TimeDurationAdapter;
import com.op.eschool.adapters.time_table.TimetableAdapter;
import com.op.eschool.adapters.time_table.ViewTimetableAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityTimetablesSettingBinding;
import com.op.eschool.databinding.ActivityViewTimeTableBinding;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.class_models.SubjectModel;
import com.op.eschool.models.staff.StaffModel;
import com.op.eschool.models.timetable_model.AllTimetableModel;
import com.op.eschool.models.timetable_model.TimeDurationModel;
import com.op.eschool.models.timetable_model.TimeTableModel;
import com.op.eschool.util.Constants;
import com.op.eschool.util.FLog;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewTimeTableActivity extends BaseActivity {
    ActivityViewTimeTableBinding binding ;
    ClassGroupModel groupModel ;
    ClassModel classModel ;
    List<ClassModel> list = new ArrayList<>() ;
    String RESPONSE="";
    AllTimetableModel allTimetableModel ;
    TimeTableModel timeTableModel ;
    int selectPosition = 0 ;
    GlobalLoader globalLoader ;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_view_time_table);
        globalLoader = new GlobalLoader(ViewTimeTableActivity.this) ;

        allTimetableModel = new AllTimetableModel(new ArrayList<>() , new ArrayList<>() , new ArrayList<>()) ;
        ClassGrpAdded();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    private void ClassGrpAdded() {
        try {
            String res = commonDB.getString("ClassGrpAdded");
            ArrayList<ClassGroupModel> list = (ArrayList<ClassGroupModel>) fromJson(res,
                    new TypeToken<ArrayList<ClassGroupModel>>() {
                    }.getType());
            setGroupAdapter(list) ;
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Map<String , String> map = new HashMap<>() ;
//        map.put("type" ,"ClassGrpAdded") ;
//        map.put("Unqid" , commonDB.getString("Unqid")) ;
//        globalLoader.showLoader();
//        String json = new Gson().toJson(map) ;
//        webSocketManager.sendMessage(json , res->{
//            runOnUiThread(()->{
//                globalLoader.dismissLoader();
//                try {
//                    ArrayList<ClassGroupModel> list = (ArrayList<ClassGroupModel>) fromJson(res,
//                            new TypeToken<ArrayList<ClassGroupModel>>() {
//                            }.getType());
//                    setGroupAdapter(list) ;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }) ;
    }
    private void setGroupAdapter(ArrayList<ClassGroupModel> groupList) {


        SearchableSpinner caste = new SearchableSpinner(this);
        caste.setWindowTitle("Select Class Group") ;
        ArrayList<String> strList = new ArrayList<>() ;
        Map<String , ClassGroupModel> classMap = new HashMap<>() ;
        for (ClassGroupModel s : groupList){
            strList.add(s.getGroupName()) ;
            classMap.put(""+s.getGroupName() ,  s) ;
        }
        caste.setSpinnerListItems(strList);
        caste.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                binding.classGrp.setText(selectedString) ;
                groupModel = classMap.get(selectedString) ;
                binding.txtClass.setText("");

                binding.llContent.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE) ;


                GetClsWisGrpDt();
            }
        });
        binding.classGrp.setOnClickListener(v->{
            caste.setHighlightSelectedItem(true);
            caste.show();
        });
    }
    private void GetClsWisGrpDt() {
        Map<String , String> map = new HashMap<>() ;
        map.put("type","GetClsWisGrpDt") ;
        map.put("Unqid" , commonDB.getString("Unqid")) ;
        map.put("GroupId" ,"" + groupModel.getGroupId());
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            runOnUiThread(()->{
                try {
                    globalLoader.dismissLoader();
                    list.clear();
                    List<ClassModel> classList=  Utility.convertClassList(res) ;
                    list.addAll(classList) ;
                    setClassSpinnerAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , ViewTimeTableActivity.this , ""+e.getMessage() ,()->{
                    }) ;
                }
            });
        });
    }
    private void setClassSpinnerAdapter() {

        SearchableSpinner caste = new SearchableSpinner(this);
        caste.setWindowTitle("Select Class") ;
        ArrayList<String> strList = new ArrayList<>() ;
        Map<String , ClassModel> classMap = new HashMap<>() ;
        for (ClassModel s : list){
            strList.add(s.getClassName()) ;
            classMap.put(""+s.getClassName() ,  s) ;
        }
        caste.setSpinnerListItems(strList);
        caste.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                binding.txtClass.setText(selectedString) ;
                classModel = list.get(position) ;
                GtTimeTable() ;
            }
        });
        binding.txtClass.setOnClickListener(v->{
            caste.setHighlightSelectedItem(true);
            caste.show();
        });
    }


    private void GtTimeTable() {
        selectPosition = 0 ;
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"GtTimeTable") ;
        map.put("Unqid" ,""+ commonDB.getString("Unqid")) ;
        map.put("Class" , ""+classModel.getClassId()) ;
        String json = new Gson().toJson(map) ;
        FLog.w("GtTimeTable" ,"map>>>" +new Gson().toJson(map) );
        globalLoader.showLoader();
        webSocketManager.sendMessage(json , res->{
            RESPONSE= res;
            runOnUiThread(()->{
                globalLoader.dismissLoader();
                try {
                    RESPONSE = RESPONSE.replace("{'type': 'GtTimeTable'}," ,"") ;
                    ArrayList<TimeTableModel> list = (ArrayList<TimeTableModel>) fromJson(res,
                            new TypeToken<ArrayList<TimeTableModel>>() {
                            }.getType());
                    allTimetableModel.tableModelList = list ;

                    initTimeTableData() ;
                }catch (Exception e){e.printStackTrace();}
            });
        });
    }

    private void initTimeTableData() {
        binding.llContent.setVisibility(allTimetableModel.tableModelList.size()>0?View.VISIBLE:View.GONE);
        binding.noData.setVisibility(allTimetableModel.tableModelList.size()>0?View.GONE:View.VISIBLE) ;
        allTimetableModel.tableModelList.remove(0) ;

        if (allTimetableModel.tableModelList.size()>0){
            timeTableModel = allTimetableModel.tableModelList.get(0) ;
            binding.setModel(timeTableModel);
        }
        binding.setViewTimetableAdapter(new ViewTimetableAdapter(selectPosition , getApplicationContext() , allTimetableModel , (position, type , textView)->{
            timeTableModel = allTimetableModel.tableModelList.get(position) ;
            binding.setModel(timeTableModel);
            selectPosition = position ;
            binding.getViewTimetableAdapter().notifyDataSetChanged() ;
        }));
    }



}