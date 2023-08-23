package com.op.eschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.op.eschool.R;

import com.op.eschool.databinding.StaffDashboardAdapterItemBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.models.DashboardModel;

import java.util.ArrayList;
import java.util.List;

public class StaffDashboardAdapter  extends RecyclerView.Adapter<StaffDashboardAdapter.Holder> {
    List<DashboardModel> list = new ArrayList<>() ;
    Context context ;
    CommonInterface commonInterface ;

    public StaffDashboardAdapter(List<DashboardModel> list, Context context , CommonInterface commonInterface) {
        this.list = list;
        this.context = context;
        this.commonInterface = commonInterface;
    }

    @NonNull
    @Override
    public StaffDashboardAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        StaffDashboardAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.staff_dashboard_adapter_item, parent, false);

        return new StaffDashboardAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffDashboardAdapter.Holder holder, int position) {
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
        StaffDashboardAdapterItemBinding itemRowBinding ;
        public Holder(@NonNull StaffDashboardAdapterItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }
    }
}