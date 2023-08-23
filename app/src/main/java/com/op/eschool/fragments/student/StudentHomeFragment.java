package com.op.eschool.fragments.student;

import static com.op.eschool.base.BaseActivity.loginUserModel;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.op.eschool.R;
import com.op.eschool.activities.student.attendance.SubjectAttendanceActivity;
import com.op.eschool.adapters.DashboardAdapter;
import com.op.eschool.databinding.FragmentStudentHomeBinding;
import com.op.eschool.models.DashboardModel;
import com.op.eschool.util.FToast;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class StudentHomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<DashboardModel> list = new ArrayList<>() ;
    FragmentStudentHomeBinding binding ;
    private String mParam1;
    private String mParam2;

    public StudentHomeFragment() {
    }

    public static StudentHomeFragment newInstance(String param1, String param2) {
        StudentHomeFragment fragment = new StudentHomeFragment();
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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_student_home, container, false);

        Glide.with(getContext())
                .load(Utility.convertBase64ToBitmap(loginUserModel.image))
                .apply(new RequestOptions().placeholder(R.drawable.round_profile))
                .into(binding.schoolLogo)   ;
        list.add(new DashboardModel("Attendance" ,R.drawable.attendance, new Intent(getContext() , SubjectAttendanceActivity.class)));
        list.add(new DashboardModel("Homework" ,R.drawable.homework, null));
        list.add(new DashboardModel("Result" ,R.drawable.exam, null));
        list.add(new DashboardModel("Exam Route" ,R.drawable.todo_list, null));
        list.add(new DashboardModel("Solution" ,R.drawable.idea_sharing, null));
        list.add(new DashboardModel("Notice & Events" ,R.drawable.questions, null));
        list.add(new DashboardModel("Add Account" ,R.drawable.add_user_male, null));
        binding.setDashboardAdapter(new DashboardAdapter(list, getContext() , pos->{
            if (list.get(pos).intent==null){
                FToast.makeText(getContext(), "Comming Soon...", FToast.LENGTH_SHORT).show();
            }else{
                startActivity(list.get(pos).intent) ;
            }
        })) ;

        return binding.getRoot() ;


    }
}