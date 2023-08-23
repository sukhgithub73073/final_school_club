
package com.op.eschool.models.bankNames;




import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("bankId")
    @Expose
    private Integer bankId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ifscAlias")
    @Expose
    private String ifscAlias;
    @SerializedName("ifscGlobal")
    @Expose
    private String ifscGlobal;
    @SerializedName("neftEnabled")
    @Expose
    private Integer neftEnabled;
    @SerializedName("neftFailureRate")
    @Expose
    private String neftFailureRate;
    @SerializedName("impsEnabled")
    @Expose
    private Integer impsEnabled;
    @SerializedName("impsFailureRate")
    @Expose
    private String impsFailureRate;
    @SerializedName("upiEnabled")
    @Expose
    private Integer upiEnabled;
    @SerializedName("upiFailureRate")
    @Expose
    private String upiFailureRate;


    @SerializedName("iin")
    @Expose
    private String iin;

    @SerializedName("aepsEnabled")
    @Expose
    private boolean aepsEnabled;

    @SerializedName("aadhaarpayEnabled")
    @Expose
    private boolean aadhaarpayEnabled;

    @SerializedName("aepsFailureRate")
    @Expose
    private String aepsFailureRate;

    @SerializedName("aadhaarpayFailureRate")
    @Expose
    private String aadhaarpayFailureRate;

    public Datum(String name,String iin, boolean aepsEnabled, boolean aadhaarpayEnabled, String aepsFailureRate, String aadhaarpayFailureRate) {
        this.name = name;
        this.iin = iin;
        this.aepsEnabled = aepsEnabled;
        this.aadhaarpayEnabled = aadhaarpayEnabled;
        this.aepsFailureRate = aepsFailureRate;
        this.aadhaarpayFailureRate = aadhaarpayFailureRate;
    }

    public Datum(int id, String bankName) {
        bankId = id;
        this.name = bankName;
    }


    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public boolean isAepsEnabled() {
        return aepsEnabled;
    }

    public void setAepsEnabled(boolean aepsEnabled) {
        this.aepsEnabled = aepsEnabled;
    }

    public boolean isAadhaarpayEnabled() {
        return aadhaarpayEnabled;
    }

    public void setAadhaarpayEnabled(boolean aadhaarpayEnabled) {
        this.aadhaarpayEnabled = aadhaarpayEnabled;
    }

    public String getAepsFailureRate() {
        return aepsFailureRate;
    }

    public void setAepsFailureRate(String aepsFailureRate) {
        this.aepsFailureRate = aepsFailureRate;
    }

    public String getAadhaarpayFailureRate() {
        return aadhaarpayFailureRate;
    }

    public void setAadhaarpayFailureRate(String aadhaarpayFailureRate) {
        this.aadhaarpayFailureRate = aadhaarpayFailureRate;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return name==null?"":name;
    }


    public void setBankName(String name) {
        this.name = name;
    }

    public String getIfscAlias() {
        return ifscAlias;
    }

    public void setIfscAlias(String ifscAlias) {
        this.ifscAlias = ifscAlias;
    }

    public String getBranchIfsc() {
        return ifscGlobal;
    }

    public void setBranchIfsc(String ifscGlobal) {
        this.ifscGlobal = ifscGlobal;
    }

    public Integer getNeftEnabled() {
        return neftEnabled;
    }

    public void setNeftEnabled(Integer neftEnabled) {
        this.neftEnabled = neftEnabled;
    }

    public String getNeftFailureRate() {
        return neftFailureRate;
    }

    public void setNeftFailureRate(String neftFailureRate) {
        this.neftFailureRate = neftFailureRate;
    }

    public Integer getImpsEnabled() {
        return impsEnabled;
    }

    public void setImpsEnabled(Integer impsEnabled) {
        this.impsEnabled = impsEnabled;
    }

    public String getImpsFailureRate() {
        return impsFailureRate;
    }

    public void setImpsFailureRate(String impsFailureRate) {
        this.impsFailureRate = impsFailureRate;
    }

    public Integer getUpiEnabled() {
        return upiEnabled;
    }

    public void setUpiEnabled(Integer upiEnabled) {
        this.upiEnabled = upiEnabled;
    }

    public String getUpiFailureRate() {
        return upiFailureRate;
    }

    public void setUpiFailureRate(String upiFailureRate) {
        this.upiFailureRate = upiFailureRate;
    }
}
