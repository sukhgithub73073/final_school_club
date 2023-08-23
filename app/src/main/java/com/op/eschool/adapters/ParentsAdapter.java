package com.op.eschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.ParentsAdapterItemBinding;
import com.op.eschool.databinding.TeacherAdapterItemBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.parents_model.ParentModel;

import java.util.ArrayList;
import java.util.List;

public class ParentsAdapter extends RecyclerView.Adapter<ParentsAdapter.Holder> {
    List<ParentModel> list = new ArrayList<>() ;
    Context context ;
    CommonInterface commonInterface ;

    public ParentsAdapter(List<ParentModel> list, Context context, CommonInterface commonInterface) {
        this.list = list;
        this.context = context;
        this.commonInterface = commonInterface;
    }

    @NonNull
    @Override
    public ParentsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ParentsAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.parents_adapter_item, parent, false);

        return new ParentsAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentsAdapter.Holder holder, int position) {
        holder.binding.setModel(list.get(position)) ;
        holder.itemView.setOnClickListener(v->{
            holder.binding.expandableLayout.setExpanded(!holder.binding.expandableLayout.isExpanded()) ;
        });
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        ParentsAdapterItemBinding binding ;
        public Holder(@NonNull ParentsAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
