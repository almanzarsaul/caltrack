package com.teamfive.caltrack;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

public class FoodRepository {

    private static FoodRepository instance;
    private List<Food> mFoods;

    public static FoodRepository getInstance(Context context) {
        if (instance == null) {
            instance = new FoodRepository(context);
        }
        return instance;
    }

    private FoodRepository(Context context) {
        mFoods = new ArrayList<>();
        Resources res = context.getResources();
        String[] foods = res.getStringArray(R.array.foods);
        String[] calories = res.getStringArray(R.array.calories);
        int[] images = {
                R.drawable.banana,
                R.drawable.apple,
                R.drawable.cabbage,
                //Add more drawable resources for each food item
        };
        for (int i = 0; i < foods.length; i++) {
            int imageResource = (i < images.length) ? images[i] : R.drawable.food_default;//default if not enough images
            mFoods.add(new Food(i + 1, foods[i], calories[i], imageResource));
        }
    }

    public List<Food> getFoods() {
        return mFoods;
    }

    public Food getFood(int foodId) {
        for (Food food : mFoods) {
            if (food.getId() == foodId) {
                return food;
            }
        }
        return null;
    }
}