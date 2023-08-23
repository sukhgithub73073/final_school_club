
package com.op.eschool.models.leave_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveModel {

    @SerializedName("status")
    @Expose
    private String status1;
    @SerializedName("AttachmentStatus")
    @Expose
    private String attachmentStatus;
    @SerializedName("Attachment")
    @Expose
    private String attachment;
    @SerializedName("AttachmentExt")
    @Expose
    private String attachmentExt;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("LeaveTo")
    @Expose
    private String leaveTo;
    @SerializedName("LeaveMsg")
    @Expose
    private String leaveMsg;
    @SerializedName("LeaveSubMsg")
    @Expose
    private String leaveSubMsg;
    @SerializedName("FeedBackMsg")
    @Expose
    private String feedBackMsg;
    @SerializedName("LeaveId")
    @Expose
    private String leaveId;
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("CreatedDate")
    @Expose
    private String CreatedDate;

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getAttachmentStatus() {
        return attachmentStatus;
    }

    public void setAttachmentStatus(String attachmentStatus) {
        this.attachmentStatus = attachmentStatus;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentExt() {
        return attachmentExt;
    }

    public void setAttachmentExt(String attachmentExt) {
        this.attachmentExt = attachmentExt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeaveTo() {
        return leaveTo;
    }

    public void setLeaveTo(String leaveTo) {
        this.leaveTo = leaveTo;
    }

    public String getLeaveMsg() {
        return leaveMsg;
    }

    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;
    }

    public String getLeaveSubMsg() {
        return leaveSubMsg;
    }

    public void setLeaveSubMsg(String leaveSubMsg) {
        this.leaveSubMsg = leaveSubMsg;
    }

    public String getFeedBackMsg() {
        return feedBackMsg;
    }

    public void setFeedBackMsg(String feedBackMsg) {
        this.feedBackMsg = feedBackMsg;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
