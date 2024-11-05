package com.teamfive.caltrack;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.teamfive.caltrack.database.AppDatabase;
import com.teamfive.caltrack.database.entities.DailyLog;
import com.teamfive.caltrack.database.entities.Goals;
import com.teamfive.caltrack.repository.DailyLogsRepository;
import com.teamfive.caltrack.database.repositories.GoalsRepository;
import com.teamfive.caltrack.HomeScreenFragment;
import com.teamfive.caltrack.ui.GoalSelectionFragment;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private GoalsRepository goalsRepository;
    private DailyLogsRepository dailyLogsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goalsRepository = new GoalsRepository(getApplication());
        dailyLogsRepository = new DailyLogsRepository(getApplication());

        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            Goals activeGoal = goalsRepository.getActiveGoal();

            if (activeGoal == null) {
                Log.d("MainActivity", "No active goal found, navigating to goal selection.");
                // No active goal, navigate to Goal Selection Fragment
                runOnUiThread(this::navigateToGoalSelection);

            } else {
                Log.d("MainActivity", "Active goal exists, proceeding with creating daily log.");
                // Active goal exists, create a daily log for today and navigate to Home Screen
                runOnUiThread(this::createDailyLogAndNavigateHome);
            }

        });


    }

    private void navigateToGoalSelection() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new GoalSelectionFragment())
                .commit();
    }

    private void createDailyLogAndNavigateHome() {
        Date today = new Date();
        dailyLogsRepository.getOrCreateDailyLog(today).observe(this, dailyLog -> {
            if (dailyLog != null) {
                Log.d("MainActivity", "Daily log created or retrieved for today.");
                Log.d("MainActivity", dailyLog.toString());
                navigateToHomeScreen();
            } else {
                Log.d("MainActivity", "There was an issue creating the daily log.");
            }
        });
    }

    private void navigateToHomeScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeScreenFragment())
                .commit();
    }
}