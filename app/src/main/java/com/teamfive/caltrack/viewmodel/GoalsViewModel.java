package com.teamfive.caltrack.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.teamfive.caltrack.database.entities.Goals;
import com.teamfive.caltrack.database.repositories.GoalsRepository;

public class GoalsViewModel extends AndroidViewModel {

    private final GoalsRepository goalsRepository;
    private final LiveData<Goals> activeGoal;

    public GoalsViewModel(Application application) {
        super(application);
        goalsRepository = new GoalsRepository(application);
        activeGoal = goalsRepository.getActiveGoal();  // Directly observe LiveData from repository
    }

    public LiveData<Goals> getActiveGoal() {
        return activeGoal;
    }

    public void saveGoal(int calories, int carbs, int fat, int protein) {
        Goals newGoal = new Goals(calories, carbs, fat, protein);
        goalsRepository.replaceActiveGoal(newGoal);
    }
}