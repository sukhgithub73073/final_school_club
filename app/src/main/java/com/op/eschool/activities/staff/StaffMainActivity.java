package com.op.eschool.activities.staff;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.op.eschool.R;
import com.op.eschool.activities.staff.student.StudentListActivity;
import com.op.eschool.adapters.DrawerAdaptter;
import com.op.eschool.adapters.ViewPagerAdapter;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.chatboat.ChatBoatActivity;
import com.op.eschool.databinding.ActivityStaffMainBinding;
import com.op.eschool.fragments.staff.StaffHomeFragment;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.interfaces.DrawerInterface;
import com.op.eschool.models.DialogModel;
import com.op.eschool.models.DrawerModel;
import com.op.eschool.models.StaffDrawerModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.util.GlobalLoader;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffMainActivity extends BaseActivity {
    ViewPagerAdapter viewPagerAdapter ;
    List<Fragment> fragmentList = new ArrayList<>() ;
    ActivityStaffMainBinding binding ;
    ViewPager2 view_pager ;
    RecyclerView rv_data ;
    FloatingActionButton chat_boat ;

    TextView name ,mobile,email ;
    ImageView school_logo ;
    GlobalLoader globalLoader ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_staff_main) ;
        globalLoader = new GlobalLoader(StaffMainActivity.this) ;

        fragmentList.add(new StaffHomeFragment()) ;
        viewPagerAdapter = new ViewPagerAdapter(StaffMainActivity.this , fragmentList) ;
        chat_boat = findViewById(R.id.chat_boat) ;
        view_pager = findViewById(R.id.view_pager) ;
        view_pager.setAdapter(viewPagerAdapter);
        view_pager.setUserInputEnabled(false);
        manageClicks() ;
        setDrawerAdpater() ;
        setCollageData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    void GetCollageDetail(String CollageId , CommonInterface commonInterface){
        if (!commonDB.getString("GetCollageDetail").equalsIgnoreCase("")){
            commonInterface.onItemClicked(0) ;
        }else {
            Map<String , String> map = new HashMap<>() ;
            map.put("type" , "GetCollageDetail") ;
            map.put("CollageId" ,CollageId) ;
            String json = new Gson().toJson(map) ;
            webSocketManager.sendMessage(map , res->{
                runOnUiThread(()->{
                    try {
                        commonDB.putString("GetCollageDetail" , res) ;
                        commonInterface.onItemClicked(0) ;
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                });
            }) ;
        }

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

        drawerList.add(new StaffDrawerModel(false,R.drawable.student_male ,"Dashboard", new ArrayList<>())) ;
        drawerList.add(new StaffDrawerModel(false,R.drawable.tuition ,"Student", new ArrayList<>())) ;

        drawerList.add(new StaffDrawerModel(false,R.drawable.ic_calendar ,"Attendance", new ArrayList<>())) ;
        drawerList.add(new StaffDrawerModel(false,R.drawable.ic_timetable ,"Time Table", new ArrayList<>())) ;

        drawerList.add(new StaffDrawerModel(false,R.drawable.ic_work ,"Class Work", new ArrayList<>())) ;
        drawerList.add(new StaffDrawerModel(false,R.drawable.ic_work ,"Home Work", new ArrayList<>())) ;
        drawerList.add(new StaffDrawerModel(false,R.drawable.attendance ,"Fee", new ArrayList<>())) ;
        drawerList.add(new StaffDrawerModel(false,R.drawable.attendance ,"Documents", new ArrayList<>())) ;
        drawerList.add(new StaffDrawerModel(false,R.drawable.ic_leave ,"Leave", new ArrayList<>())) ;
        drawerList.add(new StaffDrawerModel(false,R.drawable.ic_feedback ,"Complaints", new ArrayList<>())) ;
        drawerList.add(new StaffDrawerModel(false,R.drawable.ic_logout ,"Logout", new ArrayList<>())) ;

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

                if (type.equalsIgnoreCase("Logout")){
                    logoutDialog() ;
                }else if (type.equalsIgnoreCase("Student")){
                    GetCollageDetail(loginUserModel.collageId ,pos -> {
                        startActivity(new Intent(getApplicationContext() ,StudentListActivity.class));
                    });

                }

            }
        }) ;
        rv_data.setAdapter(drawerAdaptter);
    }

    private void logoutDialog() {
        DialogModel dialogModel = new DialogModel(StaffMainActivity.this ,"Logout","Want to sure to logout ?","Yeah, sure","Cancel",type->{
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
    private void setCollageData() {
        //loginUserModel
        try {
            name.setText("" + loginUserModel.getFullName() + " ( "+loginUserModel.type+" )" );
            mobile.setText("" + loginUserModel.getUserMobileNumber());
            email.setText("" + loginUserModel.getUserEmailId());
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.round_profile)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(getApplicationContext())
                    .load(Utility.convertBase64ToBitmap(loginUserModel.getUserImage()))
                    .apply(requestOptions)
                    .into(school_logo);

        }catch (Exception e){e.printStackTrace();}
    }
}