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
import com.op.eschool.models.SubCasteModel;

import java.util.List;

public class SpiSubCasteAdapter extends ArrayAdapter<SubCasteModel> {

    LayoutInflater flater;

    public SpiSubCasteAdapter(Activity context, int resouceId, List<SubCasteModel> list){

        super(context,resouceId, list);
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

        SubCasteModel rowItem = getItem(position);

        SpiSubCasteAdapter.viewHolder holder ;
        View rowview = convertView;
        if (rowview==null) {
            holder = new SpiSubCasteAdapter.viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.operater_spinner_adapter_item, null, false);
            holder.txtTitle = rowview.findViewById(R.id.name);
            holder.iv_image = rowview.findViewById(R.id.iv_image);
            rowview.setTag(holder);
        }else{
            holder = (SpiSubCasteAdapter.viewHolder) rowview.getTag();
        }
        holder.txtTitle.setText(rowItem.getSubcasteName());

        return rowview;

    }

    public class viewHolder{
        TextView txtTitle;
        ImageView iv_image ;
    }
}