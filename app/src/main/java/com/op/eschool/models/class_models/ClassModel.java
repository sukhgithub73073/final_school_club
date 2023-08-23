
package com.op.eschool.models.class_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassModel {

    @SerializedName("ClassGroup")
    @Expose
    private String classGroup;
    @SerializedName("class_code")
    @Expose
    private String classCode;
    @SerializedName("classId")
    @Expose
    private String classId;

    @SerializedName("ClassId")
    @Expose
    private String ClassId;

    @SerializedName("ClassName")
    @Expose
    private String ClassName;
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


    public ClassModel(String classGroup, String classCode, String classId, String classId1, String className, String unqid, String name, String status, String timestamp) {
        this.classGroup = classGroup;
        this.classCode = classCode;
        this.classId = classId;
        ClassId = classId1;
        ClassName = className;
        Unqid = unqid;
        this.name = name;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getClassName() {
        return ClassName==null?"":ClassName ;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getUnqid() {
        return Unqid;
    }

    public void setUnqid(String unqid) {
        Unqid = unqid;
    }

    public String getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(String classGroup) {
        this.classGroup = classGroup;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassId() {
        return classId==null?ClassId==null?"":ClassId:classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getName() {
        return name==null?getClassName():name;
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
