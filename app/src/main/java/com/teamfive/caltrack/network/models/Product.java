package com.teamfive.caltrack.network.models;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("product_name")
    private String productName;

    @SerializedName("serving_size")
    private String servingSize;

    @SerializedName("nutriments")
    private Nutriments nutriments;

    // Getters and Setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }

    public Nutriments getNutriments() {
        return nutriments;
    }

    public void setNutriments(Nutriments nutriments) {
        this.nutriments = nutriments;
    }
}