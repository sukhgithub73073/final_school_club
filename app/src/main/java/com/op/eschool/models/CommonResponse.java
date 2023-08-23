package com.op.eschool.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonResponse {

    @SerializedName("Msg")
    @Expose
    public String Msg;

    @SerializedName("msg")
    @Expose
    public String short_msg;
    @SerializedName("Exists")
    @Expose
    public boolean Exists;

    @SerializedName("Type")
    @Expose
    public String Type;

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("staus")
    @Expose
    public String staus;

    @SerializedName("CollageId")
    @Expose
    public String CollageId;


    public String getStatus() {
        return status==null?
                staus==null?"":staus
                : status ;

    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return Msg==null?
                short_msg==null?"":short_msg
                : Msg ;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }
}
