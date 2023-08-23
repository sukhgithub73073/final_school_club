package com.op.eschool.models.school_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchoolModel {
    @SerializedName("Unqid")
    @Expose
    private String Unqid;
    @SerializedName("AadharCard")
    @Expose
    private String aadharCard;
    @SerializedName("ActionStatus")
    @Expose
    private String actionStatus;
    @SerializedName("AlternateNumber")
    @Expose
    private String alternateNumber;
    @SerializedName("CollageCode")
    @Expose
    private String collageCode;
    @SerializedName("CollageId")
    @Expose
    private String collageId;
    @SerializedName("CollageName")
    @Expose
    private String collageName;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("DOB")
    @Expose
    private String dob;
    @SerializedName("Designation")
    @Expose
    private String designation;
    @SerializedName("District")
    @Expose
    private String district;
    @SerializedName("EmailId")
    @Expose
    private String emailId;
    @SerializedName("FatherName")
    @Expose
    private String fatherName;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("ImageExt")
    @Expose
    private String imageExt;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("OwnerName")
    @Expose
    private String ownerName;
    @SerializedName("Pincode")
    @Expose
    private String pincode;
    @SerializedName("Qulification")
    @Expose
    private String qulification;
    @SerializedName("State")
    @Expose
    private String state;
    @SerializedName("Thesil")
    @Expose
    private String thesil;
    @SerializedName("Village_Mohalla")
    @Expose
    private String villageMohalla;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("uid")
    @Expose
    private String uid;

    public String getUnqid() {
        return Unqid;
    }

    public void setUnqid(String unqid) {
        Unqid = unqid;
    }

    public String getAadharCard() {
        return aadharCard;
    }

    public void setAadharCard(String aadharCard) {
        this.aadharCard = aadharCard;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getAlternateNumber() {
        return alternateNumber;
    }

    public void setAlternateNumber(String alternateNumber) {
        this.alternateNumber = alternateNumber;
    }

    public String getCollageCode() {
        return collageCode;
    }

    public void setCollageCode(String collageCode) {
        this.collageCode = collageCode;
    }

    public String getCollageId() {
        return collageId;
    }

    public void setCollageId(String collageId) {
        this.collageId = collageId;
    }

    public String getCollageName() {
        return collageName;
    }

    public void setCollageName(String collageName) {
        this.collageName = collageName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageExt() {
        return imageExt;
    }

    public void setImageExt(String imageExt) {
        this.imageExt = imageExt;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getQulification() {
        return qulification;
    }

    public void setQulification(String qulification) {
        this.qulification = qulification;
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

    public String getVillageMohalla() {
        return villageMohalla;
    }

    public void setVillageMohalla(String villageMohalla) {
        this.villageMohalla = villageMohalla;
    }

    public String getStatus() {
        return status;
    }

    public void setStaus(String staus) {
        this.status = staus;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
