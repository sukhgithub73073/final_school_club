
package com.op.eschool.models.chatboat_model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatboatModel {

    @SerializedName("Unqid")
    @Expose
    private String unqid;
    @SerializedName("MessageId")
    @Expose
    private String MessageId;
    @SerializedName("Response")
    @Expose
    private String message;
    @SerializedName("MessageFrom")
    @Expose
    private String MessageFrom;
    @SerializedName("susg")
    @Expose
    private List<Suggest> suggests;

    public ChatboatModel(String unqid, String message, String messageFrom, List<Suggest> suggests) {
        this.unqid = unqid;
        this.message = message;
        MessageFrom = messageFrom;
        this.suggests = suggests;
    }

    public String getMessageFrom() {
        return MessageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        MessageFrom = messageFrom;
    }

    public String getUnqid() {
        return unqid;
    }

    public void setUnqid(String unqid) {
        this.unqid = unqid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Suggest> getSuggests() {
        return suggests;
    }

    public void setSuggests(List<Suggest> suggests) {
        this.suggests = suggests;
    }

}
