package com.teamfive.caltrack.network.models;

import com.google.gson.annotations.SerializedName;

public class ProductResponse {

    @SerializedName("product")
    private Product product;

    // Getters and Setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}