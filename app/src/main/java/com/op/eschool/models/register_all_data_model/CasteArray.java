
package com.op.eschool.models.register_all_data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CasteArray {

    @SerializedName("staus")
    @Expose
    private String staus;
    @SerializedName("CasteName")
    @Expose
    private String casteName;
    @SerializedName("CasteId")
    @Expose
    private String casteId;
    @SerializedName("type")
    @Expose
    private String type;

    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }

    public String getCasteName() {
        return casteName;
    }

    public void setCasteName(String casteName) {
        this.casteName = casteName;
    }

    public String getCasteId() {
        return casteId;
    }

    public void setCasteId(String casteId) {
        this.casteId = casteId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
