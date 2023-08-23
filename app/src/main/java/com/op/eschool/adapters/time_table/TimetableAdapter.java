package com.op.eschool.adapters.time_table;

import static org.xmlpull.v1.XmlPullParser.TYPES;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.op.eschool.R;
import com.op.eschool.adapters.SubjectAdapter;
import com.op.eschool.databinding.TimeTableAdapterItemBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.interfaces.TimetableInterface;
import com.op.eschool.models.class_models.SubjectModel;
import com.op.eschool.models.pincode_api_model.PostOffice;
import com.op.eschool.models.staff.StaffModel;
import com.op.eschool.models.timetable_model.AllTimetableModel;
import com.op.eschool.models.timetable_model.TimeTableModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.Holder> {
    Context context ;
    AllTimetableModel allTimetableModel ;
    TimetableInterface commoTypenInterface ;
    String updateFlag ;
    public TimetableAdapter(String updateFlag , Context context, AllTimetableModel allTimetableModel , TimetableInterface commoTypenInterface) {
        this.updateFlag = updateFlag;
        this.commoTypenInterface = commoTypenInterface;
        this.context = context;
        this.allTimetableModel = allTimetableModel;
    }
    @NonNull
    @Override
    public TimetableAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TimeTableAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.time_table_adapter_item, parent, false);
        return new TimetableAdapter.Holder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull TimetableAdapter.Holder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.setModel(allTimetableModel.tableModelList.get(position)) ;
        if (updateFlag.equalsIgnoreCase("TRUE")){
            holder.binding.etSubject.setOnClickListener(v->{
                commoTypenInterface.onSpinnerChange(position ,"SUBJECT" ,holder.binding.etSubject) ;
            });
            holder.binding.etSubSubject.setOnClickListener(v->{
                commoTypenInterface.onSpinnerChange(position ,"SUBSUBJECT" ,holder.binding.etSubSubject) ;
            });
            holder.binding.etTeacher.setOnClickListener(v->{
                commoTypenInterface.onSpinnerChange(position ,"STAFF" ,holder.binding.etTeacher) ;
            });
            if (allTimetableModel.tableModelList.get(position).getSubjectOption().toLowerCase().equalsIgnoreCase("false")){
                holder.binding.txtSubject.setVisibility(View.VISIBLE) ;
                holder.binding.etSubject.setVisibility(View.GONE) ;
                holder.binding.txtSubSubject.setVisibility(View.VISIBLE) ;
                holder.binding.etSubSubject.setVisibility(View.GONE) ;
            }else{
                if (!allTimetableModel.tableModelList.get(position).getSubject().equalsIgnoreCase("")){
                    holder.binding.etSubject.setText("" + allTimetableModel.tableModelList.get(position).getSubject());
                }
            }
            if (allTimetableModel.tableModelList.get(position).getTechargeOption().toLowerCase().equalsIgnoreCase("false")){
                holder.binding.txtTeacher.setVisibility(View.VISIBLE) ;
                holder.binding.etTeacher.setVisibility(View.GONE) ;
            }else{
                if (!allTimetableModel.tableModelList.get(position).getTeacher().equalsIgnoreCase("")){
                    int subPos = Arrays.asList(allTimetableModel.staffArray).indexOf(allTimetableModel.tableModelList.get(position).getTeacher());
                    holder.binding.etTeacher.setText("" + allTimetableModel.tableModelList.get(position).getTeacher());
                }
            }
            holder.binding.etSubSubject.setText( allTimetableModel.tableModelList.get(position).getSubSubject());
        }else {
            holder.binding.txtSubject.setVisibility(View.VISIBLE) ;
            holder.binding.etSubject.setVisibility(View.GONE) ;
            holder.binding.txtSubSubject.setVisibility(View.VISIBLE) ;
            holder.binding.etSubSubject.setVisibility(View.GONE) ;
            holder.binding.txtTeacher.setVisibility(View.VISIBLE) ;
            holder.binding.etTeacher.setVisibility(View.GONE) ;
//            if (allTimetableModel.tableModelList.get(position).getTeacher().equalsIgnoreCase("Select Teacher")){
//                holder.binding.txtTeacher.setText("--") ;
//            }
//            if (allTimetableModel.tableModelList.get(position).getSubject().equalsIgnoreCase("Select Subject")){
//                holder.binding.txtSubject.setText("--") ;
//            }
//            if (allTimetableModel.tableModelList.get(position).getSubSubject().equalsIgnoreCase("SELECT SUB SUBJECT")){
//                holder.binding.txtSubSubject.setText("--") ;
//            }
        }
    }
    @Override
    public int getItemCount() {
        return allTimetableModel.tableModelList.size() ;
    }
    class Holder extends RecyclerView.ViewHolder {
        TimeTableAdapterItemBinding binding ;
        public Holder(@NonNull TimeTableAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}