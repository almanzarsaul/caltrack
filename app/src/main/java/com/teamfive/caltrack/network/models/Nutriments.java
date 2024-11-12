package com.teamfive.caltrack.network.models;

import com.google.gson.annotations.SerializedName;

// Nutriments.java
public class Nutriments {
    @SerializedName("energy")
    private float energy;

    @SerializedName("fat")
    private float fat;

    @SerializedName("sodium")
    private float sodium;

    public float getEnergy() {
        return energy;
    }

    public float getFat() {
        return fat;
    }

    public float getSodium() {
        return sodium;
    }
}
