package com.teamfive.caltrack.ui.newlog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teamfive.caltrack.R;
import com.teamfive.caltrack.database.entities.DailyLog;
import com.teamfive.caltrack.database.entities.FoodLog;
import com.teamfive.caltrack.database.repositories.FoodLogRepository;

import java.time.LocalDate;
import java.time.ZoneId;

public class NewLogFragment extends Fragment {

    private DailyLog currentDailyLog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentDailyLog = (DailyLog) getArguments().getSerializable("dailyLog");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_log, container, false);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        Button scanBarcodeButton = view.findViewById(R.id.scan_barcode_button);
        scanBarcodeButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_newLogFragment_to_scanner);
        });
        Button saveLogButton = view.findViewById(R.id.save_button);

        // Initialize views
        EditText foodNameInput = view.findViewById(R.id.food_name_input);
        EditText foodCaloriesInput = view.findViewById(R.id.food_calories_input);
        EditText foodProteinInput = view.findViewById(R.id.food_protein_input);
        EditText foodCarbsInput = view.findViewById(R.id.food_carbs_input);
        EditText foodFatInput = view.findViewById(R.id.food_fat_input);


        saveLogButton.setOnClickListener(v -> {
            FoodLogRepository foodLogRepository = new FoodLogRepository(requireActivity().getApplication());
            FoodLog foodLog = new FoodLog(foodNameInput.getText().toString(), Integer.parseInt(foodCaloriesInput.getText().toString()), Integer.parseInt(foodFatInput.getText().toString()), Integer.parseInt(foodCarbsInput.getText().toString()), Integer.parseInt(foodProteinInput.getText().toString()));

            foodLogRepository.insert(foodLog);

            Toast.makeText(requireContext(), "Food log saved", Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        });

        if (currentDailyLog != null) {
            EditText dateInput = view.findViewById(R.id.date_input);
            LocalDate currentDate = currentDailyLog.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            dateInput.setText(currentDate.getDayOfWeek() + ", " + currentDate.getMonth() + " " + currentDate.getDayOfMonth() + ", " + currentDate.getYear());
        }

        return view;
    }
}