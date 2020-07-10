package com.example.myapplication.retrofit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Leg {
    @SerializedName("steps")
    @Expose
    private List<Step> steps;

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
