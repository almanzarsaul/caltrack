package com.teamfive.caltrack.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.teamfive.caltrack.database.entities.DailyLog;
import com.teamfive.caltrack.database.entities.FoodLog;
import com.teamfive.caltrack.database.entities.Goals;
import com.teamfive.caltrack.database.repositories.DailyLogsRepository;
import com.teamfive.caltrack.database.repositories.FoodLogRepository;

import java.util.Date;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final DailyLogsRepository dailyLogsRepository;
    private final FoodLogRepository foodLogRepository;
    private final LiveData<List<DailyLog>> allDailyLogs;

    public HomeViewModel(Application application) {
        super(application);
        dailyLogsRepository = new DailyLogsRepository(application);
        foodLogRepository = new FoodLogRepository(application);
        allDailyLogs = dailyLogsRepository.getAllDailyLogs();
    }


    public LiveData<DailyLog> getOrCreateDailyLog(Date date, Goals activeGoal) {
        return dailyLogsRepository.getOrCreateDailyLog(date, activeGoal);
    }

    public LiveData<List<DailyLog>> getAllDailyLogs() {
        return allDailyLogs;
    }
    public LiveData<List<FoodLog>> getFoodLogsByDate(Date date) {
        return foodLogRepository.getLogsByDate(date);
    }

}