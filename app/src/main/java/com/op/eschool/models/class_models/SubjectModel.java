
package com.op.eschool.models.class_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubjectModel {

    @SerializedName("SubjectGroup")
    @Expose
    private String SubjectGroup;
    @SerializedName("Subject_code")
    @Expose
    private String SubjectCode;
    @SerializedName("SubjectId")
    @Expose
    private String SubjectId;

    @SerializedName("SubjectName")
    @Expose
    private String SubjectName;
    @SerializedName("Unqid")
    @Expose
    private String Unqid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public SubjectModel(String subjectGroup, String subjectCode, String subjectId, String subjectName, String unqid, String name, String status, String timestamp) {
        SubjectGroup = subjectGroup;
        SubjectCode = subjectCode;
        SubjectId = subjectId;
        SubjectName = subjectName;
        Unqid = unqid;
        this.name = name;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getSubjectName() {
        return SubjectName==null?"":SubjectName ;
    }

    public void setSubjectName(String SubjectName) {
        SubjectName = SubjectName;
    }

    public String getUnqid() {
        return Unqid;
    }

    public void setUnqid(String unqid) {
        Unqid = unqid;
    }

    public String getSubjectGroup() {
        return SubjectGroup;
    }

    public void setSubjectGroup(String SubjectGroup) {
        this.SubjectGroup = SubjectGroup;
    }

    public String getSubjectCode() {
        return SubjectCode;
    }

    public void setSubjectCode(String SubjectCode) {
        this.SubjectCode = SubjectCode;
    }

    public String getSubjectId() {
        return SubjectId;
    }

    public void setSubjectId(String SubjectId) {
        this.SubjectId = SubjectId;
    }

    public String getName() {
        return name==null?getSubjectName():name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
