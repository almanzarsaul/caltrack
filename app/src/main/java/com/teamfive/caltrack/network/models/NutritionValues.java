package com.teamfive.caltrack.network.models;

// Helper class to hold nutrition values
public class NutritionValues {
    private final float calories;
    private final float carbs;
    private final float fat;
    private final float protein;

    public NutritionValues(float calories, float carbs, float fat, float protein) {
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }

    public float getCalories() { return calories; }
    public float getCarbs() { return carbs; }
    public float getFat() { return fat; }
    public float getProtein() { return protein; }
}
