package com.teamfive.caltrack.database.dao;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.teamfive.caltrack.database.entities.DailyLog;
import com.teamfive.caltrack.database.entities.Goals;
import com.teamfive.caltrack.database.helpers.DateHelper;

import java.util.Date;
import java.util.List;

@Dao
public interface DailyLogsDao {

    @Insert
    void insertDailyLog(DailyLog dailyLog);

    @Update
    void updateDailyLog(DailyLog dailyLog);


    @Transaction
    default DailyLog getOrCreateDailyLog(Date date, Goals activeGoals) {
        Date normalizedDate = DateHelper.normalizeDate(date);
        DailyLog dailyLog = getDailyLogByDate(normalizedDate);
        if (dailyLog == null) {
            dailyLog = new DailyLog(date, activeGoals.getGoalCalories(), activeGoals.getGoalCarbs(), activeGoals.getGoalFat(), activeGoals.getGoalProtein());
            Log.d("DailyLogsDao", "Creating a new daily log for date: " + normalizedDate);
            insertDailyLog(dailyLog);
        } else {
            Log.d("DailyLogsDao", "Retrieved existing daily log for date: " + normalizedDate);
        }
        return dailyLog;
    }

    @Query("SELECT * FROM daily_logs WHERE date = :date")
    DailyLog getDailyLogByDate(Date date);

    @Delete
    void deleteDailyLog(DailyLog dailyLog);

    @Query("SELECT * FROM daily_logs")
    LiveData<List<DailyLog>> getAllDailyLogs();
}