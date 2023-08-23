package com.op.eschool.adapters.class_group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.ClassgroupAdapterItemBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.models.class_models.ClassGroupModel;
import com.op.eschool.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class ClassGroupAdapter extends RecyclerView.Adapter<ClassGroupAdapter.Holder> {
    Context context ;
    List<ClassGroupModel> list = new ArrayList<>() ;
    CommoTypenInterface commoTypenInterface ;
    public ClassGroupAdapter(Context context, List<ClassGroupModel> list , CommoTypenInterface commoTypenInterface) {
        this.commoTypenInterface = commoTypenInterface;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ClassGroupAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ClassgroupAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.classgroup_adapter_item, parent, false);

        return new ClassGroupAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassGroupAdapter.Holder holder, int position) {
        holder.binding.setModel(list.get(position)) ;
        holder.binding.status.setText(list.get(position).getStatus().equalsIgnoreCase(Constants.RESPONSE_SUCCESS) ?"ACTIVE":"DE-ACTIVE") ;
        holder.binding.delele.setOnClickListener(v->{
            commoTypenInterface.onItemClicked(position , "DELETE");
        });
        holder.binding.edit.setOnClickListener(v->{
            commoTypenInterface.onItemClicked(position , "EDIT");
        });

    }
    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        ClassgroupAdapterItemBinding binding ;
        public Holder(@NonNull ClassgroupAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
