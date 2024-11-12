package com.teamfive.caltrack.ui.dailylog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.teamfive.caltrack.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class CameraFragment extends Fragment implements OnBackPressed {


    private static final int CAMERA_PERMISSION_CODE = 100;
    private Button addToLogButton;
    private ImageCapture imageCapture;
    private PreviewView previewView;
    private TextView scanItem;
    private ProcessCameraProvider cameraProvider;
    private ImageButton captureButton;
    private List<String> imageLog = new ArrayList<>();

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        addToLogButton = rootView.findViewById(R.id.add_to_log_button);
        scanItem = rootView.findViewById(R.id.scanned_item);
        previewView = rootView.findViewById(R.id.previewView);
        captureButton = rootView.findViewById(R.id.captureBtn);
        File imageFile = new File(requireContext().getExternalFilesDir(null), "capturedImage.jpg");

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        return rootView;

    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        Preview preview = new Preview.Builder().build();
        imageCapture = new ImageCapture.Builder().build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    private void captureImage(){
        if(imageCapture==null){
            return;
        }

        //file to save the image
        File imageFile = new File(requireContext().getExternalFilesDir(null), "capturedImage.jpg");
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(imageFile).build();

        //take the picture and save it to the file
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        // Image saved successfully
                        Toast.makeText(requireContext(), "Image captured and saved", Toast.LENGTH_SHORT).show();
                        imageLog.add(imageFile.getAbsolutePath());
                        addItemToLog(imageFile);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        // Handle the error
                        exception.printStackTrace();
                        Toast.makeText(requireContext(), "Failed to capture image", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void openCameraForImageScan() {
        // Implement camera functionality for image scanning
        previewView = requireView().findViewById(R.id.previewView);
        startCamera();
    }

    @Override
    public void onBackPressed() {
        // Handle back press manually if needed, for example:
        // Navigate back using NavController if required
        NavController navController = Navigation.findNavController(requireView());
        navController.popBackStack();
    }

    private void addItemToLog(File imageFile) {
        InputImage image;
        try {
            image = InputImage.fromFilePath(requireContext(), Uri.fromFile(imageFile));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Bundle to store data
        Bundle bundle = new Bundle();

        // Use ML Kit's Image Labeling to recognize objects in the image
        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

        labeler.process(image)
                .addOnSuccessListener(labels -> {
                    // To store multiple labels and confidence values
                    ArrayList<String> labelNames = new ArrayList<>();
                    ArrayList<Float> confidences = new ArrayList<>();

                    for (ImageLabel label : labels) {
                        String foodName = label.getText();
                        float confidence = label.getConfidence();

                        // Store label data in lists
                        labelNames.add(foodName);
                        confidences.add(confidence);

                        // Placeholder calories based on item names
                        int calories = getCalories(foodName);

                        // Display the item and calories to the user
                        String result = foodName + ": " + calories + " calories (Confidence: " + (int) (confidence * 100) + "%)";
                        scanItem.append(result + "\n");
                    }

                    // Add label data to Bundle
                    bundle.putStringArrayList("labelNames", labelNames);
                    bundle.putSerializable("confidences", confidences);
                    bundle.putString("imagePath", imageFile.getAbsolutePath());

                    addToLogButton.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Image processing failed", Toast.LENGTH_SHORT).show();
                });

        // Initialize Barcode Scanning
        BarcodeScanner barcodeScanner = BarcodeScanning.getClient();

        barcodeScanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    for (Barcode barcode : barcodes) {
                        String upcCode = barcode.getRawValue();
                        if (upcCode != null) {
                            bundle.putString("upcCode", upcCode);
                            scanItem.append("UPC Code: " + upcCode + "\n");
                        }
                    }
                    // Set the bundle arguments
                    setArguments(bundle);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Barcode processing failed", Toast.LENGTH_SHORT).show();
                });
    }

    //placeholder and can be refined with a more comprehensive food database.
    private int getCalories(String foodName) {
        switch (foodName.toLowerCase()) {
            case "banana":
                return 100;
            case "apple":
                return 57;
            case "cabbage":
                return 72;
            case "orange":
                return 47;
            case "grape":
                return 67;
            case "strawberry":
                return 33;
            default:
                return 0; // Unknown items can have 0 calories or a generic placeholder value
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraForImageScan();
            } else {
                Toast.makeText(requireContext(), "Camera permission is needed to scan images", Toast.LENGTH_SHORT).show();
            }
        }
    }


}