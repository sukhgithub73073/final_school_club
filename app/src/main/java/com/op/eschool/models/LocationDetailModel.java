package com.op.eschool.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationDetailModel {

    @SerializedName("isMock")
    @Expose
    public boolean isMock;
    @SerializedName("Accuracy")
    @Expose
    public double accuracy;
    @SerializedName("Log")
    @Expose
    public double log;
    @SerializedName("Lat")
    @Expose
    public double lat;

    @SerializedName("netIsMock")
    @Expose
    public boolean netIsMock;
    @SerializedName("NetLog")
    @Expose
    public double netLog;
    @SerializedName("NetLat")
    @Expose
    public double netLat;
    @SerializedName("NetAccuracy")
    @Expose
    public double netAccuracy;

    public LocationDetailModel(boolean isMock , double accuracy, double lat, double log, boolean netIsMock , double netAccuracy, double netLat , double netLog) {
        this.isMock = isMock;
        this.netIsMock = netIsMock;
        this.accuracy = accuracy;
        this.log = log;
        this.lat = lat;
        this.netLog = netLog;
        this.netLat = netLat;
        this.netAccuracy = netAccuracy;
    }
    
}
