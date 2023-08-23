
package com.op.eschool.models.login_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginUserModel {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("Msg")
    @Expose
    public String msg;
    @SerializedName("Type")
    @Expose
    public String type;
    @SerializedName("CollageId")
    @Expose
    public String collageId;
    @SerializedName("StaffId")
    @Expose
    public String staffId;
    @SerializedName("StudentId")
    @Expose
    public String studentId;
    @SerializedName("CollageName")
    @Expose
    public String collageName;
    @SerializedName("CollageCode")
    @Expose
    public String collageCode;
    @SerializedName("MobileNumber")
    @Expose
    public String mobileNumber;
    @SerializedName("EmailId")
    @Expose
    public String emailId;
    @SerializedName("Image")
    @Expose
    public String image;
    @SerializedName("ImageExt")
    @Expose
    public String imageExt;
    @SerializedName("CollageUnqid")
    @Expose
    public String collageUnqid;

    @SerializedName("UserMobileNumber")
    @Expose
    public String UserMobileNumber;

    @SerializedName("UserEmailId")
    @Expose
    public String UserEmailId;

    @SerializedName("UserUniqeId")
    @Expose
    public String UserUniqeId;

    @SerializedName("UserImage")
    @Expose
    public String UserImage;
    @SerializedName("FullName")
    @Expose
    public String FullName;

    public String getUserMobileNumber() {
        return UserMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        UserMobileNumber = userMobileNumber;
    }

    public String getUserEmailId() {
        return UserEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        UserEmailId = userEmailId;
    }

    public String getUserUniqeId() {
        return UserUniqeId;
    }

    public void setUserUniqeId(String userUniqeId) {
        UserUniqeId = userUniqeId;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCollageId() {
        return collageId;
    }

    public void setCollageId(String collageId) {
        this.collageId = collageId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCollageName() {
        return collageName;
    }

    public void setCollageName(String collageName) {
        this.collageName = collageName;
    }

    public String getCollageCode() {
        return collageCode;
    }

    public void setCollageCode(String collageCode) {
        this.collageCode = collageCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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

    public String getCollageUnqid() {
        return collageUnqid;
    }

    public void setCollageUnqid(String collageUnqid) {
        this.collageUnqid = collageUnqid;
    }

}
