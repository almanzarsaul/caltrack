// FoodLog.java
package com.teamfive.caltrack.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "food_logs")
public class FoodLog {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "calories")
    private int calories;

    @ColumnInfo(name = "carbs")
    private int carbs;

    @ColumnInfo(name = "fat")
    private int fat;

    @ColumnInfo(name = "protein")
    private int protein;

    /**
     * Constructor
     * @param name The name of the food.
     * @param calories The number of calories in the food.
     * @param carbs The number of carbs in the food.
     * @param fat The number of fat in the food.
     * @param protein The number of protein in the food.
     */
    public FoodLog(String name, int calories, int carbs, int fat, int protein) {
        this.name = name;
        this.date = new Date();
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getFat() {
        return fat;
    }

    public int getProtein() {
        return protein;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodLog foodLog = (FoodLog) o;
        return id == foodLog.id &&
                calories == foodLog.calories &&
                carbs == foodLog.carbs &&
                fat == foodLog.fat &&
                protein == foodLog.protein &&
                Objects.equals(name, foodLog.name) &&
                Objects.equals(date, foodLog.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, calories, carbs, fat, protein, date);
    }
}
