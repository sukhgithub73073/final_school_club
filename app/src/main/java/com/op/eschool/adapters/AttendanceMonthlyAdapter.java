package com.op.eschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.AttendanceAdapterItemBinding;
import com.op.eschool.databinding.AttendanceMonthlyAdapterItemBinding;

import java.util.ArrayList;
import java.util.List;

public class AttendanceMonthlyAdapter extends RecyclerView.Adapter<AttendanceMonthlyAdapter.Holder> {
    Context context ;
    List<Integer> datesList = new ArrayList<>() ;

    public AttendanceMonthlyAdapter(Context context, List<Integer> datesList) {
        this.context = context;
        this.datesList = datesList;
    }

    @NonNull
    @Override
    public AttendanceMonthlyAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        AttendanceMonthlyAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.attendance_monthly_adapter_item, parent, false);

        return new AttendanceMonthlyAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceMonthlyAdapter.Holder holder, int position) {
       for (Integer integer : datesList){
           CheckBox checkBox = new CheckBox(context) ;
           checkBox.setText(integer+"");
           checkBox.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
           holder.itemRowBinding.linDate.addView(checkBox) ;

       }
    }

    @Override
    public int getItemCount() {
        return 50 ;
    }

    class Holder extends RecyclerView.ViewHolder {
        AttendanceMonthlyAdapterItemBinding itemRowBinding ;
        public Holder(@NonNull AttendanceMonthlyAdapterItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }
    }
}
