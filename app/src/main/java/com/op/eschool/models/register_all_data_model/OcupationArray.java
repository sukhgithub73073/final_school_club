
package com.op.eschool.models.register_all_data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OcupationArray {

    @SerializedName("staus")
    @Expose
    private String staus;
    @SerializedName("OccupationName")
    @Expose
    private String occupationName;
    @SerializedName("OccupationId")
    @Expose
    private String occupationId;
    @SerializedName("type")
    @Expose
    private String type;

    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }

    public String getOccupationName() {
        return occupationName;
    }

    public void setOccupationName(String occupationName) {
        this.occupationName = occupationName;
    }

    public String getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(String occupationId) {
        this.occupationId = occupationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
