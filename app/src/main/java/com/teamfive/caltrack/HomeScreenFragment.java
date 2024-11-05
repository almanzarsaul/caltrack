package com.teamfive.caltrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.teamfive.caltrack.R;
import com.teamfive.caltrack.database.entities.DailyLog;
import com.teamfive.caltrack.database.entities.FoodLog;
import com.teamfive.caltrack.repository.DailyLogsRepository;
import com.teamfive.caltrack.database.repositories.FoodLogRepository;

import java.util.Date;

public class HomeScreenFragment extends Fragment {

    private DailyLogsRepository dailyLogsRepository;
    private FoodLogRepository foodLogsRepository;
    private CalendarView calendarView;
    private TextView goalsTextView;
    private EditText journalEditText;
    private Button saveButton;
    private RecyclerView foodLogsRecyclerView;
    private FoodLogsAdapter foodLogsAdapter;
    private Date selectedDate;
    private DailyLog currentDailyLog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);

        dailyLogsRepository = new DailyLogsRepository(requireActivity().getApplication());
        foodLogsRepository = new FoodLogRepository(requireActivity().getApplication());

        calendarView = view.findViewById(R.id.calendar_view);
        goalsTextView = view.findViewById(R.id.goals_text_view);
        journalEditText = view.findViewById(R.id.journal_edit_text);
        saveButton = view.findViewById(R.id.save_button);
        foodLogsRecyclerView = view.findViewById(R.id.food_logs_recycler_view);

        foodLogsAdapter = new FoodLogsAdapter();
        foodLogsRecyclerView.setAdapter(foodLogsAdapter);

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            selectedDate = new Date(year - 1900, month, dayOfMonth); // Adjust Date initialization as needed
            loadDailyLog(selectedDate);
        });

        saveButton.setOnClickListener(v -> saveJournalEntry());

        // Load today's daily log by default
        selectedDate = new Date();
        loadDailyLog(selectedDate);

        return view;
    }

    private void loadDailyLog(Date date) {
        dailyLogsRepository.getOrCreateDailyLog(date).observe(getViewLifecycleOwner(), dailyLog -> {
            if (dailyLog != null) {
                currentDailyLog = dailyLog;
                goalsTextView.setText(formatGoals(dailyLog));
                journalEditText.setText(dailyLog.getJournal());
                loadFoodLogs(date);
            }
        });
    }

    private String formatGoals(DailyLog dailyLog) {
        return "Calories: " + dailyLog.getGoalCalories() + ", Carbs: " + dailyLog.getGoalCarbs() +
                ", Fat: " + dailyLog.getGoalFat() + ", Protein: " + dailyLog.getGoalProtein() + "DATE: " + dailyLog.getDate();
    }

    private void loadFoodLogs(Date date) {
        foodLogsRepository.getLogsByDate(date).observe(getViewLifecycleOwner(), foodLogs -> {
            foodLogsAdapter.submitList(foodLogs);
        });
    }

    private void saveJournalEntry() {
        if (currentDailyLog != null) {
            currentDailyLog.setJournal(journalEditText.getText().toString());
            dailyLogsRepository.updateDailyLog(currentDailyLog);
            Toast.makeText(getContext(), "Journal saved", Toast.LENGTH_SHORT).show();
        }
    }
}