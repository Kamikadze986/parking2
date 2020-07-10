package com.example.myapplication.retrofit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step {
    @SerializedName("end_location")
    @Expose
    private EndLocation endLocation;

    public EndLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(EndLocation endLocation) {
        this.endLocation = endLocation;
    }
}
