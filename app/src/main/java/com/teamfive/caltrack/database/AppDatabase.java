package com.teamfive.caltrack.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.teamfive.caltrack.database.dao.DailyLogsDao;
import com.teamfive.caltrack.database.dao.FoodLogsDao;
import com.teamfive.caltrack.database.dao.GoalsDao;
import com.teamfive.caltrack.database.entities.DailyLog;
import com.teamfive.caltrack.database.entities.FoodLog;
import com.teamfive.caltrack.database.entities.Goals;
import com.teamfive.caltrack.database.helpers.Converters;


@Database(entities = {DailyLog.class, FoodLog.class, Goals.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract DailyLogsDao dailyLogsDao();
    public abstract FoodLogsDao foodLogsDao();
    public abstract GoalsDao goalsDao();
}
