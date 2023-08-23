
package com.op.eschool.models.register_all_data_model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllInOneDtModel {

    @SerializedName("Data")
    @Expose
    private List<Datum> data;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
