package com.op.eschool.models.student;

public class StudentFilterModel {
    public String group , className , gender , caste , status ;

    public StudentFilterModel(String group, String className, String gender, String caste , String status) {
        this.group = group;
        this.className = className;
        this.gender = gender;
        this.caste = caste;
        this.status = status;
    }
}
