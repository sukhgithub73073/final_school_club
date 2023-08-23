package com.op.eschool.models.timetable_model;

import com.op.eschool.models.class_models.SubjectModel;
import com.op.eschool.models.staff.StaffModel;

import java.util.ArrayList;
import java.util.List;

public class AllTimetableModel {
    public List<TimeTableModel> tableModelList = new ArrayList<>() ;
    public List<StaffModel> staffModelList = new ArrayList<>() ;
    public List<SubjectModel> subjectModels = new ArrayList<>() ;
    public String[] subjectArray ;
    public String[] staffArray ;

    public AllTimetableModel(List<TimeTableModel> tableModelList, List<StaffModel> staffModelList, List<SubjectModel> subjectModels) {
        this.tableModelList = tableModelList;
        this.staffModelList = staffModelList;
        this.subjectModels = subjectModels;
    }
}
