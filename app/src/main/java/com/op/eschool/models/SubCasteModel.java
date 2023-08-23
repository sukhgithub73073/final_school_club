
package com.op.eschool.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCasteModel {

    @SerializedName("caste_id")
    @Expose
    private String casteId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("subcaste_id")
    @Expose
    private String subcasteId;
    @SerializedName("subcaste_name")
    @Expose
    private String subcasteName;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getCasteId() {
        return casteId;
    }

    public void setCasteId(String casteId) {
        this.casteId = casteId;
    }

    public String getStatus() {
        return status;
    }

    public void setStaus(String staus) {
        this.status = staus;
    }

    public String getSubcasteId() {
        return subcasteId;
    }

    public void setSubcasteId(String subcasteId) {
        this.subcasteId = subcasteId;
    }

    public String getSubcasteName() {
        return subcasteName;
    }

    public void setSubcasteName(String subcasteName) {
        this.subcasteName = subcasteName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
