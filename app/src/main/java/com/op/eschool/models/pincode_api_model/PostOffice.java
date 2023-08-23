
package com.op.eschool.models.pincode_api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostOffice {
    
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Description")
    @Expose
    public Object description;
    @SerializedName("BranchType")
    @Expose
    public String branchType;
    @SerializedName("DeliveryStatus")
    @Expose
    public String deliveryStatus;
    @SerializedName("Circle")
    @Expose
    public String circle;
    @SerializedName("District")
    @Expose
    public String district;
    @SerializedName("Division")
    @Expose
    public String division;
    @SerializedName("Region")
    @Expose
    public String region;
    @SerializedName("Block")
    @Expose
    public String block;
    @SerializedName("State")
    @Expose
    public String state;
    @SerializedName("Country")
    @Expose
    public String country;
    @SerializedName("Pincode")
    @Expose
    public String pincode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getBranchType() {
        return branchType;
    }

    public void setBranchType(String branchType) {
        this.branchType = branchType;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

}
