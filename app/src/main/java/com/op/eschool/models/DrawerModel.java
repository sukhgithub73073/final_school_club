package com.op.eschool.models;

import android.content.Intent;

public class DrawerModel {
    public  int icon ;
    public  String title ;
    public Intent intent ;

    public DrawerModel(int icon, String title, Intent intent) {
        this.icon = icon;
        this.title = title;
        this.intent = intent;
    }
}
