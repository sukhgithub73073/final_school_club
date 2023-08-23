package com.op.eschool.activities.staff.principle;

import static com.op.eschool.util.Utility.fromJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.op.eschool.R;
import com.op.eschool.activities.complaints.ComplaintsActivity;
import com.op.eschool.activities.staff.CreateNotesActivity;
import com.op.eschool.activities.staff.ProfileActivity;
import com.op.eschool.activities.staff.attendance.AttendanceActivity;
import com.op.eschool.activities.staff.attendance.AttendanceMonthlyActivity;
import com.op.eschool.activities.staff.class_section.ClassListActivity;
import com.op.eschool.activities.staff.class_section.ClassStudentListActivity;
import com.op.eschool.activities.staff.class_section.MonitorListActivity;
import com.op.eschool.activities.staff.class_section.SubjectTeacherActivity;
import com.op.eschool.activities.staff.leave.LeaveListActivity;
import com.op.eschool.activities.staff.parents.ParentsListActivity;
import com.op.eschool.activities.staff.school.AddSessionsActivity;
import com.op.eschool.activities.staff.school.SchoolListActivity;
import com.op.eschool.activities.staff.settings.AttendanceSettingActivity;
import com.op.eschool.activities.staff.settings.ChangeSessionActivity;
import com.op.eschool.activities.staff.settings.ClassSettingActivity;
import com.op.eschool.activities.staff.settings.FeeSettingActivity;
import com.op.eschool.activities.staff.settings.SalarySettingActivity;
import com.op.eschool.activities.staff.settings.TimetablesSettingActivity;
import com.op.eschool.activities.staff.student.FeesCollectionActivity;
import com.op.eschool.activities.staff.student.PromotionActivity;
import com.op.eschool.activities.staff.student.StudentListActivity;
import com.op.eschool.activities.staff.teacher.SalaryDisActivity;
import com.op.eschool.activities.staff.teacher.TeacherListActivity;
import com.op.eschool.activities.timetable.ViewTimeTableActivity;
import com.op.eschool.adapters.DrawerAdaptter;
import com.op.eschool.adapters.ViewPagerAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.chatboat.ChatBoatActivity;
import com.op.eschool.databinding.ActivityPrincipleMainBinding;
import com.op.eschool.databinding.ActivityStaffMainBinding;
import com.op.eschool.fragments.HomeFragment;
import com.op.eschool.interfaces.DrawerInterface;
import com.op.eschool.models.CommonResponse;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.DrawerModel;
import com.op.eschool.models.StaffDrawerModel;
import com.op.eschool.models.login_model.LoginUserModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.util.Constants;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class PrincipleMainActivity extends BaseActivity {
    ViewPagerAdapter viewPagerAdapter ;
    List<Fragment> fragmentList = new ArrayList<>() ;
    ActivityPrincipleMainBinding binding ;
    ViewPager2 view_pager ;
    RecyclerView rv_data ;
    FloatingActionButton chat_boat ;

    TextView name ,mobile,email ;
    ImageView school_logo ;
    GlobalLoader globalLoader ;
    LoginUserModel loginUserModel ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_principle_main) ;
        globalLoader = new GlobalLoader(PrincipleMainActivity.this) ;
        loginUserModel = Utility.getLoginUser(getApplicationContext()) ;
        fragmentList.add(new HomeFragment()) ;
        viewPagerAdapter = new ViewPagerAdapter(PrincipleMainActivity.this , fragmentList) ;
        chat_boat = findViewById(R.id.chat_boat) ;
        view_pager = findViewById(R.id.view_pager) ;
        view_pager.setAdapter(viewPagerAdapter);
        view_pager.setUserInputEnabled(false);
        manageClicks() ;
        setDrawerAdpater() ;
        setCollageData() ;

    }

    private void setCollageData() {
        //loginUserModel
        try {

            name.setText("" + loginUserModel.getCollageName() + " ( "+loginUserModel.type+" )" );
            mobile.setText("" + loginUserModel.getMobileNumber());
            email.setText("" + loginUserModel.getEmailId());
            Glide.with(getApplicationContext())
                    .load(Utility.convertBase64ToBitmap(loginUserModel.getImage()))
                    .apply(new RequestOptions().placeholder(R.drawable.round_profile))
                    .into(school_logo)   ;
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void setDrawerAdpater() {
        View headers = binding.navView.getHeaderView(0);
        rv_data = headers.findViewById(R.id.rv_data);
        rv_data.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        name = headers.findViewById(R.id.name) ;
        mobile = headers.findViewById(R.id.mobile) ;
        email = headers.findViewById(R.id.email) ;
        school_logo = headers.findViewById(R.id.school_logo) ;


        headers.findViewById(R.id.profile_lin).setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext() , ProfileActivity.class)) ;
        });
        List<StaffDrawerModel> drawerList = new ArrayList<>();
        if (commonDB.getString("SELECT_ROLE").equalsIgnoreCase("Admin")){
            List<DrawerModel> schoolSection = new ArrayList<>();
            schoolSection.add(new DrawerModel(R.drawable.attendance, "All School", new Intent(getApplicationContext() , SchoolListActivity.class))) ;
            schoolSection.add(new DrawerModel(R.drawable.attendance, "Add Session", new Intent(getApplicationContext() , AddSessionsActivity.class))) ;
            drawerList.add(new StaffDrawerModel(R.drawable.attendance ,"School", schoolSection)) ;
        }

        if (commonDB.getString("SELECT_ROLE").equalsIgnoreCase("SubjectTeacher")){
            drawerList.add(new StaffDrawerModel(R.drawable.student_male ,"Dashboard", new ArrayList<>())) ;



        }


        List<DrawerModel> studentSection = new ArrayList<>();
        studentSection.add(new DrawerModel(R.drawable.attendance, "Manage Students", new Intent(getApplicationContext() , StudentListActivity.class))) ;
//        studentSection.add(new DrawerModel(R.drawable.attendance, "Admission Form", new Intent(getApplicationContext() , ClassRegisterActivity.class))) ;
        studentSection.add(new DrawerModel(R.drawable.attendance, "Student Promotion", new Intent(getApplicationContext() , PromotionActivity.class))) ;
        studentSection.add(new DrawerModel(R.drawable.attendance, "Fees Collection", new Intent(getApplicationContext() , FeesCollectionActivity.class))) ;
        drawerList.add(new StaffDrawerModel(R.drawable.student_male ,"Students", studentSection)) ;

        List<DrawerModel> teacherSection = new ArrayList<>();
        teacherSection.add(new DrawerModel(R.drawable.attendance, "Manage Staff", new Intent(getApplicationContext() , TeacherListActivity.class)));
        //teacherSection.add(new DrawerModel(R.drawable.attendance, "Teacher Detail", new Intent(getApplicationContext() , TeacherProfileActivity.class)));
        //teacherSection.add(new DrawerModel(R.drawable.attendance, "Add Teacher", new Intent(getApplicationContext() , DetailRegisterActivity.class)));
        teacherSection.add(new DrawerModel(R.drawable.attendance, "Salary", new Intent(getApplicationContext() , SalaryDisActivity.class)));
        drawerList.add(new StaffDrawerModel(R.drawable.tuition ,"Staff", teacherSection)) ;

        List<DrawerModel> parentSection = new ArrayList<>();
        parentSection.add(new DrawerModel(R.drawable.attendance, "Manage Parents", new Intent(getApplicationContext() , ParentsListActivity.class))) ;
        //parentSection.add(new DrawerModel(R.drawable.attendance, "Parents Detail", null));
        //parentSection.add(new DrawerModel(R.drawable.attendance, "Add Parents", new Intent(getApplicationContext() , DetailRegisterActivity.class))) ;
        drawerList.add(new StaffDrawerModel(R.drawable.person ,"Parents", parentSection)) ;


        List<DrawerModel> attendanceSection = new ArrayList<>();
        attendanceSection.add(new DrawerModel(R.drawable.attendance, "Staff Day Wise", new Intent(getApplicationContext() , AttendanceActivity.class)));
        attendanceSection.add(new DrawerModel(R.drawable.attendance, "Staff Month Wise", new Intent(getApplicationContext() , AttendanceMonthlyActivity.class)));
        attendanceSection.add(new DrawerModel(R.drawable.attendance, "Staff Month Name Wise", null));
        attendanceSection.add(new DrawerModel(R.drawable.attendance, "Summary Report Monthly", null));
        drawerList.add(new StaffDrawerModel(R.drawable.ic_calendar ,"Attendance", attendanceSection)) ;


        List<DrawerModel> classSection = new ArrayList<>();
        // classSection.add(new DrawerModel(R.drawable.attendance, "Manage Class Group", new Intent(getApplicationContext() , ClassGroupListActivity.class)));
        classSection.add(new DrawerModel(R.drawable.attendance, "Class List", new Intent(getApplicationContext() , ClassListActivity.class).putExtra("TYPE" , "ClsTbl")));
        classSection.add(new DrawerModel(R.drawable.attendance, "Student List", new Intent(getApplicationContext() , ClassStudentListActivity.class)));
        classSection.add(new DrawerModel(R.drawable.attendance, "Monitor", new Intent(getApplicationContext() , MonitorListActivity.class)));
        classSection.add(new DrawerModel(R.drawable.attendance, "Class Teacher", new Intent(getApplicationContext() , TeacherListActivity.class)));
        classSection.add(new DrawerModel(R.drawable.attendance, "Subject Teacher", new Intent(getApplicationContext() , SubjectTeacherActivity.class)));
        //classSection.add(new DrawerModel(R.drawable.attendance, "Add New Class",  new Intent(getApplicationContext() , AddClassActivity.class)));
        drawerList.add(new StaffDrawerModel(R.drawable.ic_class ,"Class", classSection)) ;

        List<DrawerModel> subjectSection = new ArrayList<>();
        subjectSection.add(new DrawerModel(R.drawable.attendance, "Manage Subject", null));
        subjectSection.add(new DrawerModel(R.drawable.attendance, "Add New Subject", null));
        drawerList.add(new StaffDrawerModel(R.drawable.ic_subject ,"Subject", subjectSection)) ;


        List<DrawerModel> timetableSection = new ArrayList<>();
        timetableSection.add(new DrawerModel(R.drawable.attendance, "View Time Table", new Intent(getApplicationContext() , ViewTimeTableActivity.class))) ;
        timetableSection.add(new DrawerModel(R.drawable.attendance, "Manage Sub Subject", null));
        drawerList.add(new StaffDrawerModel(R.drawable.ic_timetable ,"Time Table", timetableSection)) ;

        List<DrawerModel> workSection = new ArrayList<>();
        workSection.add(new DrawerModel(R.drawable.attendance, "Class Work", null));
        workSection.add(new DrawerModel(R.drawable.attendance, "Home Work", null));
        drawerList.add(new StaffDrawerModel(R.drawable.ic_work ,"Work", workSection)) ;

        List<DrawerModel> ledgerSection = new ArrayList<>();
        ledgerSection.add(new DrawerModel(R.drawable.attendance, "Income", null));
        ledgerSection.add(new DrawerModel(R.drawable.attendance, "Expenses", null));
        drawerList.add(new StaffDrawerModel(R.drawable.attendance ,"Ledger", ledgerSection)) ;

        List<DrawerModel> leaveSection = new ArrayList<>();
        leaveSection.add(new DrawerModel(R.drawable.attendance, "Student", new Intent(getApplicationContext() , LeaveListActivity.class)));
        leaveSection.add(new DrawerModel(R.drawable.attendance, "Staff", new Intent(getApplicationContext() , LeaveListActivity.class)));
        leaveSection.add(new DrawerModel(R.drawable.attendance, "Self", new Intent(getApplicationContext() , LeaveListActivity.class)));
        drawerList.add(new StaffDrawerModel(R.drawable.ic_leave ,"Leave", leaveSection)) ;

        List<DrawerModel> complaintsSection = new ArrayList<>();
        complaintsSection.add(new DrawerModel(R.drawable.attendance, "Manage Complaints", new Intent(getApplicationContext() , ComplaintsActivity.class))) ;
        drawerList.add(new StaffDrawerModel(R.drawable.ic_feedback ,"Complaints", complaintsSection)) ;

        List<DrawerModel> transportSection = new ArrayList<>();
        transportSection.add(new DrawerModel(R.drawable.attendance, "All Buses", null));
        transportSection.add(new DrawerModel(R.drawable.attendance, "Add New Bus", null));
        transportSection.add(new DrawerModel(R.drawable.attendance, "All Drivers", null));
        transportSection.add(new DrawerModel(R.drawable.attendance, "Add New Driver", null));
        transportSection.add(new DrawerModel(R.drawable.attendance, "All Routes", null));
        transportSection.add(new DrawerModel(R.drawable.attendance, "Add New Route", null));
        drawerList.add(new StaffDrawerModel(R.drawable.ic_transport ,"Transport", transportSection)) ;

        List<DrawerModel> examSection = new ArrayList<>();
        examSection.add(new DrawerModel(R.drawable.attendance, "Exam Schdule", null));
        examSection.add(new DrawerModel(R.drawable.attendance, "Exam Grades", null));
        drawerList.add(new StaffDrawerModel(R.drawable.ic_exam ,"Exam", examSection)) ;

        List<DrawerModel> documentSection = new ArrayList<>();
        documentSection.add(new DrawerModel(R.drawable.attendance, "Manage Document", null));
        drawerList.add(new StaffDrawerModel(R.drawable.ic_documents ,"Document", documentSection)) ;

        List<DrawerModel> librarySection = new ArrayList<>();
        librarySection.add(new DrawerModel(R.drawable.attendance, "All Books", null));
        librarySection.add(new DrawerModel(R.drawable.attendance, "Add New Book", null));
        drawerList.add(new StaffDrawerModel(R.drawable.ic_library ,"Library", librarySection)) ;

        List<DrawerModel> hostelSection = new ArrayList<>();
        hostelSection.add(new DrawerModel(R.drawable.attendance, "Manage Hostel", null));
        drawerList.add(new StaffDrawerModel(R.drawable.ic_hostel ,"Hostel", hostelSection)) ;


//        List<DrawerModel> accountSection = new ArrayList<>();
//        accountSection.add(new DrawerModel(R.drawable.attendance, "All Fee Collection", null));
//        accountSection.add(new DrawerModel(R.drawable.attendance, "Expenses", null));
//        accountSection.add(new DrawerModel(R.drawable.attendance, "Add Expenses", null));
//        drawerList.add(new StaffDrawerModel(R.drawable.attendance ,"Account", accountSection)) ;
//
//
//
//        List<DrawerModel> routineSection = new ArrayList<>();
//        routineSection.add(new DrawerModel(R.drawable.attendance, "All Class Routine", null));
//        routineSection.add(new DrawerModel(R.drawable.attendance, "Add New Class Routine", null));
//        drawerList.add(new StaffDrawerModel(R.drawable.attendance ,"Routine", routineSection)) ;
//



        List<DrawerModel> noticeSection = new ArrayList<>();
        noticeSection.add(new DrawerModel(R.drawable.attendance, "All Notices", null));
        noticeSection.add(new DrawerModel(R.drawable.attendance, "Add New Notice", new Intent(getApplicationContext() , CreateNotesActivity.class)));
        drawerList.add(new StaffDrawerModel(R.drawable.ic_notice ,"Notice", noticeSection)) ;


        List<DrawerModel> settingSection = new ArrayList<>();
        settingSection.add(new DrawerModel(R.drawable.attendance, "Class", new Intent(getApplicationContext() , ClassSettingActivity.class))) ;
        settingSection.add(new DrawerModel(R.drawable.attendance, "Time Table", new Intent(getApplicationContext() , TimetablesSettingActivity.class))) ;
        settingSection.add(new DrawerModel(R.drawable.attendance, "Attendance", new Intent(getApplicationContext() , AttendanceSettingActivity.class))) ;
        settingSection.add(new DrawerModel(R.drawable.attendance, "Fee", new Intent(getApplicationContext() , FeeSettingActivity.class))) ;
        settingSection.add(new DrawerModel(R.drawable.attendance, "Salary", new Intent(getApplicationContext() , SalarySettingActivity.class))) ;
        settingSection.add(new DrawerModel(R.drawable.attendance, "Change Session", new Intent(getApplicationContext() , ChangeSessionActivity.class))) ;
        drawerList.add(new StaffDrawerModel(R.drawable.ic_settings ,"Setting", settingSection)) ;

        drawerList.add(new StaffDrawerModel(R.drawable.ic_logout ,"Logout", new ArrayList<>())) ;


        DrawerAdaptter drawerAdaptter = new DrawerAdaptter(drawerList, getApplicationContext(), new DrawerInterface() {
            @Override
            public void onItemClicked(DrawerModel model) {
                binding.drawerLayout.closeDrawers() ;
                if (model.intent != null){
                    startActivity(model.intent);
                }
            }

            @Override
            public void onMainItemClicked(String type) {
                binding.drawerLayout.closeDrawers() ;
                logoutDialog() ;
            }
        }) ;
        rv_data.setAdapter(drawerAdaptter);
    }

    private void logoutDialog() {
        DialogModel dialogModel = new DialogModel(PrincipleMainActivity.this ,"Logout","Want to sure to logout ?","Yeah, sure","Cancel", type->{
            if (type.equalsIgnoreCase("POSTIVE")){
                Utility.userLogout(getApplicationContext());
            }
        } ) ;
        Utility.wantTOSureDialog(dialogModel) ;
    }

    private void manageClicks() {
        chat_boat.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext() , ChatBoatActivity.class)) ;
        });
        findViewById(R.id.iv_menu).setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(Gravity.LEFT);
        });
    }
    private void GetCollageDetail(String res) {
        try {
            ArrayList<SchoolModel> list = (ArrayList<SchoolModel>) fromJson(res,
                    new TypeToken<ArrayList<SchoolModel>>() {
                    }.getType());
            SchoolModel schoolModel = list.get(0) ;
            commonDB.putString("SCHOOL_DETAILS" , new Gson().toJson(schoolModel)) ;
            CommonResponse commonResponse = new Gson().fromJson(commonDB.getString(Constants.LOGIN_RESPONSE) , CommonResponse.class) ;

            name.setText("" + schoolModel.getCollageName() + " ( "+commonResponse.Type+" )" );
            mobile.setText("" + schoolModel.getMobileNumber());
            email.setText("" + schoolModel.getEmailId());
            Glide.with(getApplicationContext())
                    .load(Utility.convertBase64ToBitmap(schoolModel.getImage()))
                    .apply(new RequestOptions().placeholder(R.drawable.round_profile))
                    .into(school_logo)   ;
        }catch (Exception e){e.printStackTrace();}

    }
}