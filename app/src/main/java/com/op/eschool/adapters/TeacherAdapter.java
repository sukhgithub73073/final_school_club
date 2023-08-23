package com.op.eschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.op.eschool.R;
import com.op.eschool.databinding.StudentAdapterItemBinding;
import com.op.eschool.databinding.TeacherAdapterItemBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.models.staff.StaffModel;
import com.op.eschool.models.student.StudentModel;
import com.op.eschool.util.Utility;

import java.util.ArrayList;
import java.util.List;
public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.Holder> {
    Context context ;
    List<StaffModel> list = new ArrayList<>() ;
    CommoTypenInterface commoTypenInterface ;
    public TeacherAdapter(Context context, List<StaffModel> list, CommoTypenInterface commoTypenInterface) {
        this.context = context;
        this.list = list;
        this.commoTypenInterface = commoTypenInterface;
    }
    @NonNull
    @Override
    public TeacherAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TeacherAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.teacher_adapter_item, parent, false);
        return new TeacherAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherAdapter.Holder holder, int position) {

        holder.binding.setModel(list.get(position)) ;
        holder.binding.txtStatus.setTextColor(context.getColor(list.get(position).getActionStatus().equalsIgnoreCase("APPROVED")? R.color.green : list.get(position).getActionStatus().equalsIgnoreCase("PENDING")? R.color.black : R.color.red)) ;
//        holder.binding.ivApprove.setVisibility(list.get(position).getActionStatus().equalsIgnoreCase("PENDING")? View.VISIBLE:View.GONE);
        holder.binding.ivDelele.setVisibility(list.get(position).getActionStatus().equalsIgnoreCase("DELETE")? View.GONE:View.VISIBLE) ;
        holder.itemView.setOnClickListener(v->{
            holder.binding.expandableLayout.setExpanded(!holder.binding.expandableLayout.isExpanded()) ;
        });
        holder.binding.ivCall.setOnClickListener(v->{
            commoTypenInterface.onItemClicked(position , "CALL") ;
        });
        holder.binding.ivApprove.setOnClickListener(v->{
            commoTypenInterface.onItemClicked(position , "APPROVE") ;
        });
        holder.binding.ivDelele.setOnClickListener(v->{
            commoTypenInterface.onItemClicked(position , "DELETE") ;
        });

        holder.binding.btnView.setOnClickListener(v->{
            commoTypenInterface.onItemClicked(position , "VIEW") ;
        });

        Glide.with(context)
                .load((list.get(position).Image))
                .apply(new RequestOptions().placeholder(R.drawable.students_placeholder))
                .into(holder.binding.ivAvatar)   ;

    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        TeacherAdapterItemBinding binding ;
        public Holder(@NonNull TeacherAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
