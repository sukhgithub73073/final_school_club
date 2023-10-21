package com.op.eschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.op.eschool.R;

import com.op.eschool.databinding.ClassTeacherAdapterItemBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.models.staff.TeacherModel;
import com.op.eschool.models.staff.TeacherModel;

import java.util.ArrayList;
import java.util.List;

public class ClassTeacherAdapter extends RecyclerView.Adapter<ClassTeacherAdapter.Holder> {
    Context context ;
    List<TeacherModel> list = new ArrayList<>() ;
    CommoTypenInterface commoTypenInterface ;
    public ClassTeacherAdapter(Context context, List<TeacherModel> list, CommoTypenInterface commoTypenInterface) {
        this.context = context;
        this.list = list;
        this.commoTypenInterface = commoTypenInterface;
    }
    @NonNull
    @Override
    public ClassTeacherAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ClassTeacherAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.class_teacher_adapter_item, parent, false);
        return new ClassTeacherAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassTeacherAdapter.Holder holder, int position) {
        holder.binding.setModel(list.get(position)) ;
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        ClassTeacherAdapterItemBinding binding ;
        public Holder(@NonNull ClassTeacherAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
