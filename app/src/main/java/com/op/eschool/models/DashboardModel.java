package com.op.eschool.models;

import android.content.Intent;

public class DashboardModel {
    public String title ;
    public int icon ;
    public Intent intent ;

    public DashboardModel(String title, int icon, Intent intent) {
        this.title = title;
        this.icon = icon;
        this.intent = intent;
    }
}
