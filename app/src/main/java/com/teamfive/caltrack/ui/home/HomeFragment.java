package com.teamfive.caltrack.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kizitonwose.calendar.core.WeekDay;
import com.kizitonwose.calendar.view.WeekCalendarView;
import com.kizitonwose.calendar.view.WeekDayBinder;
import com.teamfive.caltrack.DayViewContainer;
import com.teamfive.caltrack.R;
import com.teamfive.caltrack.database.entities.DailyLog;
import com.teamfive.caltrack.database.entities.FoodLog;
import com.teamfive.caltrack.database.repositories.DailyLogsRepository;
import com.teamfive.caltrack.viewmodel.GoalsViewModel;
import com.teamfive.caltrack.viewmodel.HomeViewModel;
import com.teamfive.caltrack.views.ProgressIndicatorView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    private LocalDate activeDate;
    private final LocalDate today = LocalDate.now();
    private HomeViewModel homeViewModel;
    private Map<LocalDate, DailyLog> dailyLogMap = new HashMap<>();

    private WeekCalendarView weekCalendarView;
    private TextView weekTitle, noteText;
    private ProgressIndicatorView caloriesProgressIndicator, proteinProgressIndicator, carbsProgressIndicator, fatProgressIndicator;
    private FoodAdapter foodAdapter;

    private boolean isDailyLogFetched = false;
    private boolean isFoodLogsFetched = false;
    private DailyLog selectedLog;
    private GoalsViewModel goalsViewModel;

    private DailyLogsRepository dailyLogsRepository;

    private FloatingActionButton newFoodLogFAB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        dailyLogsRepository = new DailyLogsRepository(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ImageButton editNoteButton = view.findViewById(R.id.editNoteButton);
        newFoodLogFAB = view.findViewById(R.id.new_log);
        newFoodLogFAB.setOnClickListener(v -> {

            DailyLog currentDailyLog = null;
            if (!dailyLogMap.isEmpty()) {
                currentDailyLog = dailyLogMap.get(activeDate);
            }

            if (currentDailyLog != null) {
                NavController navController = Navigation.findNavController(view);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dailyLog", currentDailyLog);
                navController.navigate(R.id.action_home_to_new_log, bundle);
            } else {
            }

        });
        editNoteButton.setOnClickListener(v -> showTextInputDialog());

        goalsViewModel.getActiveGoal().observe(getViewLifecycleOwner(), goal -> {
            if (goal != null) {
                Log.d("HomeFragment", "Active goal found in HomeFragment: " + goal.toString());
                homeViewModel.getOrCreateDailyLog(new Date(), goal).observe(getViewLifecycleOwner(), dailyLog -> {
                    if (dailyLog != null) {
                        Log.d("HomeFragment", "Daily log created or retrieved for today.");
                    } else {
                        Log.d("HomeFragment", "There was an issue creating the daily log.");
                    }
                });
            } else {
                Log.d("HomeFragment", "No active goal found in HomeFragment.");
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUIComponents(view);
        setupViewModel();
        setupCalendar();

        // Set initial activeDate to today and update UI
        activeDate = today;
        updateUIForDate(activeDate);
    }

    private void setupUIComponents(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.food_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodAdapter = new FoodAdapter();
        recyclerView.setAdapter(foodAdapter);

        noteText = view.findViewById(R.id.note_text);
        caloriesProgressIndicator = view.findViewById(R.id.calories_progress_indicator);
        proteinProgressIndicator = view.findViewById(R.id.protein_progress_indicator);
        carbsProgressIndicator = view.findViewById(R.id.carbs_progress_indicator);
        fatProgressIndicator = view.findViewById(R.id.fat_progress_indicator);

        weekCalendarView = view.findViewById(R.id.weekCalendarView);
        weekTitle = view.findViewById(R.id.weekTitle);
    }

    private void showTextInputDialog() {
        DailyLog currentDailyLog = dailyLogMap.get(activeDate);
        String currentNote = "";

        if (currentDailyLog.getJournal() != null) {
            currentNote = currentDailyLog.getJournal();
        }

        EditText editText = new EditText(getContext());
        editText.setHint("Enter text here");
        editText.setText(currentNote);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Edit note")
                .setView(editText)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String enteredText = editText.getText().toString();
                    currentDailyLog.setJournal(enteredText);
                    dailyLogsRepository.updateDailyLog(currentDailyLog);
                    noteText.setText(enteredText);
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }

    private void setupViewModel() {
        homeViewModel.getAllDailyLogs().observe(getViewLifecycleOwner(), dailyLogs -> {
            if (dailyLogs != null) {
                dailyLogMap.clear();
                for (DailyLog dailyLog : dailyLogs) {
                    LocalDate logDate = dailyLog.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    dailyLogMap.put(logDate, dailyLog);
                }
                setupCalendarDates();
                updateUIForDate(today);
            }
        });
    }

    private void setupCalendar() {
        weekCalendarView.setDayBinder(new WeekDayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, WeekDay weekDay) {
                LocalDate date = weekDay.getDate();
                container.textView.setText(String.valueOf(date.getDayOfMonth()));

                if (!dailyLogMap.containsKey(date) || date.isAfter(today)) {
                    disableDate(container);
                } else {
                    enableDate(container, date);
                }
            }
        });

        weekCalendarView.setWeekScrollListener(week -> {
            LocalDate firstDayOfWeek = week.getDays().get(0).getDate();
            if (!firstDayOfWeek.isAfter(today)) {
                updateWeekTitle(firstDayOfWeek);
            }
            return null;
        });
    }

    private void setupCalendarDates() {
        LocalDate earliestDate = dailyLogMap.keySet().stream()
                .min(LocalDate::compareTo)
                .orElse(today);
        weekCalendarView.setup(earliestDate, today, DayOfWeek.SUNDAY);
        weekCalendarView.scrollToWeek(activeDate);
        updateWeekTitle(activeDate);
    }

    private void updateUIForDate(LocalDate date) {
        isDailyLogFetched = false;
        isFoodLogsFetched = false;

        selectedLog = dailyLogMap.get(date);
        isDailyLogFetched = true;
        updateUI(0, 0, 0, 0);

        homeViewModel.getFoodLogsByDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .observe(getViewLifecycleOwner(), foodLogs -> {
                    foodAdapter.updateFoods(foodLogs);

                    int totalCalories = 0;
                    int totalProtein = 0;
                    int totalFat = 0;
                    int totalCarbs = 0;

                    for (FoodLog foodLog : foodLogs) {
                        totalCalories += foodLog.getCalories();
                        totalProtein += foodLog.getProtein();
                        totalFat += foodLog.getFat();
                        totalCarbs += foodLog.getCarbs();
                    }

                    isFoodLogsFetched = true;
                    updateUI(totalCalories, totalProtein, totalCarbs, totalFat);
                });
    }

    private void updateUI(int totalCalories, int totalProtein, int totalCarbs, int totalFat) {
        if (isDailyLogFetched && isFoodLogsFetched) {
            if (selectedLog != null) {
                noteText.setText(selectedLog.getJournal());
                caloriesProgressIndicator.updateGoalUI(totalCalories, selectedLog.getGoalCalories());
                proteinProgressIndicator.updateGoalUI(totalProtein, selectedLog.getGoalProtein());
                carbsProgressIndicator.updateGoalUI(totalCarbs, selectedLog.getGoalCarbs());
                fatProgressIndicator.updateGoalUI(totalFat, selectedLog.getGoalFat());
            } else {
                resetUI();
            }
        }
    }

    private void resetUI() {
        noteText.setText("");
        caloriesProgressIndicator.updateGoalUI(0, 2000);
        proteinProgressIndicator.updateGoalUI(0, 50);
        carbsProgressIndicator.updateGoalUI(0, 150);
        fatProgressIndicator.updateGoalUI(0, 500);
    }

    private void updateWeekTitle(LocalDate date) {
        LocalDate startOfWeek = date.with(DayOfWeek.SUNDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        weekTitle.setText(String.format("%s - %s",
                startOfWeek.format(DateTimeFormatter.ofPattern("MMM d")),
                endOfWeek.format(DateTimeFormatter.ofPattern("MMM d"))));
    }

    private void disableDate(DayViewContainer container) {
        container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray));
        container.textView.setEnabled(false);
        container.textView.setBackground(null);
        container.textView.setOnClickListener(null);
    }

    private void enableDate(DayViewContainer container, LocalDate date) {
        container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        container.textView.setEnabled(true);

        if (date.equals(activeDate)) {
            container.textView.setBackgroundResource(R.drawable.active_date_background);
            container.textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else {
            container.textView.setBackground(null);
        }

        container.textView.setOnClickListener(v -> {
            if (!activeDate.equals(date)) {
                activeDate = date;
                weekCalendarView.notifyCalendarChanged();
                updateUIForDate(activeDate);
            }
        });
    }
}