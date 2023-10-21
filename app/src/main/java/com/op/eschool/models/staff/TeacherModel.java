
package com.op.eschool.models.staff;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Teacher")
    @Expose
    private String teacher;
    @SerializedName("TeacherId")
    @Expose
    private String teacherId;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("StaffUnqid")
    @Expose
    private String staffUnqid;
    @SerializedName("Class")
    @Expose
    private String _class;
    @SerializedName("SubjectId")
    @Expose
    private String subjectId;
    @SerializedName("Subject")
    @Expose
    private String subject;
    @SerializedName("type")
    @Expose
    private String type;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getStaffUnqid() {
        return staffUnqid;
    }

    public void setStaffUnqid(String staffUnqid) {
        this.staffUnqid = staffUnqid;
    }

    public String getClass_() {
        return _class;
    }

    public void setClass_(String _class) {
        this._class = _class;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
