package com.teamfive.caltrack.ui.goals;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamfive.caltrack.R;
import com.teamfive.caltrack.database.entities.Goals;
import com.teamfive.caltrack.viewmodel.GoalsViewModel;

public class EditGoalFragment extends Fragment {
    private boolean isSetupMode;

    // Initialize views
    private EditText editCaloriesGoal;
    private EditText editCarbohydratesGoal;
    private EditText editProteinGoal;
    private EditText editFatGoal;
    private Button saveGoalButton;
    private TextView editGoalNote;

    private GoalsViewModel goalsViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSetupMode = getArguments() != null && getArguments().getBoolean("isSetupMode", false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_goal, container, false);
        // Retrieve the setup mode argument
        editGoalNote = view.findViewById(R.id.edit_goal_note);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (isSetupMode) {
            activity.getSupportActionBar().setTitle("Set Up Goals");
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false); // Hide back button
            editGoalNote.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        // Setup goalsViewModel
        goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);

        //Initialize views
        editCaloriesGoal = view.findViewById(R.id.edit_goal_calories);
        editCarbohydratesGoal = view.findViewById(R.id.edit_goal_carbs);
        editProteinGoal = view.findViewById(R.id.edit_goal_protein);
        editFatGoal = view.findViewById(R.id.edit_goal_fat);
        saveGoalButton = view.findViewById(R.id.button_save_goal);
        editGoalNote = view.findViewById(R.id.edit_goal_note);

        saveGoalButton.setOnClickListener(v -> saveGoal());

        // If not in setup mode, populate with active goal.
        if (!isSetupMode) {
            goalsViewModel.getActiveGoal().observe(getViewLifecycleOwner(), new Observer<Goals>() {
                @Override
                public void onChanged(Goals goal) {
                    // Update UI with goal data when available
                    if (goal != null) {
                        editCaloriesGoal.setText(String.valueOf(goal.getGoalCalories()));
                        editCarbohydratesGoal.setText(String.valueOf(goal.getGoalCarbs()));
                        editFatGoal.setText(String.valueOf(goal.getGoalFat()));
                        editProteinGoal.setText(String.valueOf(goal.getGoalProtein()));
                    } else {
                        Log.e("EditGoalFragment", "Active goal is null"); // This should never happen.
                    }
                }
            });
        }

        // Hide bottom navigation in setup mode
        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        if (isSetupMode && navView != null) {
            navView.setVisibility(View.GONE);
        }

        // Customize app bar if in setup mode
//        if (isSetupMode && getActivity() instanceof AppCompatActivity) {
////            AppCompatActivity activity = (AppCompatActivity) getActivity();
////            activity.getSupportActionBar().setTitle("Set Up Goals");  // Set custom title
////            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);  // Hide back button
////            editGoalNote.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Restore the bottom navigation and app bar title when leaving
        if (isSetupMode && getActivity() instanceof AppCompatActivity) {
            BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
            if (navView != null) {
                navView.setVisibility(View.VISIBLE);
            }
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.getSupportActionBar().setTitle(R.string.app_name);  // Restore default title
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Restore back button
            editGoalNote.setVisibility(View.VISIBLE);
        }
    }

    private void saveGoal() {
        if (goalsViewModel == null) {
            Toast.makeText(getContext(), "GoalsViewModel is null", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int calories = Integer.parseInt(editCaloriesGoal.getText().toString());
            int carbs = Integer.parseInt(editCarbohydratesGoal.getText().toString());
            int fat = Integer.parseInt(editFatGoal.getText().toString());
            int protein = Integer.parseInt(editProteinGoal.getText().toString());
            goalsViewModel.saveGoal(calories, carbs, fat, protein);
            Toast.makeText(getContext(), "Goal saved successfully", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(requireView());

            if (isSetupMode) {
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_edit_goal, true)
                        .build();
                navController.navigate(R.id.navigation_home, null, navOptions);
            } else {
                navController.navigateUp();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();

        }
    }
}