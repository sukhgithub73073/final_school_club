package com.op.eschool.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.SubjectSelectionAdapterItemBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.models.class_models.SubjectModel;

import java.util.ArrayList;
import java.util.List;

public class SubjectSelectionAdapter  extends RecyclerView.Adapter<SubjectSelectionAdapter.Holder> {

    List<SubjectModel> list = new ArrayList<>() ;
    CommoTypenInterface commoTypenInterface ;

    public SubjectSelectionAdapter(List<SubjectModel> list, CommoTypenInterface commoTypenInterface) {
        this.list = list;
        this.commoTypenInterface = commoTypenInterface;
    }

    @NonNull
    @Override
    public SubjectSelectionAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SubjectSelectionAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.subject_selection_adapter_item, parent, false);
        return new SubjectSelectionAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectSelectionAdapter.Holder holder, int position) {
        holder.binding.setModel(list.get(position)) ;
        holder.binding.cbSubject.setOnCheckedChangeListener((cutton , b)->{
            commoTypenInterface.onItemClicked(position , b?"ADD":"REMOVE");

        });
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        SubjectSelectionAdapterItemBinding binding ;
        public Holder(@NonNull SubjectSelectionAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
