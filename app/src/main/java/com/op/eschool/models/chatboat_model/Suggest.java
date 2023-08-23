
package com.op.eschool.models.chatboat_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Suggest {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("Message")
    @Expose
    private String message;

    public Suggest(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
