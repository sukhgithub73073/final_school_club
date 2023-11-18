package com.op.eschool.models;

import java.util.ArrayList;
import java.util.List;

public class StaffDrawerModel {
    public String title ;
    public  int icon ;
    public  List<DrawerModel> list = new ArrayList<>() ;
    public  boolean openMenu = false;

    public StaffDrawerModel(boolean openMenu , int icon , String title, List<DrawerModel> list) {
        this.openMenu = openMenu;
        this.icon = icon;
        this.title = title;
        this.list = list;
    }
}
