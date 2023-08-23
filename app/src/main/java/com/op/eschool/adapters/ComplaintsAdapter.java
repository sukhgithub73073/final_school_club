package com.op.eschool.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.ComplaintsAdapterItemBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.models.class_models.ClassModel;
import com.op.eschool.models.complaint_model.ComplaintsModel;
import com.op.eschool.models.student.StudentModel;

import java.util.ArrayList;
import java.util.List;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.Holder> {

    List<ComplaintsModel> list = new ArrayList<>() ;
    CommoTypenInterface commoTypenInterface ;

    public ComplaintsAdapter(List<ComplaintsModel> list, CommoTypenInterface commoTypenInterface) {
        this.list = list;
        this.commoTypenInterface = commoTypenInterface;
    }

    @NonNull
    @Override
    public ComplaintsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ComplaintsAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.complaints_adapter_item, parent, false);
        return new ComplaintsAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintsAdapter.Holder holder, int position) {
        holder.binding.setModel(list.get(position)) ;


    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        ComplaintsAdapterItemBinding binding ;
        public Holder(@NonNull ComplaintsAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
