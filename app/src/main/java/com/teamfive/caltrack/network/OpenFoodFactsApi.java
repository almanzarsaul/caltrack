package com.teamfive.caltrack.network;

import com.teamfive.caltrack.network.models.ProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// OpenFoodFactsApi.java
public interface OpenFoodFactsApi {
    @GET("api/v3/product/{barcode}.json")
    Call<ProductResponse> getProduct(@Path("barcode") String barcode);
}

