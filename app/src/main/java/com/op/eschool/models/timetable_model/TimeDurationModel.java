package com.op.eschool.models.timetable_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeDurationModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("DurationName")
    @Expose
    private String durationName;
    @SerializedName("DurationTime")
    @Expose
    private String durationTime;
    @SerializedName("StartingTime")
    @Expose
    private String startingTime;
    @SerializedName("TimeDurationId")
    @Expose
    private String timeDurationId;
    @SerializedName("DurationSeries")
    @Expose
    private String durationSeries;
    @SerializedName("type")
    @Expose
    private String type;

    public String getStatus() {
        return status;
    }

    public void setStaus(String staus) {
        this.status = staus;
    }

    public String getDurationName() {
        return durationName;
    }

    public void setDurationName(String durationName) {
        this.durationName = durationName;
    }

    public String getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(String durationTime) {
        this.durationTime = durationTime;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getTimeDurationId() {
        return timeDurationId;
    }

    public void setTimeDurationId(String timeDurationId) {
        this.timeDurationId = timeDurationId;
    }

    public String getDurationSeries() {
        return durationSeries;
    }

    public void setDurationSeries(String durationSeries) {
        this.durationSeries = durationSeries;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
