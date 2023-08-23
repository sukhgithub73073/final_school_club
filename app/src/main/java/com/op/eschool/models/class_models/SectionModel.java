
package com.op.eschool.models.class_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SectionModel {

    @SerializedName("sec_class_id")
    @Expose
    private String secClassId;
    @SerializedName("sec_id")
    @Expose
    private String secId;
    @SerializedName("sec_name")
    @Expose
    private String secName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("teacher_id")
    @Expose
    private String teacherId;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getSecClassId() {
        return secClassId;
    }

    public void setSecClassId(String secClassId) {
        this.secClassId = secClassId;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
