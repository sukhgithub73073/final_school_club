package com.op.eschool.adapters.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.op.eschool.R;
import com.op.eschool.databinding.SubjectAttendanceAdapterItemBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.DashboardModel;
import com.op.eschool.models.attendance.SubjectAttendanceModel;

import java.util.ArrayList;
import java.util.List;

public class SubjectAttendanceAdapter extends RecyclerView.Adapter<SubjectAttendanceAdapter.Holder> {
    List<SubjectAttendanceModel> list = new ArrayList<>() ;
    Context context ;
    CommonInterface commonInterface ;

    public SubjectAttendanceAdapter(List<SubjectAttendanceModel> list, Context context , CommonInterface commonInterface) {
        this.list = list;
        this.context = context;
        this.commonInterface = commonInterface;
    }

    @NonNull
    @Override
    public SubjectAttendanceAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SubjectAttendanceAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.subject_attendance_adapter_item, parent, false);

        return new SubjectAttendanceAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAttendanceAdapter.Holder holder, int position) {
        holder.itemRowBinding.setModel(list.get(position)) ;

        holder.itemView.setOnClickListener(v->{
            commonInterface.onItemClicked(position) ;
        });
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        SubjectAttendanceAdapterItemBinding itemRowBinding ;
        public Holder(@NonNull SubjectAttendanceAdapterItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }
    }
}

