
package com.op.eschool.models.school_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSchoolModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Msg")
    @Expose
    private String msg;
    @SerializedName("CollageId")
    @Expose
    private String collageId;
    @SerializedName("CollageName")
    @Expose
    private String collageName;
    @SerializedName("CollageCode")
    @Expose
    private String collageCode;
    @SerializedName("Type")
    @Expose
    private String typeg;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("Unqid")
    @Expose
    private String Unqid;

    public String getUnqid() {
        return Unqid;
    }

    public void setUnqid(String unqid) {
        Unqid = unqid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCollageId() {
        return collageId;
    }

    public void setCollageId(String collageId) {
        this.collageId = collageId;
    }

    public String getCollageName() {
        return collageName;
    }

    public void setCollageName(String collageName) {
        this.collageName = collageName;
    }

    public String getCollageCode() {
        return collageCode;
    }

    public void setCollageCode(String collageCode) {
        this.collageCode = collageCode;
    }

    public String getTypeg() {
        return typeg;
    }

    public void setTypeg(String typeg) {
        this.typeg = typeg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
