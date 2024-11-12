package com.teamfive.caltrack.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.teamfive.caltrack.NutritionTotals;
import com.teamfive.caltrack.database.entities.FoodLog;

import java.util.Date;
import java.util.List;

@Dao
public interface FoodLogsDao {

    @Query("SELECT * FROM food_logs")
    LiveData<List<FoodLog>> getAllFoodLogs();

    /**
     * This method inserts a new food log into the database.
     * @param foodLog The food log to be inserted.
     */
    @Insert
    void insertFoodLog(FoodLog foodLog);

    /**
     * This method returns a list of food logs for a given date.
     * @param date The date for which to retrieve the food logs.
     * @return A list of food logs for the given date.
     */
    @Query("SELECT * FROM food_logs WHERE date >= :startOfDay AND date < :endOfDay ORDER BY date desc")
    LiveData<List<FoodLog>> getFoodLogsByDate(Date startOfDay, Date endOfDay);

    /** 
     * This method deletes a food log from the database.
     * @param id The food log id to be deleted.
     */
    @Query("DELETE FROM food_logs WHERE id = :id")
    void deleteFoodLog(int id);

    @Query("SELECT SUM(calories) AS totalCalories, SUM(protein) AS totalProtein, SUM(fat) AS totalFat, SUM(carbs) AS totalCarbs " +
            "FROM food_logs WHERE date >= :startOfDay AND date < :endOfDay")
    LiveData<NutritionTotals> getNutritionTotalsByDate(Date startOfDay, Date endOfDay);
}
