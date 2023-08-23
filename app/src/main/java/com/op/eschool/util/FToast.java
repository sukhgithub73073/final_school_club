package com.op.eschool.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.op.eschool.R;


public class FToast {

    public static int  LENGTH_SHORT =0 ;
    public static int  LENGTH_LONG =1 ;

    public static Toast makeText(Context context , String message , int duration){


        View view = LayoutInflater.from(context).inflate(R.layout.f_toast, null);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view) ;
        TextView txt_message = view.findViewById(R.id.txt_message) ;
        txt_message.setText("" + message) ;
        toast.setDuration(LENGTH_SHORT);
        toast.show() ;
        return  toast ;
    }

    public static Toast makeText(Context context , int id , int duration){

        View view = LayoutInflater.from(context).inflate(R.layout.f_toast, null);
        Toast toast = new Toast(context);
//        toast.setMargin(50,50);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view) ;
        TextView txt_message = view.findViewById(R.id.txt_message) ;
        txt_message.setText(context.getString(id)) ;
        toast.setDuration(LENGTH_SHORT);
        toast.show() ;
        return  toast ;
    }

}
