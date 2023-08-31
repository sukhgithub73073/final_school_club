package com.op.eschool.adapters.class_section;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.CastCounterAdapterItemBinding;
import com.op.eschool.interfaces.CommoTypenInterface;
import com.op.eschool.models.class_models.CastCounterModel;

import java.util.ArrayList;
import java.util.List;

public class CastCounterAdapter  extends RecyclerView.Adapter<CastCounterAdapter.Holder> {

    List<CastCounterModel> list = new ArrayList<>() ;
    CommoTypenInterface commoTypenInterface ;

    public CastCounterAdapter(List<CastCounterModel> list, CommoTypenInterface commoTypenInterface) {
        this.list = list;
        this.commoTypenInterface = commoTypenInterface;
    }

    @NonNull
    @Override
    public CastCounterAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CastCounterAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.cast_counter_adapter_item, parent, false);
        return new CastCounterAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CastCounterAdapter.Holder holder, int position) {
       // holder.binding.setModel(list.get(position)) ;


    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        CastCounterAdapterItemBinding binding ;
        public Holder(@NonNull CastCounterAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
