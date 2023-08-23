
package com.op.eschool.models.class_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ClassGroupModel {

    @SerializedName("GroupId")
    @Expose
    private String groupId;
    @SerializedName("GroupName")
    @Expose
    private String groupName;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("classList")
    @Expose
    private List<ClassModel> classList= new ArrayList<>() ;


    public ClassGroupModel(String groupId, String groupName, String status, List<ClassModel> classList) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.status = status;
        this.classList = classList;
    }

    public List<ClassModel> getClassList() {
        return classList==null?new ArrayList<>():classList ;
    }

    public void setClassList(List<ClassModel> classList) {
        this.classList = classList;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getStatus() {
        return status==null?"":status ;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
