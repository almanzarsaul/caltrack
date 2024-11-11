package com.teamfive.caltrack.database.repositories;// FoodLogRepository.java

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.teamfive.caltrack.NutritionTotals;
import com.teamfive.caltrack.database.AppDatabase;
import com.teamfive.caltrack.database.dao.FoodLogsDao;
import com.teamfive.caltrack.database.entities.FoodLog;
import com.teamfive.caltrack.database.helpers.DateHelper;

import java.util.Date;
import java.util.List;

public class FoodLogRepository {
    private FoodLogsDao foodLogDao;

    public FoodLogRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        foodLogDao = db.foodLogDao();
    }

    public LiveData<List<FoodLog>> getAllFoodLogs() {
        return foodLogDao.getAllFoodLogs();
    }

    public LiveData<List<FoodLog>> getLogsByDate(Date date) {
        // Required to use range as the date field for logs includes timestamp data.
        Date startOfDay = DateHelper.getStartOfDay(date);
        Date endOfDay = DateHelper.getEndOfDay(date);
        return foodLogDao.getFoodLogsByDate(startOfDay, endOfDay);
    }

    public void insert(final FoodLog foodLog) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> foodLogDao.insertFoodLog(foodLog));
    }

    public void deleteLog(final int id) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> foodLogDao.deleteFoodLog(id));
    }

    public LiveData<NutritionTotals> getTotalNutritionByDate(Date date) {
        Date startOfDay = DateHelper.getStartOfDay(date);
        Date endOfDay = DateHelper.getEndOfDay(date);

        // Custom query to get totals for the day
        return foodLogDao.getNutritionTotalsByDate(startOfDay, endOfDay);
    }


}
