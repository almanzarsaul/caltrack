package com.teamfive.caltrack.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.teamfive.caltrack.HomeScreenFragment;
import com.teamfive.caltrack.R;
import com.teamfive.caltrack.database.entities.Goals;
import com.teamfive.caltrack.database.repositories.GoalsRepository;

public class GoalSelectionFragment extends Fragment {

    private GoalsRepository goalsRepository;
    private EditText caloriesInput, carbsInput, fatInput, proteinInput;
    private Button saveButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal_selection, container, false);

        goalsRepository = new GoalsRepository(requireActivity().getApplication());

        caloriesInput = view.findViewById(R.id.calories_input);
        carbsInput = view.findViewById(R.id.carbs_input);
        fatInput = view.findViewById(R.id.fat_input);
        proteinInput = view.findViewById(R.id.protein_input);
        saveButton = view.findViewById(R.id.save_button);

        saveButton.setOnClickListener(v -> saveGoal());

        return view;
    }

    private void saveGoal() {
        if (validateInputs()) {
            int calories = Integer.parseInt(caloriesInput.getText().toString());
            int carbs = Integer.parseInt(carbsInput.getText().toString());
            int fat = Integer.parseInt(fatInput.getText().toString());
            int protein = Integer.parseInt(proteinInput.getText().toString());

            Goals newGoal = new Goals(calories, carbs, fat, protein);
            goalsRepository.replaceActiveGoal(newGoal);

            Toast.makeText(getContext(), "Goal saved successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to the Home Screen
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeScreenFragment())
                    .commit();
        }
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(caloriesInput.getText()) || TextUtils.isEmpty(carbsInput.getText())
                || TextUtils.isEmpty(fatInput.getText()) || TextUtils.isEmpty(proteinInput.getText())) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}