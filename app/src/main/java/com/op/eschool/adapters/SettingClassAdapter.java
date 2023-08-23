package com.op.eschool.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.ClassAdapterItemBinding;
import com.op.eschool.databinding.SettingClassAdapterItemBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.models.class_models.ClassModel;

import java.util.ArrayList;
import java.util.List;

public class SettingClassAdapter extends RecyclerView.Adapter<SettingClassAdapter.Holder> {

    List<ClassModel> list = new ArrayList<>() ;
    CommoTypenInterface commoTypenInterface ;

    public SettingClassAdapter(List<ClassModel> list, CommoTypenInterface commoTypenInterface) {
        this.list = list;
        this.commoTypenInterface = commoTypenInterface;
    }

    @NonNull
    @Override
    public SettingClassAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SettingClassAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.setting_class_adapter_item, parent, false);
        return new SettingClassAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingClassAdapter.Holder holder, int position) {
        holder.binding.setModel(list.get(position)) ;
        holder.binding.status.setText(list.get(position).getStatus().equalsIgnoreCase("true")?"Status : ACTIVE" : "Status : DE-ACTIVE");
        holder.binding.delele.setOnClickListener(v->{commoTypenInterface.onItemClicked(position , "DELETE");});
        holder.binding.edit.setOnClickListener(v->{commoTypenInterface.onItemClicked(position , "EDIT");});



    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        SettingClassAdapterItemBinding binding ;
        public Holder(@NonNull SettingClassAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
