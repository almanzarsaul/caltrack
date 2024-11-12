package com.teamfive.caltrack.network.models;

// Product.java
import com.google.gson.annotations.SerializedName;


public class Product {
    @SerializedName("product_name")
    private String name;

    @SerializedName("nutriments")
    private Nutriments nutriments;

    public String getName() {
        return name;
    }

    public Nutriments getNutriments() {
        return nutriments;
    }
}
