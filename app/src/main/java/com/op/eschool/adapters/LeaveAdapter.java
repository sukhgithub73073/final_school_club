package com.op.eschool.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.LeaveAdapterItemBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.models.leave_model.LeaveModel;

import java.util.ArrayList;
import java.util.List;


public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.Holder> {

    List<LeaveModel> list = new ArrayList<>() ;
    CommoTypenInterface commoTypenInterface ;

    public LeaveAdapter(List<LeaveModel> list, CommoTypenInterface commoTypenInterface) {
        this.list = list;
        this.commoTypenInterface = commoTypenInterface;
    }

    @NonNull
    @Override
    public LeaveAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LeaveAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.leave_adapter_item, parent, false);
        return new LeaveAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveAdapter.Holder holder, int position) {
        holder.binding.setModel(list.get(position)) ;
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        LeaveAdapterItemBinding binding ;
        public Holder(@NonNull LeaveAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
