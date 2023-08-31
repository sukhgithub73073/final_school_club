
package com.op.eschool.models.class_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CastCounterModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Class_Name")
    @Expose
    private String className;
    @SerializedName("OBC_Count")
    @Expose
    private Integer oBCCount;
    @SerializedName("SC_Count")
    @Expose
    private Integer sCCount;
    @SerializedName("ST_Count")
    @Expose
    private Integer sTCount;
    @SerializedName("GENRAL_Count")
    @Expose
    private Integer gENRALCount;
    @SerializedName("type")
    @Expose
    private String type;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getOBCCount() {
        return oBCCount;
    }

    public void setOBCCount(Integer oBCCount) {
        this.oBCCount = oBCCount;
    }

    public Integer getSCCount() {
        return sCCount;
    }

    public void setSCCount(Integer sCCount) {
        this.sCCount = sCCount;
    }

    public Integer getSTCount() {
        return sTCount;
    }

    public void setSTCount(Integer sTCount) {
        this.sTCount = sTCount;
    }

    public Integer getGENRALCount() {
        return gENRALCount;
    }

    public void setGENRALCount(Integer gENRALCount) {
        this.gENRALCount = gENRALCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
