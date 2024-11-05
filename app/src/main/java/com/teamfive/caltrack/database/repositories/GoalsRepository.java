package com.teamfive.caltrack.database.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.teamfive.caltrack.database.AppDatabase;
import com.teamfive.caltrack.database.dao.GoalsDao;
import com.teamfive.caltrack.database.entities.Goals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GoalsRepository {

    private final GoalsDao goalsDao;

    public GoalsRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        goalsDao = db.goalsDao();
    }

    /**
     * Sets a new goal, replacing the active goal if one exists.
     */
    public void replaceActiveGoal(Goals newGoal) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> goalsDao.replaceActiveGoal(newGoal));
    }

    /**
     * Retrieves the active goal if it exists.
     */
    public Goals getActiveGoal() {
        return goalsDao.getActiveGoal();
    }
}