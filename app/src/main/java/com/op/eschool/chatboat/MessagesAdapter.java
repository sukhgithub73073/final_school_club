package com.op.eschool.chatboat;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.op.eschool.R;
import com.op.eschool.databinding.MessagesAdapterItemBinding;
import com.op.eschool.interfaces.SuggestInterface;
import com.op.eschool.models.chatboat_model.ChatboatModel;
import com.op.eschool.models.chatboat_model.Suggest;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.Holder> {
    List<ChatboatModel> list = new ArrayList<>() ;
    SuggestInterface suggestInterface ;

    public MessagesAdapter(List<ChatboatModel> list, SuggestInterface suggestInterface) {
        this.list = list;
        this.suggestInterface = suggestInterface;
    }

    @NonNull
    @Override
    public MessagesAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MessagesAdapterItemBinding binding  = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.messages_adapter_item, parent, false);

        return new MessagesAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.Holder holder, int position) {
        holder.binding.setModel(list.get(position));
        if(list.get(position).getMessageFrom().equalsIgnoreCase("SENT")){
            //If a message is sent
            holder.binding.sentLayout.setVisibility(LinearLayout.VISIBLE);
            holder.binding.sentTextView.setText(list.get(position).getMessage());
            // Set visibility as GONE to remove the space taken up
            holder.binding.receivedLayout.setVisibility(LinearLayout.GONE);
        }
        else{
            //Message is received
            holder.binding.receivedLayout.setVisibility(LinearLayout.VISIBLE);
            holder.binding.receivedTextView.setText(list.get(position).getMessage()) ;
            // Set visibility as GONE to remove the space taken up
            holder.binding.sentLayout.setVisibility(LinearLayout.GONE);
        }
        holder.binding.setSuggestionAdapter(new SuggestionAdapter(list.get(position).getSuggests() , suggestInterface)) ;

    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    class Holder extends RecyclerView.ViewHolder {
        MessagesAdapterItemBinding binding ;
        public Holder(@NonNull MessagesAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

