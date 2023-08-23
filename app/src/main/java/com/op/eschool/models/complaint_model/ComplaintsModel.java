
package com.op.eschool.models.complaint_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComplaintsModel {

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
    @SerializedName("CmplntTo")
    @Expose
    private String cmplntTo;
    @SerializedName("CmplntMsg")
    @Expose
    private String cmplntMsg;
    @SerializedName("CmplntSubMsg")
    @Expose
    private String cmplntSubMsg;
    @SerializedName("ReplyMsg")
    @Expose
    private String replyMsg;
    @SerializedName("ComplaintId")
    @Expose
    private String complaintId;
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

    public String getCmplntTo() {
        return cmplntTo;
    }

    public void setCmplntTo(String cmplntTo) {
        this.cmplntTo = cmplntTo;
    }

    public String getCmplntMsg() {
        return cmplntMsg;
    }

    public void setCmplntMsg(String cmplntMsg) {
        this.cmplntMsg = cmplntMsg;
    }

    public String getCmplntSubMsg() {
        return cmplntSubMsg;
    }

    public void setCmplntSubMsg(String cmplntSubMsg) {
        this.cmplntSubMsg = cmplntSubMsg;
    }

    public String getReplyMsg() {
        return replyMsg;
    }

    public void setReplyMsg(String replyMsg) {
        this.replyMsg = replyMsg;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
