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
import com.op.eschool.activities.staff.student.StudentListActivity;
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
import com.op.eschool.models.student.StudentModel;
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
    List<ClassModel> list = new ArrayList<>() ;
    String RESPONSE="";
    AllTimetableModel allTimetableModel ;
    TimeTableModel timeTableModel ;
    int selectPosition = 0 ;
    GlobalLoader globalLoader ;
    Map<String , String> map = new HashMap<>() ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_view_time_table);
        globalLoader = new GlobalLoader(ViewTimeTableActivity.this) ;
        allTimetableModel = new AllTimetableModel(new ArrayList<>() , new ArrayList<>() , new ArrayList<>()) ;

        map.put("type" ,"GtTimeTable") ;
        map.put("Unqid" ,""+ commonDB.getString("Unqid")) ;


        manageClicks() ;
    }

    private void manageClicks() {

        Utility.setGroupAdapter(ViewTimeTableActivity.this , getApplicationContext() , binding.txtGroup , model->{

            binding.txtClass.setText("");
            binding.noData.setVisibility(View.VISIBLE) ;
            binding.llContent.setVisibility(View.GONE) ;

            Utility.setClassesAdapter(ViewTimeTableActivity.this , model.getClassList() , getApplicationContext() , binding.txtClass ,classModel->{

                map.put("ClassId" , classModel.getClassId()+"") ;
                map.put("Class" , classModel.getClassId()+"") ;
                GtTimeTable() ;
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void GtTimeTable() {
        selectPosition = 0 ;
       String json = new Gson().toJson(map) ;
        FLog.w("GtTimeTable" ,"map>>>" +new Gson().toJson(map) );
        globalLoader.showLoader();
        webSocketManager.sendMessage(map , res->{
            RESPONSE= res;
            runOnUiThread(()->{
                globalLoader.dismissLoader();
                try {
                    RESPONSE = RESPONSE.replace("{'type': 'GtTimeTable'}," ,"") ;
                    RESPONSE = RESPONSE.replace("{\"type\": \"GtTimeTable\"}," ,"") ;
                    FLog.w("GtTimeTable" ,"RESPONSE>>>" +RESPONSE );

                    List<TimeTableModel> list = (ArrayList<TimeTableModel>) fromJson(RESPONSE,
                            new TypeToken<ArrayList<TimeTableModel>>() {
                            }.getType());
//
//                    List<TimeTableModel> list = (ArrayList<TimeTableModel>) fromJson(RESPONSE,
//                            new TypeToken<ArrayList<TimeTableModel>>() {
//                            }.getType());
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
            String sub = timeTableModel.getSubject().equalsIgnoreCase("Select Subject")?"--":timeTableModel.getSubject() ;
            String subSubject = timeTableModel.getSubSubject().equalsIgnoreCase("SELECT SUB SUBJECT")?"--":timeTableModel.getSubSubject() ;
            if (!sub.equalsIgnoreCase("--")){
                binding.txtSubject.setText(sub + " ( "+ subSubject +" )");
            }
        }
        binding.setViewTimetableAdapter(new ViewTimetableAdapter(selectPosition , getApplicationContext() , allTimetableModel , (position, type , textView)->{
            timeTableModel = allTimetableModel.tableModelList.get(position) ;
            binding.setModel(timeTableModel);
            selectPosition = position ;
            binding.getViewTimetableAdapter().notifyDataSetChanged() ;
        }));
    }
}