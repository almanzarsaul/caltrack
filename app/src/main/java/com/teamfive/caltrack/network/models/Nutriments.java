package com.teamfive.caltrack.network.models;

import com.google.gson.annotations.SerializedName;

public class Nutriments {

    // Fat
    @SerializedName("fat_100g")
    private Double fat100g;

    @SerializedName("fat")
    private Double fatServing;

    // Protein
    @SerializedName("proteins_100g")
    private Double proteins100g;

    @SerializedName("proteins")
    private Double proteinsServing;

    // Carbohydrates
    @SerializedName("carbohydrates_100g")
    private Double carbohydrates100g;

    @SerializedName("carbohydrates")
    private Double carbohydratesServing;

    // Calories (Energy in kcal)
    @SerializedName("energy-kcal_100g")
    private Double energyKcal100g;

    @SerializedName("energy-kcal")
    private Double energyKcalServing;

    // Getters and Setters

    // Fat
    public Double getFat100g() {
        return fat100g;
    }

    public void setFat100g(Double fat100g) {
        this.fat100g = fat100g;
    }

    public Double getFatServing() {
        return fatServing;
    }

    public void setFatServing(Double fatServing) {
        this.fatServing = fatServing;
    }

    // Protein
    public Double getProteins100g() {
        return proteins100g;
    }

    public void setProteins100g(Double proteins100g) {
        this.proteins100g = proteins100g;
    }

    public Double getProteinsServing() {
        return proteinsServing;
    }

    public void setProteinsServing(Double proteinsServing) {
        this.proteinsServing = proteinsServing;
    }

    // Carbohydrates
    public Double getCarbohydrates100g() {
        return carbohydrates100g;
    }

    public void setCarbohydrates100g(Double carbohydrates100g) {
        this.carbohydrates100g = carbohydrates100g;
    }

    public Double getCarbohydratesServing() {
        return carbohydratesServing;
    }

    public void setCarbohydratesServing(Double carbohydratesServing) {
        this.carbohydratesServing = carbohydratesServing;
    }

    // Calories
    public Double getEnergyKcal100g() {
        return energyKcal100g;
    }

    public void setEnergyKcal100g(Double energyKcal100g) {
        this.energyKcal100g = energyKcal100g;
    }

    public Double getEnergyKcalServing() {
        return energyKcalServing;
    }

    public void setEnergyKcalServing(Double energyKcalServing) {
        this.energyKcalServing = energyKcalServing;
    }
}