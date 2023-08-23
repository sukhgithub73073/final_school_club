
package com.op.eschool.models.register_all_data_model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("OcupationArray")
    @Expose
    private List<OcupationArray> ocupationArray;
    @SerializedName("CasteArray")
    @Expose
    private List<CasteArray> casteArray;
    @SerializedName("ReligionArray")
    @Expose
    private List<ReligionArray> religionArray;

    public List<OcupationArray> getOcupationArray() {
        return ocupationArray;
    }

    public void setOcupationArray(List<OcupationArray> ocupationArray) {
        this.ocupationArray = ocupationArray;
    }

    public List<CasteArray> getCasteArray() {
        return casteArray;
    }

    public void setCasteArray(List<CasteArray> casteArray) {
        this.casteArray = casteArray;
    }

    public List<ReligionArray> getReligionArray() {
        return religionArray;
    }

    public void setReligionArray(List<ReligionArray> religionArray) {
        this.religionArray = religionArray;
    }

}
