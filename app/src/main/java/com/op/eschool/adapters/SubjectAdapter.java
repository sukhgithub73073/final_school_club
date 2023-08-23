package com.op.eschool.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.SubjectAdapterItemBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.models.class_models.SubjectModel;

import java.util.ArrayList;
import java.util.List;


public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.Holder> {

        List<SubjectModel> list = new ArrayList<>() ;
        CommoTypenInterface commoTypenInterface ;

public SubjectAdapter(List<SubjectModel> list, CommoTypenInterface commoTypenInterface) {
        this.list = list;
        this.commoTypenInterface = commoTypenInterface;
        }

@NonNull
@Override
public SubjectAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SubjectAdapterItemBinding binding  = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.subject_adapter_item, parent, false);
        return new SubjectAdapter.Holder(binding);
        }

@Override
public void onBindViewHolder(@NonNull SubjectAdapter.Holder holder, int position) {
        holder.binding.setModel(list.get(position)) ;
        holder.binding.delele.setOnClickListener(v->{commoTypenInterface.onItemClicked(position , "DELETE");});
        holder.binding.edit.setOnClickListener(v->{commoTypenInterface.onItemClicked(position , "EDIT");});



        }

@Override
public int getItemCount() {
        return list.size() ;
        }

class Holder extends RecyclerView.ViewHolder {
    SubjectAdapterItemBinding binding ;
    public Holder(@NonNull SubjectAdapterItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
}
