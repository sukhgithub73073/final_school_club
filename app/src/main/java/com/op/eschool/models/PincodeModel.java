
package com.op.eschool.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PincodeModel {

    @SerializedName("DistrictName")
    @Expose
    private String districtName;
    @SerializedName("Pincode")
    @Expose
    private String pincode;
    @SerializedName("PincodeId")
    @Expose
    private String pincodeId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("State")
    @Expose
    private String state;
    @SerializedName("Thesil")
    @Expose
    private String thesil;
    @SerializedName("ThesilId")
    @Expose
    private String thesilId;

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPincodeId() {
        return pincodeId;
    }

    public void setPincodeId(String pincodeId) {
        this.pincodeId = pincodeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStaus(String staus) {
        this.status = staus;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getThesil() {
        return thesil;
    }

    public void setThesil(String thesil) {
        this.thesil = thesil;
    }

    public String getThesilId() {
        return thesilId;
    }

    public void setThesilId(String thesilId) {
        this.thesilId = thesilId;
    }

}
