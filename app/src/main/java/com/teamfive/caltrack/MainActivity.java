package com.teamfive.caltrack;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

//import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;

//import com.teamfive.caltrack.databinding.ActivityMainBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.cardview.widget.CardView;

import com.teamfive.caltrack.database.AppDatabase;
import com.teamfive.caltrack.database.entities.Goals;
import com.teamfive.caltrack.databinding.ActivityMainBinding;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
      //private ActivityMainBinding binding;

      private String selectedDate;
      private HashMap<String, String> notesMap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarView calendarView = findViewById(R.id.calendarView);
        Button editNoteButton = findViewById(R.id.editNoteButton);
        notesMap = new HashMap<>();

        selectedDate = String.valueOf(calendarView.getDate());
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth);
        
        // Button click to edit notes
        editNoteButton.setOnClickListener(v -> showNoteDialog());

        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        //BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
        //        R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        //        .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(binding.navView, navController);


    }

    private void showNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Edit Note for " + selectedDate);


        final EditText input = new EditText(MainActivity.this);
        input.setText(notesMap.get(selectedDate)); // Load existing note if any
        builder.setView(input);


        builder.setPositiveButton("Save", (dialog, which) -> {
            String note = input.getText().toString();
            notesMap.put(selectedDate, note); // Save the note
            Toast.makeText(MainActivity.this, "Note saved!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();


        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_log, R.id.navigation_notifications)
                    .build();

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
            NavigationUI.setupWithNavController(navView, navController);
        }
    }

}



