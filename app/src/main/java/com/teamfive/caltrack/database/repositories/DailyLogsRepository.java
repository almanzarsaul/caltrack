package com.teamfive.caltrack.database.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.teamfive.caltrack.database.AppDatabase;
import com.teamfive.caltrack.database.dao.DailyLogsDao;
import com.teamfive.caltrack.database.entities.DailyLog;
import com.teamfive.caltrack.database.entities.Goals;

import java.util.Date;
import java.util.List;

public class DailyLogsRepository {

    private final DailyLogsDao dailyLogsDao;
    AppDatabase db;

    public DailyLogsRepository(Application application) {
        db = AppDatabase.getDatabase(application);
        dailyLogsDao = db.dailyLogDao();
    }

    public LiveData<DailyLog> getOrCreateDailyLog(Date date, Goals activeGoal) {
        MutableLiveData<DailyLog> dailyLogLiveData = new MutableLiveData<>();

        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            if (activeGoal != null) {
                Log.d("DailyLogsRepository", "Active goal found, calling getOrCreateDailyLog in DAO.");
                DailyLog dailyLog = dailyLogsDao.getOrCreateDailyLog(date, activeGoal);
                if (dailyLog != null) {
                    Log.d("DailyLogsRepository", "Daily log created or retrieved successfully.");
                    dailyLogLiveData.postValue(dailyLog);
                } else {
                    Log.d("DailyLogsRepository", "Failed to create or retrieve daily log.");
                    dailyLogLiveData.postValue(null);
                }
            } else {
                Log.d("DailyLogsRepository", "No active goal found.");
                dailyLogLiveData.postValue(null);
            }
        });

        return dailyLogLiveData;
    }

    /**
     * Updates an existing daily log.
     * @param dailyLog The DailyLog object to be updated.
     */
    public void updateDailyLog(DailyLog dailyLog) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> dailyLogsDao.updateDailyLog(dailyLog));
    }

    public LiveData<List<DailyLog>> getAllDailyLogs() {
        return dailyLogsDao.getAllDailyLogs();
    }
}