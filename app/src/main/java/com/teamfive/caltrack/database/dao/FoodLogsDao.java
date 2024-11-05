package com.teamfive.caltrack.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.teamfive.caltrack.database.entities.DailyLog;
import com.teamfive.caltrack.database.entities.FoodLog;

import java.util.Date;
import java.util.List;

@Dao
public interface FoodLogsDao {

    @Insert
    void insertFoodLog(FoodLog foodLog);

    @Query("SELECT * FROM food_logs WHERE date = :date")
    List<FoodLog> getFoodLogsByDate(Date date);

    @Delete
    void deleteDailyLog(DailyLog dailyLog);

}
