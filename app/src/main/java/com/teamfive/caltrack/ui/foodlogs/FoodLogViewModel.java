package com.teamfive.caltrack.ui.foodlogs;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.teamfive.caltrack.database.entities.FoodLog;
import com.teamfive.caltrack.database.repositories.FoodLogRepository;

import java.util.Date;
import java.util.List;

public class FoodLogViewModel extends AndroidViewModel {
    private final FoodLogRepository repository;
    private LiveData<List<FoodLog>> foodLogs;

    public FoodLogViewModel(@NonNull Application application) {
        super(application);
        repository = new FoodLogRepository(application);
    }

    public LiveData<List<FoodLog>> getAllFoodLogs() {
        foodLogs = repository.getAllFoodLogs();
        return foodLogs;
    }

    public LiveData<List<FoodLog>> getLogsByDate(Date date) {
        foodLogs = repository.getLogsByDate(date);
        return foodLogs;
    }

    public void insert(FoodLog foodLog) {
        repository.insert(foodLog);
    }

    public void deleteLog(int id) {
        repository.deleteLog(id);
    }
}