package com.op.eschool.fragments.student;

import static com.op.eschool.base.BaseActivity.bindingStudentOne;
import static com.op.eschool.base.BaseActivity.bindingStudentThree;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.op.eschool.R;
import com.op.eschool.interfaces.CollegeInterface;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentThreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentThreeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    CollegeInterface collegeInterface;
    public StudentThreeFragment( CollegeInterface collegeInterface) {
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
        bindingStudentThree = DataBindingUtil.inflate(inflater , R.layout.fragment_student_three, container, false);
        collegeInterface.onRegisterClicked();
        return bindingStudentThree.getRoot() ;
    }
}