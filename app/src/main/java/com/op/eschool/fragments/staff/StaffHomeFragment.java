package com.op.eschool.fragments.staff;

import static com.op.eschool.base.BaseActivity.commonDB;
import static com.op.eschool.base.BaseActivity.loginUserModel;
import static com.op.eschool.base.BaseActivity.webSocketManager;
import static com.op.eschool.util.Utility.fromJson;
import android.content.Intent;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.op.eschool.R;
import com.op.eschool.activities.staff.student.SignUpStudentActivity;
import com.op.eschool.activities.staff.student.StudentListActivity;
import com.op.eschool.activities.student.attendance.SubjectAttendanceActivity;
import com.op.eschool.adapters.DashboardAdapter;
import com.op.eschool.databinding.FragmentStaffHomeBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.DashboardModel;
import com.op.eschool.models.school_models.SchoolModel;
import com.op.eschool.util.FToast;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffHomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentStaffHomeBinding binding ;
    List<DashboardModel> list = new ArrayList<>() ;
    String alertText ="" ;


    private String mParam1;
    private String mParam2;
    String key="" ;
    public StaffHomeFragment() {

    }


    public static StaffHomeFragment newInstance(String param1, String param2) {
        StaffHomeFragment fragment = new StaffHomeFragment();
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
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_staff_home, container, false);
        initADapters() ;
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.round_profile)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(getContext())
                .load(Utility.convertBase64ToBitmap(loginUserModel.image))
                .apply(requestOptions)
                .into(binding.schoolLogo) ;

        manageSchoolResponse() ;
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

        list.add(new DashboardModel("Students" ,R.drawable.attendance, new Intent(getContext() , StudentListActivity.class)));
        list.add(new DashboardModel("Homework" ,R.drawable.homework, null));
        list.add(new DashboardModel("Result" ,R.drawable.exam, null));
        list.add(new DashboardModel("Exam Route" ,R.drawable.todo_list, null));
        list.add(new DashboardModel("Solution" ,R.drawable.idea_sharing, null));
        list.add(new DashboardModel("Notice & Events" ,R.drawable.questions, null));
        list.add(new DashboardModel("Add Student" ,R.drawable.add_user_male, new Intent(getContext() , SignUpStudentActivity.class).putExtra("TYPE" ,"ADD_STUDENT")));
        binding.setDashboardAdapter(new DashboardAdapter(list, getContext() , pos->{
            if (list.get(pos).intent==null){
                FToast.makeText(getContext(), "Comming Soon...", FToast.LENGTH_SHORT).show();
            } else if (list.get(pos).title.equalsIgnoreCase("Add Student")){
                commonDB.putString("STUDENT_DETAIL" ,"") ;
                GetCollageDetail(Utility.getLoginUser(getContext()).collageId , pppp->{
                    startActivity(list.get(pos).intent) ;
                });
            }else{
                startActivity(list.get(pos).intent) ;
            }
        })) ;


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
                getActivity().runOnUiThread(()->{
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


    private void manageSchoolResponse() {

        try {
            alertText =  loginUserModel.getCollageName() ;

            Glide.with(getContext())
                    .load(Utility.convertBase64ToBitmap(loginUserModel.getImage()))
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