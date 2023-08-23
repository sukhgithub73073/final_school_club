package com.op.eschool.fragments;

import static com.op.eschool.base.BaseActivity.commonDB;

import static com.op.eschool.base.BaseActivity.webSocketManager;
import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;
import static com.op.eschool.util.Utility.fromJson;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.op.eschool.R;
import com.op.eschool.activities.staff.StaffMainActivity;
import com.op.eschool.activities.staff.class_section.ClassGroupListActivity;
import com.op.eschool.activities.staff.class_section.ClassListActivity;
import com.op.eschool.activities.staff.school.SchoolListActivity;
import com.op.eschool.activities.staff.student.StudentListActivity;
import com.op.eschool.activities.staff.teacher.TeacherListActivity;
import com.op.eschool.adapters.DashboardAdapter;
import com.op.eschool.adapters.StaffDashboardAdapter;
import com.op.eschool.databinding.FragmentHomeBinding;
import com.op.eschool.models.DashboardModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.util.FLog;
import com.op.eschool.util.FToast;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentHomeBinding binding ;
    List<DashboardModel> list = new ArrayList<>() ;
    String alertText ="" ;
    String GetCollageDetail ="" ;


    private String mParam1;
    private String mParam2;
    String key="" ;
    public HomeFragment() {

    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_home, container, false);
        initADapters() ;
        manageSchoolResponse(commonDB.getString("GetCollageDetail")) ;
        return binding.getRoot() ;
    }
    public void setTickerAnimation(View view) {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, +1f,
                Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(10000);
        view.startAnimation(animation);
    }

    private void initADapters() {

        if (commonDB.getString("SELECT_ROLE").equalsIgnoreCase("Admin")){list.add(new DashboardModel("School\n(--)" ,R.drawable.attendance, new Intent(getContext() , SchoolListActivity.class))); }

        list.add(new DashboardModel("Staff\n(300)" ,R.drawable.tuition, new Intent(getContext() , TeacherListActivity.class)));
        list.add(new DashboardModel("Present Staff\n(280)" ,R.drawable.tuition, new Intent(getContext() , TeacherListActivity.class)));
        list.add(new DashboardModel("Absent Staff\n(10)" ,R.drawable.tuition, new Intent(getContext() , TeacherListActivity.class)));

        list.add(new DashboardModel("Student\n(500)" ,R.drawable.student_male, new Intent(getContext() , StudentListActivity.class))) ;
        list.add(new DashboardModel("Present Student\n(400)" ,R.drawable.student_male, new Intent(getContext() , StudentListActivity.class))) ;
        list.add(new DashboardModel("Absent Student\n(100)" ,R.drawable.student_male, new Intent(getContext() , StudentListActivity.class))) ;


        list.add(new DashboardModel("Class Groups\n(--)" ,R.drawable.homework, new Intent(getContext() , ClassGroupListActivity.class)));
        list.add(new DashboardModel("Classes\n(--)" ,R.drawable.homework, new Intent(getContext() , ClassListActivity.class).putExtra("TYPE" , "ClsTbl")));
        list.add(new DashboardModel("Subject\n(--)" ,R.drawable.exam, null));

        binding.setDashboardAdapter(new StaffDashboardAdapter(list, getContext() , pos->{
            if (list.get(pos).intent==null){
                FToast.makeText(getContext(), "Comming Soon...", FToast.LENGTH_SHORT).show();
            }else{
                startActivity(list.get(pos).intent) ;
            }
        })) ;

    }
    private void manageSchoolResponse(String res) {
        try {
            ArrayList<SchoolModel> list = (ArrayList<SchoolModel>) fromJson(res,
                    new TypeToken<ArrayList<SchoolModel>>() {
                    }.getType());
            SchoolModel schoolModel = list.get(0) ;
            alertText =  schoolModel.getCollageName() ;

            Glide.with(getContext())
                    .load(Utility.convertBase64ToBitmap(schoolModel.getImage()))
                    .apply(new RequestOptions().placeholder(R.drawable.round_profile))
                    .into(binding.schoolLogo)   ;

            binding.txtMarque.setText(Html.fromHtml(alertText ,Html.FROM_HTML_MODE_LEGACY)) ;
            binding.txtMarque.setSelected(true);
            binding.txtMarque.requestFocus();
            if (alertText.length() < 60) {
                setTickerAnimation( binding.txtMarque);
            }
        }catch (Exception e){e.printStackTrace();}


    }


}