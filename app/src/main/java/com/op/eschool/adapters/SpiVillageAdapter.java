package com.op.eschool.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.op.eschool.R;
import com.op.eschool.models.pincode_api_model.PostOffice;

import java.util.List;

public class SpiVillageAdapter extends ArrayAdapter<PostOffice> {

    LayoutInflater flater;
    String type ;

    public SpiVillageAdapter(String type , Activity context, int resouceId, List<PostOffice> list){
        super(context,resouceId, list);
        this.type = type ;
//        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView,position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView,position);
    }

    private View rowview(View convertView , int position){

        PostOffice rowItem = getItem(position);

        SpiVillageAdapter.viewHolder holder ;
        View rowview = convertView;
        if (rowview==null) {
            holder = new SpiVillageAdapter.viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.operater_spinner_adapter_item, null, false);
            holder.txtTitle = rowview.findViewById(R.id.name);
            holder.iv_image = rowview.findViewById(R.id.iv_image);
            rowview.setTag(holder);
        }else{
            holder = (SpiVillageAdapter.viewHolder) rowview.getTag();
        }

        holder.txtTitle.setText(type.equalsIgnoreCase("TEHSIL")?rowItem.getBlock() : rowItem.getName());

        return rowview;

    }

    public class viewHolder{
        TextView txtTitle;
        ImageView iv_image ;
    }
}