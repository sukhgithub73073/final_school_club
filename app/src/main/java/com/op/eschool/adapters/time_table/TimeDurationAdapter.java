package com.op.eschool.adapters.time_table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.TimeDurationAdapterItemBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.models.student.StudentModel;
import com.op.eschool.models.timetable_model.TimeDurationModel;

import java.util.ArrayList;
import java.util.List;

public class TimeDurationAdapter extends RecyclerView.Adapter<TimeDurationAdapter.Holder> {
    Context context ;
    List<TimeDurationModel> list = new ArrayList<>() ;
    CommoTypenInterface commoTypenInterface ;
    public TimeDurationAdapter(Context context, List<TimeDurationModel> list , CommoTypenInterface commoTypenInterface) {
        this.commoTypenInterface = commoTypenInterface;
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public TimeDurationAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TimeDurationAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.time_duration_adapter_item, parent, false);
        return new TimeDurationAdapter.Holder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull TimeDurationAdapter.Holder holder, int position) {
        holder.binding.setModel(list.get(position)) ;

    }
    @Override
    public int getItemCount() {
        return list.size() ;
    }
    class Holder extends RecyclerView.ViewHolder {
        TimeDurationAdapterItemBinding binding ;
        public Holder(@NonNull TimeDurationAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}