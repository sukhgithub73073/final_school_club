
package com.op.eschool.models.student;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentModel {

    @SerializedName("Checked")
    @Expose
    public boolean Checked = false;
    @SerializedName("AadharNo")
    @Expose
    public String aadharNo;
    @SerializedName("ActionStatus")
    @Expose
    public String actionStatus="";
    @SerializedName("AlternateNumber")
    @Expose
    public String alternateNumber;
    @SerializedName("CasteData")
    @Expose
    public String casteData;

    @SerializedName("GroupName")
    @Expose
    public String GroupName="";
    @SerializedName("imgLink")
    @Expose
    public String Image="";
    @SerializedName("ImageExt")
    @Expose
    public String ImageExt="";
    @SerializedName("Class")
    @Expose
    public String _class;
    @SerializedName("ClassId")
    @Expose
    public String ClassId;
    @SerializedName("CollageCode")
    @Expose
    public String collageCode;
    @SerializedName("CollageName")
    @Expose
    public String collageName;
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
    @SerializedName("FatherOccupation")
    @Expose
    public String fatherOccupation;
    @SerializedName("FullName")
    @Expose
    public String fullName;
    @SerializedName("Gender")
    @Expose
    public String gender;
    @SerializedName("MobileNumber")
    @Expose
    public String mobileNumber;
    @SerializedName("MotherName")
    @Expose
    public String motherName;
    @SerializedName("MotherOccupation")
    @Expose
    public String motherOccupation;
    @SerializedName("PinCode")
    @Expose
    public String pinCode;
    @SerializedName("PreviousSchool")
    @Expose
    public String previousSchool;
    @SerializedName("RelegionData")
    @Expose
    public String relegionData;
    @SerializedName("State")
    @Expose
    public String state;
    @SerializedName("StudentId")
    @Expose
    public String studentId;
    @SerializedName("StudentUnqid")
    @Expose
    public String studentUnqid;
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
    @SerializedName("qr")
    @Expose
    public String qr;
    @SerializedName("staus")
    @Expose
    public String staus;
    @SerializedName("type")
    @Expose
    public String type;

    public boolean isChecked() {
        return Checked ;
    }

    public void setChecked(boolean checked) {
        Checked = checked;
    }
}
