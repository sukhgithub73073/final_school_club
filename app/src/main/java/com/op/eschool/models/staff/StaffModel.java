
package com.op.eschool.models.staff;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StaffModel {

    @SerializedName("status")
    @Expose
    public String status;


    @SerializedName("imgLink2")
    @Expose
    public String Image="";
    @SerializedName("ImageExt")
    @Expose
    public String ImageExt="";

    @SerializedName("Designation")
    @Expose
    public String Designation;

    @SerializedName("DesignationId")
    @Expose
    public String DesignationId;


    @SerializedName("AadharNo")
    @Expose
    public String aadharNo;
    @SerializedName("AccountHldr")
    @Expose
    public String accountHldr;
    @SerializedName("AccountNumber")
    @Expose
    public String accountNumber;
    @SerializedName("ActionStatus")
    @Expose
    public String actionStatus;
    @SerializedName("AlternateNumber")
    @Expose
    public String alternateNumber;
    @SerializedName("BankName")
    @Expose
    public String bankName;
    @SerializedName("CasteData")
    @Expose
    public String casteData;
    @SerializedName("CollageCode")
    @Expose
    public String collageCode;
    @SerializedName("CollageName")
    @Expose
    public String collageName;
    @SerializedName("ConfrAccountNumber")
    @Expose
    public String confrAccountNumber;
    @SerializedName("CreatedDate")
    @Expose
    public String createdDate;
    @SerializedName("DOB")
    @Expose
    public String dob;
    @SerializedName("District")
    @Expose
    public String district;
    @SerializedName("EmailId")
    @Expose
    public String emailId;
    @SerializedName("FatherName")
    @Expose
    public String fatherName;
    @SerializedName("FullName")
    @Expose
    public String fullName;
    @SerializedName("Gender")
    @Expose
    public String gender;
    @SerializedName("IfscCode")
    @Expose
    public String ifscCode;
    @SerializedName("MobileNumber")
    @Expose
    public String mobileNumber;
    @SerializedName("PinCode")
    @Expose
    public String pinCode;
    @SerializedName("Qualification")
    @Expose
    public String qualification;
    @SerializedName("RelegionData")
    @Expose
    public String relegionData;
    @SerializedName("StaffId")
    @Expose
    public String staffId;
    @SerializedName("State")
    @Expose
    public String state;
    @SerializedName("SubCasteData")
    @Expose
    public String subCasteData;
    @SerializedName("Tahsil")
    @Expose
    public String tahsil;
    @SerializedName("Unqid")
    @Expose
    public String unqid;
    @SerializedName("Villa_Mohalla")
    @Expose
    public String villaMohalla;
    @SerializedName("StaffUnqid")
    @Expose
    public String staffUnqid;
    @SerializedName("qr")
    @Expose
    public String qr;
    @SerializedName("type")
    @Expose
    public String type;



    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getDesignationId() {
        return DesignationId;
    }

    public void setDesignationId(String designationId) {
        DesignationId = designationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStaus(String staus) {
        this.status = staus;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getAccountHldr() {
        return accountHldr;
    }

    public void setAccountHldr(String accountHldr) {
        this.accountHldr = accountHldr;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCasteData() {
        return casteData;
    }

    public void setCasteData(String casteData) {
        this.casteData = casteData;
    }

    public String getCollageCode() {
        return collageCode;
    }

    public void setCollageCode(String collageCode) {
        this.collageCode = collageCode;
    }

    public String getCollageName() {
        return collageName;
    }

    public void setCollageName(String collageName) {
        this.collageName = collageName;
    }

    public String getConfrAccountNumber() {
        return confrAccountNumber;
    }

    public void setConfrAccountNumber(String confrAccountNumber) {
        this.confrAccountNumber = confrAccountNumber;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getRelegionData() {
        return relegionData;
    }

    public void setRelegionData(String relegionData) {
        this.relegionData = relegionData;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSubCasteData() {
        return subCasteData;
    }

    public void setSubCasteData(String subCasteData) {
        this.subCasteData = subCasteData;
    }

    public String getTahsil() {
        return tahsil;
    }

    public void setTahsil(String tahsil) {
        this.tahsil = tahsil;
    }

    public String getUnqid() {
        return unqid;
    }

    public void setUnqid(String unqid) {
        this.unqid = unqid;
    }

    public String getVillaMohalla() {
        return villaMohalla;
    }

    public void setVillaMohalla(String villaMohalla) {
        this.villaMohalla = villaMohalla;
    }

    public String getStaffUnqid() {
        return staffUnqid;
    }

    public void setStaffUnqid(String staffUnqid) {
        this.staffUnqid = staffUnqid;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
