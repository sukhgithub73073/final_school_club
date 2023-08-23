package com.op.eschool.fragments.college;

import static com.op.eschool.base.BaseActivity.bindingTwo;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.op.eschool.R;
import com.op.eschool.databinding.FragmentCollegeTwoBinding;
import com.op.eschool.interfaces.CollegeInterface;
import com.op.eschool.util.FToast;
import com.op.eschool.util.Utility;

public class CollegeTwoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    CollegeInterface collegeInterface ;

    public CollegeTwoFragment(CollegeInterface collegeInterface) {
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
        bindingTwo = DataBindingUtil.inflate(inflater , R.layout.fragment_college_two, container, false) ;
        collegeInterface.onRegisterClicked();
        return bindingTwo.getRoot() ;
    }


}