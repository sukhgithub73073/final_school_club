
package com.op.eschool.models.register_all_data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReligionArray {

    @SerializedName("staus")
    @Expose
    private String staus;
    @SerializedName("ReligionName")
    @Expose
    private String religionName;
    @SerializedName("ReligionId")
    @Expose
    private String religionId;
    @SerializedName("type")
    @Expose
    private String type;

    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }

    public String getReligionName() {
        return religionName;
    }

    public void setReligionName(String religionName) {
        this.religionName = religionName;
    }

    public String getReligionId() {
        return religionId;
    }

    public void setReligionId(String religionId) {
        this.religionId = religionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
