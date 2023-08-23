package com.op.eschool.models;

import android.app.Activity;

import com.op.eschool.interfaces.DialogInterface;

public class DialogModel {
    public Activity activity ;
    public String title , des ,postiveBtn , negtiveBtn ;
    public DialogInterface dialogInterface ;

    public DialogModel(Activity activity, String title, String des, String postiveBtn, String negtiveBtn, DialogInterface dialogInterface) {
        this.activity = activity;
        this.title = title;
        this.des = des;
        this.postiveBtn = postiveBtn;
        this.negtiveBtn = negtiveBtn;
        this.dialogInterface = dialogInterface;
    }
}
