package com.op.eschool.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.op.eschool.R;
import com.op.eschool.models.class_models.ClassModel;

import java.util.List;

public class SpiClassAdapter  extends ArrayAdapter<ClassModel> {

    LayoutInflater flater;

    public SpiClassAdapter(Activity context, int resouceId, List<ClassModel> list){

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

        ClassModel rowItem = getItem(position);

        SpiClassAdapter.viewHolder holder ;
        View rowview = convertView;
        if (rowview==null) {
            holder = new SpiClassAdapter.viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.operater_spinner_adapter_item, null, false);
            holder.txtTitle = rowview.findViewById(R.id.name);
            holder.iv_image = rowview.findViewById(R.id.iv_image);
            rowview.setTag(holder);
        }else{
            holder = (SpiClassAdapter.viewHolder) rowview.getTag();
        }
        holder.txtTitle.setText(rowItem.getName());

        return rowview;

    }

    public class viewHolder{
        TextView txtTitle;
        ImageView iv_image ;
    }
}