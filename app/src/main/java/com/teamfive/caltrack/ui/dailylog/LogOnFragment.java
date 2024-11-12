//package com.teamfive.caltrack.ui.dailylog;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.camera.core.ImageCapture;
//import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.camera.view.PreviewView;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.navigation.Navigation;
//
//import com.teamfive.caltrack.Food;
//import com.teamfive.caltrack.R;
//
//public class LogOnFragment extends Fragment {
//
//    private EditText upcCodeInput;
//    private TextView scanItem;
//    private Button scanUpcButton;
//    private Button scanImageButton;
//    private Button addToLogButton;
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_log_on, container, false);
//
//        upcCodeInput = view.findViewById(R.id.);
//        scanItem = view.findViewById(R.id.scanned_item);
//        scanUpcButton = view.findViewById(R.id.scan_upc_button);
//        scanImageButton = view.findViewById(R.id.scan_image_button);
//        addToLogButton = view.findViewById(R.id.add_to_log_button);
//
//
//        scanUpcButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String upcCode = upcCodeInput.getText().toString();
//                fetchFoodDataByUPC(upcCode);
//            }
//        });
//
//        scanImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openCameraFragment();
//            }
//        });
//
//        addToLogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String upcCode = new String();
//                addItemToLog(upcCode);
//            }
//        });
//
//        return view;
//    }
//
//    private Food fetchFoodDataByUPC(String upcCode) {
//        // Simulate fetching data for the scanned UPC
//        if (!upcCode.isEmpty()) {
//            String foodName = "Example Food"; // Replace with actual data retrieval
//            int calories = 200; // Replace with actual data retrieval
//            scanItem.setText("Scanned Item: " + foodName + ", Calories: " + calories);
//            addToLogButton.setVisibility(View.VISIBLE);
//        } else {
//            scanItem.setText("Invalid UPC");
//        }
//        return null;
//    }
//
//    private void openCameraFragment() {
//        // Implement camera functionality for image scanning
//        View view = requireView();
//        Navigation.findNavController(view).navigate(R.id.navigation_camera);
//    }
//
//    private void addItemToLog(String upcCode) {
//        // Logic to save the scanned food item to the daily log
//        Food foodItem = fetchFoodDataByUPC(upcCode);
//
//        if (foodItem != null) {
//            String foodName = foodItem.getName();
//            String calories = foodItem.getCalories();
//
//            // Display the item and calories to the user
//            String result = foodName + ": " + calories + " calories";
//            scanItem.append(result + "\n");
//
//            // Show the add to log button (if applicable)
//            addToLogButton.setVisibility(View.VISIBLE);
//        } else {
//            // Handle case where the UPC code is not found
//            Toast.makeText(requireContext(), "Food item not found for UPC: " + upcCode, Toast.LENGTH_SHORT).show();
//        }
//
//    }
//}