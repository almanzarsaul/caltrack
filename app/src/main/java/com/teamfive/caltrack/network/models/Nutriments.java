package com.teamfive.caltrack.network.models;

import com.google.gson.annotations.SerializedName;

// Nutriments.java
public class Nutriments {
    @SerializedName("energy")
    private float energy;

    public float getEnergy() {
        return energy;
    }
}
