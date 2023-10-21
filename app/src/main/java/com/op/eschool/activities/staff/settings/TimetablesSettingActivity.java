package com.op.eschool.activities.staff.settings;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Utility.fromJson;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.adapters.SpiClassAdapter;
import com.op.eschool.adapters.SpiClassGroupAdapter;
import com.op.eschool.adapters.time_table.TimeDurationAdapter;
import com.op.eschool.adapters.time_table.TimetableAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityTimetablesSettingBinding;
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

public class TimetablesSettingActivity extends BaseActivity {
    ActivityTimetablesSettingBinding binding ;
    ClassGroupModel groupModel ;
    ClassModel classModel ;
    List<ClassModel> list = new ArrayList<>() ;
    String RESPONSE="";
    AllTimetableModel allTimetableModel ;
    TimeTableModel timeTableModel ;
    GlobalLoader globalLoader ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_timetables_setting) ;
        globalLoader = new GlobalLoader(TimetablesSettingActivity.this) ;

        allTimetableModel = new AllTimetableModel(new ArrayList<>() , new ArrayList<>() , new ArrayList<>()) ;
        GetTimeDuration() ;



    }
    @Override
    protected void onResume() {
        super.onResume();

//        ClassGrpAdded() ;
    }
    void StaffTbl(){
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"StaffTbl") ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(map , res->{
            runOnUiThread(()->{
                try {
                    globalLoader.dismissLoader();
                    ArrayList<StaffModel> list = (ArrayList<StaffModel>) fromJson(res,
                            new TypeToken<ArrayList<StaffModel>>() {
                            }.getType());

                    String[] teacherArray = new String[list.size()] ;
                    int count = 0 ;
                    for (StaffModel model:list){
                        teacherArray[count] = model.getFullName() ;
                        count++;
                    }
                    allTimetableModel.staffModelList = list ;
                    allTimetableModel.staffArray = teacherArray ;
                    ClassGrpAdded();
                }catch (Exception e){e.printStackTrace();}
            });

        });

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
//        map.put("Unqid" , loginUserModel.collageUnqid) ;
//        globalLoader.showLoader();
//        String json = new Gson().toJson(map) ;
//        webSocketManager.sendMessage(map , res->{
//
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
                binding.llHeader.setVisibility(View.GONE) ;

                GetSubjctWisGrpDt();
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
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        map.put("GroupId" ,"" + groupModel.getGroupId());
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(map , res->{
            runOnUiThread(()->{
                try {
                    globalLoader.dismissLoader();
                    list.clear();
                    List<ClassModel> classList=  Utility.convertClassList(res) ;
                    list.addAll(classList) ;
                    setClassSpinnerAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , TimetablesSettingActivity.this , ""+e.getMessage() ,()->{
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

    private void GetSubjctWisGrpDt() {
        Map<String , String> map = new HashMap<>() ;
        map.put("type","GetSubjctWisGrpDt") ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        map.put("GroupId" ,"" + groupModel.getGroupId());
        String json = new Gson().toJson(map) ;
        globalLoader.showLoader();
        webSocketManager.sendMessage(map , res->{
            runOnUiThread(()->{
                try {
                    globalLoader.dismissLoader();
                    list.clear();
                    List<SubjectModel> list =  Utility.convertSubjectList(res) ;

                    String[] subjectArray = new String[list.size()] ;
                    int count = 0 ;
                    for (SubjectModel model:list){
                        subjectArray[count] = model.getSubjectName() ;
                        count++;
                    }
                    allTimetableModel.subjectModels = list ;
                    allTimetableModel.subjectArray = subjectArray ;
                    GetClsWisGrpDt();

                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , TimetablesSettingActivity.this , ""+e.getMessage() ,()->{
                    }) ;
                }
            });
        });
    }


    private void GtTimeTable() {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"GtTimeTable") ;
        map.put("Unqid" ,""+ commonDB.getString("Unqid")) ;
        map.put("Class" , ""+classModel.getClassId()) ;
        String json = new Gson().toJson(map) ;
        FLog.w("GtTimeTable" ,"map>>>" +new Gson().toJson(map) );
        globalLoader.showLoader();
        webSocketManager.sendMessage(map , res->{
            RESPONSE= res;
            runOnUiThread(()->{
                globalLoader.dismissLoader();
                try {
                    RESPONSE = RESPONSE.replace("{'type': 'GtTimeTable'}," ,"") ;
                    ArrayList<TimeTableModel> list = (ArrayList<TimeTableModel>) fromJson(res,
                            new TypeToken<ArrayList<TimeTableModel>>() {
                            }.getType());
                    allTimetableModel.tableModelList = list ;
                    binding.llHeader.setVisibility(allTimetableModel.tableModelList.size()>0?View.VISIBLE:View.GONE);

                    initTimeTableData() ;
                }catch (Exception e){e.printStackTrace();}
            });
        });
    }



    private void GetTimeDuration() {
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"GetTimeDuration") ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        String json = new Gson().toJson(map) ;
        webSocketManager.sendMessage(map , res->{
            runOnUiThread(()->{
                try {
                    ArrayList<TimeDurationModel> list = (ArrayList<TimeDurationModel>) fromJson(res,
                            new TypeToken<ArrayList<TimeDurationModel>>() {
                            }.getType());
                    setTimeDurationTable(list) ;
                    StaffTbl();
                }catch (Exception e){e.printStackTrace();}
            });
        });
    }
    private void setTimeDurationTable(ArrayList<TimeDurationModel> list) {
        binding.setTimeDurationAdapter(new TimeDurationAdapter(getApplicationContext() , list ,(p,t)->{

        }));
    }
    private void initTimeTableData() {
        allTimetableModel.tableModelList.remove(0) ;
        for (TimeTableModel s :allTimetableModel.tableModelList){
            s.setSubjectType(false) ;
            s.setTeacherType(false);
        }


        binding.setTimetableAdapter(new TimetableAdapter("TRUE" , getApplicationContext() , allTimetableModel , (position, type , textView)->{
            timeTableModel =allTimetableModel.tableModelList.get(position) ;
            if(type.equalsIgnoreCase("STAFF")){
                selectTeacher(textView) ;
            }else if(type.equalsIgnoreCase("SUBJECT")){
                selectSubject(textView , "SUBJECT") ;
            }else {
                selectSubject(textView , "SUBSUBJECT") ;
            }
        }));
    }
    private void UpTimeTable() {
        Map<String,String> map = new HashMap<>() ;
        map.put("type" ,"UpTimeTable") ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        map.put("Period" ,""+timeTableModel.getPeriod()) ;
        map.put("TimeFrom" ,""+timeTableModel.getTimeFrom()) ;
        map.put("TimeTo" ,"" + timeTableModel.getTimeTo()) ;
        map.put("Class" ,""+timeTableModel.getClass_()) ;
        map.put("MainClass" ,""+timeTableModel.getMainClass()) ;
        map.put("Subject" ,""+timeTableModel.getSubject()) ;
        map.put("SubjectId" ,""+timeTableModel.getSubjectId()) ;
        map.put("Teacher" ,""+timeTableModel.getTeacher()) ;
        map.put("TeacherId" ,""+timeTableModel.getTeacherId()) ;
        map.put("SubSubject" ,""+timeTableModel.getSubject()) ;
        map.put("TimeTableId" ,""+timeTableModel.getTimeTableId()) ;
        String json = new Gson().toJson(map) ;
        webSocketManager.sendMessage(map , res->{
            try {
                runOnUiThread(()->{
                    CommonResponse commonResponse = Utility.convertResponse(res) ;

                    if (!commonResponse.getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS)){
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , TimetablesSettingActivity.this , ""+commonResponse.getMsg() ,()->{
                        }) ;
                    }
                });
            }catch (Exception e){e.printStackTrace();}
        });
    }


    void selectTeacher(TextView textView) {
        SearchableSpinner teacher = new SearchableSpinner(this);
        teacher.setWindowTitle("Select Teacher") ;
        ArrayList<String> teacherList = new ArrayList<>() ;
        Map<String , StaffModel> teacherMap = new HashMap<>() ;
        for (StaffModel ss : allTimetableModel.staffModelList){
            teacherList.add(ss.getFullName()) ;
            teacherMap.put(""+ss.getFullName() ,  ss) ;
        }
        teacher.setSpinnerListItems(teacherList);
        teacher.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                textView.setText(selectedString) ;
                timeTableModel.setTeacher(teacherMap.get(selectedString).getFullName());
                timeTableModel.setTeacherId(teacherMap.get(selectedString).getStaffId());
                UpTimeTable();

            }
        });
        teacher.show();
    }

    void selectSubject(TextView textView , String type){
        SearchableSpinner subject = new SearchableSpinner(this) ;
        subject.setWindowTitle("Select Subject") ;
        if (type.equalsIgnoreCase("SUBSUBJECT")){
            subject.setWindowTitle("Select Sub Subject") ;
        }

        ArrayList<String> strList = new ArrayList<>() ;
        Map<String , SubjectModel> subjectMap = new HashMap<>() ;
        for (SubjectModel s : allTimetableModel.subjectModels){
            strList.add(s.getSubjectName()) ;
            subjectMap.put(""+s.getSubjectName() ,  s) ;
        }
        subject.setSpinnerListItems(strList);
        subject.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                textView.setText(selectedString) ;
                if (type.equalsIgnoreCase("SUBJECT")) {
                    timeTableModel.setSubject(subjectMap.get(selectedString).getSubjectName());
                    timeTableModel.setSubjectId(subjectMap.get(selectedString).getSubjectId());
                    timeTableModel.setSubjectOption("True");

                }else{
                    timeTableModel.setSubSubject(subjectMap.get(selectedString).getSubjectName());
                    timeTableModel.setSubSubjectId(subjectMap.get(selectedString).getSubjectId());
                }

                UpTimeTable();
            }
        });
        subject.show();
    }

}