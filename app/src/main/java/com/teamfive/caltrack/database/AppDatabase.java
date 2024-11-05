package com.teamfive.caltrack.database;// AppDatabase.java
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.teamfive.caltrack.database.dao.DailyLogsDao;
import com.teamfive.caltrack.database.dao.FoodLogsDao;
import com.teamfive.caltrack.database.dao.GoalsDao;
import com.teamfive.caltrack.database.entities.DailyLog;
import com.teamfive.caltrack.database.entities.FoodLog;
import com.teamfive.caltrack.database.entities.Goals;
import com.teamfive.caltrack.database.helpers.TimestampConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DailyLog.class, FoodLog.class, Goals.class}, version = 1)
@TypeConverters({TimestampConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public abstract DailyLogsDao dailyLogDao();
    public abstract FoodLogsDao foodLogDao();
    public abstract GoalsDao goalsDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }
}

