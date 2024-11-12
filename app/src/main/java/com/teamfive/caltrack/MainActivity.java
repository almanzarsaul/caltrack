package com.teamfive.caltrack;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamfive.caltrack.database.repositories.GoalsRepository;
import com.teamfive.caltrack.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private GoalsRepository goalsRepository;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Set up NavHostFragment and NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_settings).build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);

            // Observe active goal in MainActivity
            goalsRepository = new GoalsRepository(getApplication());
            goalsRepository.getActiveGoal().observe(this, goals -> {
                if (goals != null) {
                    Log.d("MainActivity", "Active goal found: " + goals.toString());
                    // Navigate to HomeFragment
                    navController.navigate(R.id.navigation_home);
                    getSupportActionBar().show();

                } else {
                    Log.d("MainActivity", "No active goal found. Redirecting to goal setup.");
                    // Navigate to EditGoalFragment with isSetupMode = true
                    Bundle setupArgs = new Bundle();
                    setupArgs.putBoolean("isSetupMode", true);
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_home, true)
                            .build();
                    navController.navigate(R.id.navigation_edit_goal, setupArgs, navOptions);
                    getSupportActionBar().show();
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}