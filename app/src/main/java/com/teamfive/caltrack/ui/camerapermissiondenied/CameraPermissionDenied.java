package com.teamfive.caltrack.ui.camerapermissiondenied;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamfive.caltrack.R;

public class CameraPermissionDenied extends Fragment {

    private Button returnToLogButton;
    private Button systemSettingsButton;
    private BottomNavigationView navView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, navigate up
            NavHostFragment.findNavController(this).navigateUp();
        } else {
            Log.d("BarcodeScanner", "Camera permission denied.");
        }

        navView = getActivity().findViewById(R.id.nav_view);

        if (navView != null) {
            navView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        navView = getActivity().findViewById(R.id.nav_view);

        if (navView != null) {
            navView.setVisibility(View.GONE);
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, navigate up
            NavHostFragment.findNavController(this).navigateUp();
        } else {
            Log.d("BarcodeScanner", "Camera permission denied still denied.");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_permission_denied, container, false);

        returnToLogButton = view.findViewById(R.id.return_to_log_button);
        systemSettingsButton = view.findViewById(R.id.system_settings_button);

        systemSettingsButton.setOnClickListener(v -> openAppSettings());

        returnToLogButton.setOnClickListener(v -> {
            Log.d("CameraPermissionDenied", "Home button clicked");
            if (getActivity() instanceof AppCompatActivity) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            }

            NavController navController = NavHostFragment.findNavController(this);
            navController.popBackStack(R.id.navigation_new_log, false); // Pop to a specific destination
        });

        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }

        return view;
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        }

    }
}