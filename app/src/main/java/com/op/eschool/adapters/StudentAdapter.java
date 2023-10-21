package com.op.eschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.op.eschool.R;
import com.op.eschool.databinding.StudentAdapterItemBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.models.student.StudentModel;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.Holder> {
    Context context;
    List<StudentModel> list = new ArrayList<>();
    CommoTypenInterface commoTypenInterface;

    public StudentAdapter(Context context, List<StudentModel> list, CommoTypenInterface commoTypenInterface) {
        this.commoTypenInterface = commoTypenInterface;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StudentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StudentAdapterItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.student_adapter_item, parent, false);
        return new StudentAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.Holder holder, int position) {
        try {

            holder.binding.setModel(list.get(position));
            holder.binding.txtStatus.setTextColor(context.getColor(list.get(position).actionStatus.equalsIgnoreCase("APPROVED") ? R.color.green : list.get(position).actionStatus.equalsIgnoreCase("PENDING") ? R.color.black : R.color.red));
//        holder.binding.ivApprove.setVisibility(list.get(position).actionStatus.equalsIgnoreCase("PENDING")? View.VISIBLE:View.GONE);
            holder.binding.ivDelele.setVisibility(list.get(position).actionStatus.equalsIgnoreCase("DELETE") ? View.GONE : View.VISIBLE);
            holder.itemView.setOnClickListener(v -> {
                holder.binding.expandableLayout.setExpanded(!holder.binding.expandableLayout.isExpanded());
            });
            holder.binding.ivCall.setOnClickListener(v -> {
                commoTypenInterface.onItemClicked(position, "CALL");
            });
            holder.binding.ivApprove.setOnClickListener(v -> {
                commoTypenInterface.onItemClicked(position, "APPROVE");
            });
            holder.binding.ivDelele.setOnClickListener(v -> {
                commoTypenInterface.onItemClicked(position, "DELETE");
            });

            holder.binding.btnView.setOnClickListener(v -> {
                commoTypenInterface.onItemClicked(position, "VIEW");
            });
            holder.binding.ivAvatar.setOnClickListener(v -> {
                commoTypenInterface.onItemClicked(position, "IMAGE_VIEW");
            });

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.students_placeholder)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE);

            Glide.with(context)
                    .load((list.get(position).Image))
                    .apply(requestOptions)
                    .into(holder.binding.ivAvatar);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        StudentAdapterItemBinding binding;

        public Holder(@NonNull StudentAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
