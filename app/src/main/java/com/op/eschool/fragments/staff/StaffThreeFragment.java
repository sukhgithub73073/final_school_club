package com.op.eschool.fragments.staff;

import static com.op.eschool.base.BaseActivity.bindingStaffThree;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.op.eschool.R;
import com.op.eschool.interfaces.CollegeInterface;

public class StaffThreeFragment extends Fragment {
  private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    CollegeInterface collegeInterface;
    public StaffThreeFragment(CollegeInterface collegeInterface) {
        this.collegeInterface = collegeInterface ;
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
        bindingStaffThree = DataBindingUtil.inflate(inflater ,R.layout.fragment_staff_three, container, false);
        collegeInterface.onRegisterClicked() ;
        return bindingStaffThree.getRoot() ;
    }
}