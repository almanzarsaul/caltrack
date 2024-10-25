package com.teamfive.caltrack.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.teamfive.caltrack.database.entities.DailyLog;

import java.util.Date;
import java.util.List;

@Dao
public interface DailyLogsDao {

    @Insert
    void insertDailyLog(DailyLog dailyLog);

    @Query("SELECT * FROM daily_logs WHERE date = :date")
    DailyLog getDailyLogByDate(Date date);

    @Delete
    void deleteDailyLog(DailyLog dailyLog);
}
