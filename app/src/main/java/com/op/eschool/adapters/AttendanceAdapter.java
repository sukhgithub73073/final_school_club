package com.op.eschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.op.eschool.R;
import com.op.eschool.databinding.AttendanceAdapterItemBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.DashboardModel;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.Holder> {
    public AttendanceAdapter() {
    }

    @NonNull
    @Override
    public AttendanceAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        AttendanceAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.attendance_adapter_item, parent, false);

        return new AttendanceAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 50 ;
    }

    class Holder extends RecyclerView.ViewHolder {
        AttendanceAdapterItemBinding itemRowBinding ;
        public Holder(@NonNull AttendanceAdapterItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }
    }
}
