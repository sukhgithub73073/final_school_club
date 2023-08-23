package com.op.eschool.activities;

import static com.op.eschool.util.Utility.fromJson;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

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
import com.op.eschool.activities.staff.ProfileActivity;
import com.op.eschool.adapters.DashboardAdapter;
import com.op.eschool.adapters.DrawerAdaptter;
import com.op.eschool.adapters.ViewPagerAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.chatboat.ChatBoatActivity;
import com.op.eschool.databinding.ActivityStaffMainBinding;
import com.op.eschool.databinding.ActivityTeacherMainBinding;
import com.op.eschool.fragments.HomeFragment;
import com.op.eschool.fragments.student.StudentHomeFragment;
import com.op.eschool.interfaces.DrawerInterface;
import com.op.eschool.models.DashboardModel;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.DrawerModel;
import com.op.eschool.models.StaffDrawerModel;
import com.op.eschool.models.login_model.LoginUserModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.util.FToast;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    ActivityTeacherMainBinding binding ;
    ViewPagerAdapter viewPagerAdapter ;
    List<Fragment> fragmentList = new ArrayList<>() ;
    ViewPager2 view_pager ;
    RecyclerView rv_data ;
    FloatingActionButton chat_boat ;

    TextView name ,mobile,email ;
    ImageView school_logo ;
    GlobalLoader globalLoader ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this , R.layout.activity_teacher_main);
        globalLoader = new GlobalLoader(MainActivity.this) ;
        fragmentList.add(new StudentHomeFragment()) ;
        viewPagerAdapter = new ViewPagerAdapter(MainActivity.this , fragmentList) ;
        chat_boat = findViewById(R.id.chat_boat) ;
        view_pager = findViewById(R.id.view_pager) ;
        view_pager.setAdapter(viewPagerAdapter);
        view_pager.setUserInputEnabled(false);
        manageClicks() ;
        setDrawerAdpater() ;
        GetCollageDetail() ;

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

        drawerList.add(new StaffDrawerModel(R.drawable.student_male ,"Dashboard", new ArrayList<>())) ;
        drawerList.add(new StaffDrawerModel(R.drawable.tuition ,"Staff", new ArrayList<>())) ;
        drawerList.add(new StaffDrawerModel(R.drawable.ic_calendar ,"Attendance", new ArrayList<>())) ;
        drawerList.add(new StaffDrawerModel(R.drawable.ic_timetable ,"Time Table", new ArrayList<>())) ;

        drawerList.add(new StaffDrawerModel(R.drawable.ic_work ,"Class Work", new ArrayList<>())) ;
        drawerList.add(new StaffDrawerModel(R.drawable.ic_work ,"Home Work", new ArrayList<>())) ;
        drawerList.add(new StaffDrawerModel(R.drawable.attendance ,"Documents", new ArrayList<>())) ;
        drawerList.add(new StaffDrawerModel(R.drawable.attendance ,"Achivment", new ArrayList<>())) ;
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
                if (type.equalsIgnoreCase("0")){
                } else if (type.equalsIgnoreCase("Logout")) {
                    logoutDialog() ;
                }

            }
        }) ;
        rv_data.setAdapter(drawerAdaptter);
    }

    private void logoutDialog() {
        DialogModel dialogModel = new DialogModel(MainActivity.this ,"Logout","Want to sure to logout ?","Yeah, sure","Cancel", type->{
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
    private void GetCollageDetail() {
        try {

            name.setText("" + loginUserModel.getFullName() + " ( "+loginUserModel.type+" )" );
            mobile.setText("" + loginUserModel.getUserMobileNumber());
            email.setText("" + loginUserModel.getUserEmailId());
            Glide.with(getApplicationContext())
                    .load(Utility.convertBase64ToBitmap(loginUserModel.getUserImage()))
                    .apply(new RequestOptions().placeholder(R.drawable.round_profile))
                    .into(school_logo)   ;
        }catch (Exception e){e.printStackTrace();}




    }
}