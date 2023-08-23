
package com.op.eschool.models.parents_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OccupationModel {

    @SerializedName("OccupationId")
    @Expose
    private String occupationId;
    @SerializedName("OccupationName")
    @Expose
    private String occupationName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(String occupationId) {
        this.occupationId = occupationId;
    }

    public String getOccupationName() {
        return occupationName;
    }

    public void setOccupationName(String occupationName) {
        this.occupationName = occupationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStaus(String staus) {
        this.status = staus;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
