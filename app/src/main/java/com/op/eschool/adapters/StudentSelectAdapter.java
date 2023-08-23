package com.op.eschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.StudentSelectAdapterItemBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.models.student.StudentModel;

import java.util.ArrayList;
import java.util.List;

public class StudentSelectAdapter extends RecyclerView.Adapter<StudentSelectAdapter.Holder> {
    Context context ;
    List<StudentModel> list = new ArrayList<>() ;
    CommoTypenInterface commoTypenInterface ;
    public StudentSelectAdapter(Context context, List<StudentModel> list , CommoTypenInterface commoTypenInterface) {
        this.commoTypenInterface = commoTypenInterface;
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public StudentSelectAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StudentSelectAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.student_select_adapter_item, parent, false);
        return new StudentSelectAdapter.Holder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull StudentSelectAdapter.Holder holder, int position) {
        holder.binding.setModel(list.get(position)) ;
        holder.binding.checkBox.setOnCheckedChangeListener((f,b)->{
           list.get(position).Checked = b ;
        });


    }
    @Override
    public int getItemCount() {
        return list.size() ;
    }
    class Holder extends RecyclerView.ViewHolder {
        StudentSelectAdapterItemBinding binding ;
        public Holder(@NonNull StudentSelectAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
