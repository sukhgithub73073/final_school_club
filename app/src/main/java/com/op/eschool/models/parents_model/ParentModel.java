
package com.op.eschool.models.parents_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParentModel {

    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("prt_address")
    @Expose
    private String prtAddress;
    @SerializedName("prt_cast")
    @Expose
    private String prtCast;
    @SerializedName("prt_children_id")
    @Expose
    private String prtChildrenId;
    @SerializedName("prt_city")
    @Expose
    private String prtCity;
    @SerializedName("prt_gender")
    @Expose
    private String prtGender;
    @SerializedName("prt_mobile")
    @Expose
    private String prtMobile;
    @SerializedName("prt_name")
    @Expose
    private String prtName;
    @SerializedName("prt_photo")
    @Expose
    private String prtPhoto;
    @SerializedName("prt_state")
    @Expose
    private String prtState;
    @SerializedName("prt_zip_code")
    @Expose
    private String prtZipCode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPrtAddress() {
        return prtAddress;
    }

    public void setPrtAddress(String prtAddress) {
        this.prtAddress = prtAddress;
    }

    public String getPrtCast() {
        return prtCast;
    }

    public void setPrtCast(String prtCast) {
        this.prtCast = prtCast;
    }

    public String getPrtChildrenId() {
        return prtChildrenId;
    }

    public void setPrtChildrenId(String prtChildrenId) {
        this.prtChildrenId = prtChildrenId;
    }

    public String getPrtCity() {
        return prtCity;
    }

    public void setPrtCity(String prtCity) {
        this.prtCity = prtCity;
    }

    public String getPrtGender() {
        return prtGender;
    }

    public void setPrtGender(String prtGender) {
        this.prtGender = prtGender;
    }

    public String getPrtMobile() {
        return prtMobile;
    }

    public void setPrtMobile(String prtMobile) {
        this.prtMobile = prtMobile;
    }

    public String getPrtName() {
        return prtName;
    }

    public void setPrtName(String prtName) {
        this.prtName = prtName;
    }

    public String getPrtPhoto() {
        return prtPhoto;
    }

    public void setPrtPhoto(String prtPhoto) {
        this.prtPhoto = prtPhoto;
    }

    public String getPrtState() {
        return prtState;
    }

    public void setPrtState(String prtState) {
        this.prtState = prtState;
    }

    public String getPrtZipCode() {
        return prtZipCode;
    }

    public void setPrtZipCode(String prtZipCode) {
        this.prtZipCode = prtZipCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
