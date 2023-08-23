
package com.op.eschool.models.timetable_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeTableModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Class")
    @Expose
    private String _class;
    @SerializedName("ClassId")
    @Expose
    private String classId;
    @SerializedName("MainClass")
    @Expose
    private String mainClass;
    @SerializedName("Period")
    @Expose
    private String period;
    @SerializedName("PeriodId")
    @Expose
    private String periodId;
    @SerializedName("SubSubject")
    @Expose
    private String subSubject;
    @SerializedName("SubSubjectId")
    @Expose
    private String subSubjectId;
    @SerializedName("Subject")
    @Expose
    private String subject;
    @SerializedName("SubjectId")
    @Expose
    private String subjectId;
    @SerializedName("SubjectOption")
    @Expose
    private String subjectOption;
    @SerializedName("Teacher")
    @Expose
    private String teacher;
    @SerializedName("TeacherId")
    @Expose
    private String teacherId;
    @SerializedName("TechargeOption")
    @Expose
    private String techargeOption;
    @SerializedName("TimeFrom")
    @Expose
    private String timeFrom;
    @SerializedName("TimeFromId")
    @Expose
    private String timeFromId;
    @SerializedName("TimeTableId")
    @Expose
    private String timeTableId;
    @SerializedName("TimeTo")
    @Expose
    private String timeTo;
    @SerializedName("TimeToId")
    @Expose
    private String timeToId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("subjectType")
    @Expose
    private boolean subjectType;


    @SerializedName("teacherType")
    @Expose
    private boolean teacherType;


    public boolean isSubjectType() {
        return subjectType;
    }

    public void setSubjectType(boolean subjectType) {
        this.subjectType = subjectType;
    }

    public boolean isTeacherType() {
        return teacherType;
    }

    public void setTeacherType(boolean teacherType) {
        this.teacherType = teacherType;
    }

    public String getStatus() {
        return status;
    }

    public void setStaus(String staus) {
        this.status = staus;
    }

    public String getClass_() {
        return _class;
    }

    public void setClass_(String _class) {
        this._class = _class;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getSubSubject() {
        return subSubject;
    }

    public void setSubSubject(String subSubject) {
        this.subSubject = subSubject;
    }

    public String getSubSubjectId() {
        return subSubjectId;
    }

    public void setSubSubjectId(String subSubjectId) {
        this.subSubjectId = subSubjectId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectOption() {
        return subjectOption==null?"":subjectOption;
    }

    public void setSubjectOption(String subjectOption) {
        this.subjectOption = subjectOption;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTechargeOption() {
        return techargeOption==null?"":techargeOption;
    }

    public void setTechargeOption(String techargeOption) {
        this.techargeOption = techargeOption;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeFromId() {
        return timeFromId;
    }

    public void setTimeFromId(String timeFromId) {
        this.timeFromId = timeFromId;
    }

    public String getTimeTableId() {
        return timeTableId;
    }

    public void setTimeTableId(String timeTableId) {
        this.timeTableId = timeTableId;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getTimeToId() {
        return timeToId;
    }

    public void setTimeToId(String timeToId) {
        this.timeToId = timeToId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
