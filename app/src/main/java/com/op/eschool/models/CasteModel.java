
package com.op.eschool.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CasteModel {

    @SerializedName("caste_id")
    @Expose
    private String casteId;
    @SerializedName("caste_name")
    @Expose
    private String casteName;
    @SerializedName("status")
    @Expose
    private String status;

    public String getCasteId() {
        return casteId;
    }

    public void setCasteId(String casteId) {
        this.casteId = casteId;
    }

    public String getCasteName() {
        return casteName;
    }

    public void setCasteName(String casteName) {
        this.casteName = casteName;
    }

    public String getStatus() {
        return status;
    }

    public void setStaus(String staus) {
        this.status = staus;
    }

}
