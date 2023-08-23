package com.op.eschool.models.class_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SessionModel {
    @SerializedName("Session")
    @Expose
    private String Session;
    @SerializedName("SessionId")
    @Expose
    private String SessionId;
    @SerializedName("status")
    @Expose
    private String status;

    public String getSession() {
        return Session;
    }

    public void setSession(String session) {
        Session = session;
    }

    public String getSessionId() {
        return SessionId;
    }

    public void setSessionId(String sessionId) {
        SessionId = sessionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStaus(String staus) {
        this.status = staus;
    }
}
