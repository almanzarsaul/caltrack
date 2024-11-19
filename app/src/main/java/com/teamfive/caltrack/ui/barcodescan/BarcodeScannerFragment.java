package com.teamfive.caltrack.ui.barcodescan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.teamfive.caltrack.R;

public class BarcodeScannerFragment extends Fragment {
    private static final String TAG = "BarcodeScannerFragment";
    private final long BUTTON_DELAY = 500;

    private PreviewView previewView;
    private ProcessCameraProvider cameraProvider;
    private BarcodeScanner barcodeScanner;
    private Button overlayButton;
    private BottomNavigationView navView;
    private ImageAnalysis imageAnalysis;
    private Preview preview;

    private boolean hasNavigated = false;
    private String currentUPC = null;
    private String lastDetectedUPC = null;
    private long lastUpdateTime = 0;


    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startCamera();
                } else {
                    Log.d(TAG, "Camera permission denied.");
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_currentFragment_to_targetFragment);
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barcode_scanner, container, false);
        previewView = view.findViewById(R.id.previewView);
        if (getActivity() != null) {
            navView = getActivity().findViewById(R.id.nav_view);
            if (navView != null) {
                navView.setVisibility(View.GONE);
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barcodeScanner = BarcodeScanning.getClient();
        overlayButton = view.findViewById(R.id.overlayButton);
        overlayButton.setOnClickListener(v -> {
            if (navView != null) {
                navView.setVisibility(View.VISIBLE);
            }
            if (currentUPC != null) {
                navigateToTargetFragment(currentUPC);
                hasNavigated = true;
            }
        });

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases();
            } catch (Exception e) {
                Log.e(TAG, "Error starting camera", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void bindCameraUseCases() {
        if (cameraProvider == null) return;

        preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        setupImageAnalyzer();

        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        try {
            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle(
                    getViewLifecycleOwner(),
                    cameraSelector,
                    preview,
                    imageAnalysis
            );
        } catch (Exception e) {
            Log.e(TAG, "Error binding camera use cases", e);
        }
    }

    private void setupImageAnalyzer() {
        if (imageAnalysis == null) return;

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext()),
                new ImageAnalysis.Analyzer() {
                    @Override
                    public void analyze(@NonNull ImageProxy imageProxy) {
                        if (hasNavigated) {
                            imageProxy.close();
                            return;
                        }

                        @SuppressLint("UnsafeOptInUsageError")
                        Image mediaImage = imageProxy.getImage();
                        if (mediaImage != null) {
                            InputImage image = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.getImageInfo().getRotationDegrees()
                            );
                            processImage(image, imageProxy);
                        } else {
                            imageProxy.close();
                        }
                    }
                });
    }

    private void processImage(InputImage image, ImageProxy imageProxy) {
        if (barcodeScanner == null) {
            imageProxy.close();
            return;
        }

        barcodeScanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    if (!isAdded()) {
                        return;
                    }
                    long currentTime = System.currentTimeMillis();
                    if (barcodes.isEmpty()) {
                        if (currentTime - lastUpdateTime > BUTTON_DELAY) {

                            if (overlayButton != null) {
                                overlayButton.setVisibility(View.GONE);
                                currentUPC = null;
                                lastDetectedUPC = null;

                            }
                            lastUpdateTime = currentTime;
                        }

                    } else {
                        for (Barcode barcode : barcodes) {
                            if (barcode.getValueType() == Barcode.TYPE_PRODUCT) {
                                String upc = barcode.getDisplayValue();

                                Log.d(TAG, "UPC Code: " + upc);
                                currentUPC = upc;
                                lastUpdateTime = currentTime;
                                lastDetectedUPC = upc;
                                overlayButton.setText(currentUPC);
                                overlayButton.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if (isAdded() && overlayButton != null) {
                        currentUPC = null;
                        overlayButton.setVisibility(View.GONE);
                    }
                })
                .addOnCompleteListener(task -> imageProxy.close());
    }

    private void navigateToTargetFragment(String upc) {
        NavController navController = NavHostFragment.findNavController(this);
        Bundle args = new Bundle();
        args.putString("upc_code", upc);
        navController.navigate(R.id.action_barcodeScannerFragment_to_resultFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cleanupCamera();

        if (navView != null) {
            navView.setVisibility(View.VISIBLE);
        }

        previewView = null;
        overlayButton = null;
        navView = null;
    }

    private void cleanupCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
            cameraProvider = null;
        }

        if (imageAnalysis != null) {
            imageAnalysis.clearAnalyzer();
            imageAnalysis = null;
        }

        if (barcodeScanner != null) {
            barcodeScanner.close();
            barcodeScanner = null;
        }

        preview = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        hasNavigated = false;
        currentUPC = null; // Reset for fresh scanning session
    }
}