package com.op.eschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.op.eschool.R;
import com.op.eschool.databinding.DashboardAdapterItemBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.DashboardModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.Holder> {
    List<DashboardModel> list = new ArrayList<>() ;
    Context context ;
    CommonInterface commonInterface ;

    public DashboardAdapter(List<DashboardModel> list, Context context , CommonInterface commonInterface) {
        this.list = list;
        this.context = context;
        this.commonInterface = commonInterface;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DashboardAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.dashboard_adapter_item, parent, false);

        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.itemRowBinding.setModel(list.get(position)) ;
        Glide.with(context)
                .load(list.get(position).icon)
                .fitCenter()
                .into(holder.itemRowBinding.icon) ;
        holder.itemView.setOnClickListener(v->{
            commonInterface.onItemClicked(position) ;
        });
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        DashboardAdapterItemBinding itemRowBinding ;
        public Holder(@NonNull DashboardAdapterItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }
    }
}
