package com.op.eschool.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.op.eschool.R;
import com.op.eschool.databinding.DrawerAdaperItemBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.interfaces.DrawerInterface;
import com.op.eschool.models.DrawerModel;
import com.op.eschool.models.StaffDrawerModel;

import java.util.ArrayList;
import java.util.List;

public class DrawerAdaptter extends RecyclerView.Adapter<DrawerAdaptter.Holder> {
    List<StaffDrawerModel> list = new ArrayList<>();
    Context context;
    DrawerInterface commonInterface;

    public DrawerAdaptter(List<StaffDrawerModel> list, Context context, DrawerInterface commonInterface) {
        this.list = list;
        this.context = context;
        this.commonInterface = commonInterface;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        DrawerAdaperItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.drawer_adaper_item, parent, false);

        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {

        holder.binding.txtTitle.setText("" + list.get(position).title);
        Glide.with(context)
                .load(list.get(position).icon)
                .apply(new RequestOptions().placeholder(R.drawable.attendance))
                .into(holder.binding.ivImage);
        holder.binding.expandableLayout.setExpanded(list.get(position).openMenu) ;

        holder.binding.setDrawerItemAdapter(new DrawerItemAdapter(list.get(position).list, context, commonInterface));
        holder.itemView.setOnClickListener(v -> {
            if (list.get(position).title.equalsIgnoreCase("Logout")) {
                commonInterface.onMainItemClicked("Logout");
            } else if (list.get(position).list.size() == 0) {
                commonInterface.onMainItemClicked("" + list.get(position).title);
            } else {
                for (StaffDrawerModel staffDrawerModel : list){
                    staffDrawerModel.openMenu = false ;
                }
                list.get(position).openMenu = !holder.binding.expandableLayout.isExpanded() ;
                holder.binding.expandableLayout.setExpanded(!holder.binding.expandableLayout.isExpanded());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        DrawerAdaperItemBinding binding;

        public Holder(@NonNull DrawerAdaperItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }


    }
}
