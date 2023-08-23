package com.op.eschool.fragments.staff;

import static com.op.eschool.base.BaseActivity.bindingStaffTwo;

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
 * Use the {@link StaffTwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffTwoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CollegeInterface collegeInterface;
    public StaffTwoFragment( CollegeInterface collegeInterface) {
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
        bindingStaffTwo = DataBindingUtil.inflate(inflater ,R.layout.fragment_staff_two, container, false);
        collegeInterface.onRegisterClicked() ;
        return bindingStaffTwo.getRoot() ;
    }
}