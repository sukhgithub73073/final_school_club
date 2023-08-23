package com.op.eschool.adapters;

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
import com.op.eschool.databinding.DrawerItemAdaperItemBinding;
import com.op.eschool.interfaces.CommonInterface;
import com.op.eschool.interfaces.DrawerInterface;
import com.op.eschool.models.DrawerModel;
import com.op.eschool.models.StaffDrawerModel;

import java.util.ArrayList;
import java.util.List;

public class DrawerItemAdapter extends RecyclerView.Adapter<DrawerItemAdapter.Holder> {
    List<DrawerModel> list = new ArrayList<>() ;
    Context context ;
    DrawerInterface commonInterface ;

    public DrawerItemAdapter(List<DrawerModel> list, Context context , DrawerInterface commonInterface) {
        this.list = list;
        this.context = context;
        this.commonInterface = commonInterface;
    }


    @NonNull
    @Override
    public DrawerItemAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        DrawerItemAdaperItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.drawer_item_adaper_item, parent, false);

        return new DrawerItemAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerItemAdapter.Holder holder, int position) {
        DrawerModel dataModel = list.get(position);
        holder.binding.txtTitle.setText("" + dataModel.title) ;
        Glide.with(context)
                .load(dataModel.icon)
                .apply(new RequestOptions().placeholder(R.drawable.attendance))
                .into(holder.binding.ivImage);
        holder.itemView.setOnClickListener(v->{
            commonInterface.onItemClicked(list.get(position)) ;
        });

    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        DrawerItemAdaperItemBinding binding ;

        public Holder(@NonNull DrawerItemAdaperItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }



    }
}
