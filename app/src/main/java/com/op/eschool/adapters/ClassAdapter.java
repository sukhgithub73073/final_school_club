package com.op.eschool.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.ClassAdapterItemBinding;
import com.op.eschool.databinding.StudentAdapterItemBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.Holder> {

    List<ClassModel> list = new ArrayList<>() ;
    CommoTypenInterface commoTypenInterface ;

    public ClassAdapter(List<ClassModel> list, CommoTypenInterface commoTypenInterface) {
        this.list = list;
        this.commoTypenInterface = commoTypenInterface;
    }

    @NonNull
    @Override
    public ClassAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ClassAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.class_adapter_item, parent, false);
        return new ClassAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter.Holder holder, int position) {
        holder.binding.setModel(list.get(position)) ;


    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        ClassAdapterItemBinding binding ;
        public Holder(@NonNull ClassAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
