package com.op.eschool.models;

import java.util.ArrayList;
import java.util.List;

public class StaffDrawerModel {
    public String title ;
    public  int icon ;

    public  List<DrawerModel> list = new ArrayList<>() ;

    public StaffDrawerModel(int icon , String title, List<DrawerModel> list) {
        this.icon = icon;
        this.title = title;
        this.list = list;
    }
}
