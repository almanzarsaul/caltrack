package com.teamfive.caltrack.network.models;

import com.google.gson.annotations.SerializedName;

// Nutriments.java
public class Nutriments {
    @SerializedName("energy")
    private float energy;

    @SerializedName("fat")
    private float fat;

    @SerializedName("proteins")
    private float protein;

    @SerializedName("carbohydrates")
    private float carbs;

    public float getEnergy() {
        return energy;
    }

    public float getFat() {
        return fat;
    }

    public float getProtein() {
        return protein;
    }

    public float getCarbs() {
        return carbs;
    }
}
