package com.teamfive.caltrack;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamfive.caltrack.R;
import com.teamfive.caltrack.database.entities.FoodLog;
import com.teamfive.caltrack.database.entities.Goals;
import com.teamfive.caltrack.database.repositories.GoalsRepository;
import com.teamfive.caltrack.ui.foodlogs.FoodLogViewModel;

import java.util.Date;
import java.util.List;

public class FoodLogActivity extends AppCompatActivity {
    private FoodLogViewModel foodLogViewModel;
    private FoodLogsAdapter adapter; // Custom adapter for displaying logs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_log);
        GoalsRepository goalsRepository = new GoalsRepository(getApplication());
//        Goals newGoal = new Goals(2000, 500, 50, 200);
//        goalsRepository.replaceActiveGoal(newGoal);

        if (goalsRepository.getActiveGoal() == null) {
            Log.d("LOGS", "No goal is set. Go to Goal fragment.");
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodLogsAdapter();
        recyclerView.setAdapter(adapter);

        foodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);

        // Observe and display food logs for the current date
        Date today = new Date();
        foodLogViewModel.getLogsByDate(today).observe(this, foodLogs -> {
            adapter.submitList(foodLogs); // Update the adapter
        });

        // Button to add a new food log
        findViewById(R.id.btnAddLog).setOnClickListener(view -> addNewFoodLog());

        // Button to delete the last food log (for demonstration)
        findViewById(R.id.btnDeleteLog).setOnClickListener(view -> deleteLastFoodLog());
    }

    private void addNewFoodLog() {
        // Create a new FoodLog (example data)
        FoodLog newLog = new FoodLog("Banana", 15, 0, 1, 1);
        foodLogViewModel.insert(newLog);
        Toast.makeText(this, "Food log added", Toast.LENGTH_SHORT).show();
    }

    private void deleteLastFoodLog() {
        List<FoodLog> foodLogs = adapter.getCurrentList();
        Log.d("LOGS", foodLogs.toString());
        if (!foodLogs.isEmpty()) {
            int lastId = foodLogs.get(foodLogs.size() - 1).getId();
            foodLogViewModel.deleteLog(lastId);
            Toast.makeText(this, "Last food log deleted", Toast.LENGTH_SHORT).show();
        }
    }
}