package com.op.eschool.adapters.time_table;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.ViewTimeTableAdapterItemBinding;
import com.op.eschool.interfaces.TimetableInterface;
import com.op.eschool.models.timetable_model.AllTimetableModel;
import com.op.eschool.util.FLog;

import java.util.Arrays;

public class ViewTimetableAdapter  extends RecyclerView.Adapter<ViewTimetableAdapter.Holder> {
    Context context ;
    AllTimetableModel allTimetableModel ;
    TimetableInterface commoTypenInterface ;
    int selectPosition ;
    public ViewTimetableAdapter(int selectPosition , Context context, AllTimetableModel allTimetableModel , TimetableInterface commoTypenInterface) {
        this.selectPosition = selectPosition;
        this.commoTypenInterface = commoTypenInterface;
        this.context = context;
        this.allTimetableModel = allTimetableModel;
    }
    @NonNull
    @Override
    public ViewTimetableAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewTimeTableAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.view_time_table_adapter_item, parent, false);
        return new ViewTimetableAdapter.Holder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewTimetableAdapter.Holder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.setModel(allTimetableModel.tableModelList.get(position)) ;
        holder.itemView.setOnClickListener(v->{
            selectPosition = position ;
            commoTypenInterface.onSpinnerChange(position ,"" , null);}) ;
        if (selectPosition == position){
            holder.binding.linRoot.setBackground(context.getDrawable(R.drawable.squre_stroke_bg_filled));
            holder.binding.txtPeriod.setTextColor(context.getColor(R.color.white)) ;
        }else{
            holder.binding.linRoot.setBackground(context.getDrawable(R.drawable.squre_stroke_bg));
            holder.binding.txtPeriod.setTextColor(context.getColor(R.color.black)) ;
        }


    }
    @Override
    public int getItemCount() {
        return allTimetableModel.tableModelList.size() ;
    }
    class Holder extends RecyclerView.ViewHolder {
        ViewTimeTableAdapterItemBinding binding ;
        public Holder(@NonNull ViewTimeTableAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}