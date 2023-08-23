package com.op.eschool.chatboat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.SuggestionAdapterItemBinding;
import com.op.eschool.interfaces.SuggestInterface;
import com.op.eschool.models.chatboat_model.Suggest;

import java.util.ArrayList;
import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.Holder> {
    List<Suggest> list = new ArrayList<>() ;
    SuggestInterface suggestInterface ;

    public SuggestionAdapter(List<Suggest> list, SuggestInterface suggestInterface) {
        this.list = list;
        this.suggestInterface = suggestInterface;
    }

    @NonNull
    @Override
    public SuggestionAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SuggestionAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.suggestion_adapter_item, parent, false);

        return new SuggestionAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionAdapter.Holder holder, int position) {
        holder.binding.setModel(list.get(position));
        holder.itemView.setOnClickListener(v->{
            suggestInterface.onSuggestClicked(list.get(position)) ;
        });

    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        SuggestionAdapterItemBinding binding ;
        public Holder(@NonNull SuggestionAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

