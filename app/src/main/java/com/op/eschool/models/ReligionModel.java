
package com.op.eschool.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReligionModel {
    @SerializedName("ReligionId")
    @Expose
    private String religionId;
    @SerializedName("ReligionName")
    @Expose
    private String religionName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getReligionId() {
        return religionId;
    }

    public void setReligionId(String religionId) {
        this.religionId = religionId;
    }

    public String getReligionName() {
        return religionName;
    }

    public void setReligionName(String religionName) {
        this.religionName = religionName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String staus) {
        this.status = staus;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
