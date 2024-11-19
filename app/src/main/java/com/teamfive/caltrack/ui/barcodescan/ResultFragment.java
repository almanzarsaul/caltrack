package com.teamfive.caltrack.ui.barcodescan;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.teamfive.caltrack.R;
import com.teamfive.caltrack.database.entities.FoodLog;
import com.teamfive.caltrack.database.repositories.FoodLogRepository;
import com.teamfive.caltrack.network.RetrofitClient;
import com.teamfive.caltrack.network.OpenFoodFactsApi;
import com.teamfive.caltrack.network.models.ProductResponse;
import com.teamfive.caltrack.network.models.Nutriments;
import com.teamfive.caltrack.network.models.NutritionValues;
import com.teamfive.caltrack.network.models.Product;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ResultFragment extends Fragment {
    private static final String ARG_UPC_CODE = "upc_code";

    private String upcCode;
    private FoodLogRepository repository;
    private View contentContainer;
    private View loadingIndicator;
    private TextView productNameText;
    private TextView servingSizeText;
    private TextView errorText;
    private RadioGroup servingSizeGroup;
    private RadioButton oneServingRadio;
    private RadioButton customServingRadio;
    private EditText customGramsEdit;
    private Button saveButton;
    private ProductResponse productData;

    private TextView caloriesText;
    private TextView carbsText;
    private TextView proteinText;
    private TextView fatText;

    public static ResultFragment newInstance(String upcCode) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_UPC_CODE, upcCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            upcCode = getArguments().getString(ARG_UPC_CODE);
        }
        repository = new FoodLogRepository(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        contentContainer = view.findViewById(R.id.content_container);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        productNameText = view.findViewById(R.id.product_name_text);
        errorText = view.findViewById(R.id.error_text);
        servingSizeGroup = view.findViewById(R.id.serving_size_group);
        oneServingRadio = view.findViewById(R.id.one_serving_radio);
        customServingRadio = view.findViewById(R.id.custom_serving_radio);
        customGramsEdit = view.findViewById(R.id.custom_grams_edit);
        saveButton = view.findViewById(R.id.save_button);

        caloriesText = view.findViewById(R.id.calories_text);
        carbsText = view.findViewById(R.id.carbs_text);
        proteinText = view.findViewById(R.id.protein_text);
        fatText = view.findViewById(R.id.fat_text);

        saveButton.setEnabled(false);

        saveButton.setOnClickListener(v -> saveFoodLog());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchProductData();
    }

    private void showLoading() {
        loadingIndicator.setVisibility(View.VISIBLE);
        contentContainer.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
    }

    private void showContent() {
        loadingIndicator.setVisibility(View.GONE);
        contentContainer.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
    }

    private void showError(String message) {
        loadingIndicator.setVisibility(View.GONE);
        contentContainer.setVisibility(View.GONE);
        errorText.setVisibility(View.VISIBLE);
        errorText.setText(message);
    }

    private void fetchProductData() {
        showLoading();
        Log.d("ResultFragment", "Fetching product data for UPC: " + upcCode);

        OpenFoodFactsApi api = RetrofitClient.getInstance().create(OpenFoodFactsApi.class);
        api.getProduct(upcCode).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                Log.d("ResultFragment", "Got API response. Success: " + response.isSuccessful());
                if (response.isSuccessful() && response.body() != null && response.body().getProduct() != null) {
                    productData = response.body();
                    Log.d("ResultFragment", "Product data: " + productData.getProduct());
                    Log.d("ResultFragment", "Product fat: " + productData.getProduct().getNutriments().getFat100g());
                    updateUI();
                    showContent();
                } else {
                    Log.e("ResultFragment", "Error response: " + response.code() + " " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            Log.e("ResultFragment", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    showError("Unable to load product information");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e("ResultFragment", "API call failed", t);
                Log.e("ResultFragment", "URL being called: " + call.request().url());
                if (t instanceof UnknownHostException) {
                    Log.e("ResultFragment", "DNS resolution failed");
                } else if (t instanceof SocketTimeoutException) {
                    Log.e("ResultFragment", "Connection timed out");
                }
                showError("Failed to fetch product data: " + t.getMessage());
            }
        });
    }

    private void updateUI() {
        if (productData != null && productData.getProduct() != null) {
            Product product = productData.getProduct();
            Nutriments nutriments = product.getNutriments();

            // Set product name
            productNameText.setText(product.getProductName());

            // Enable save button
            saveButton.setEnabled(true);

            // Set initial nutritional information based on serving size
            updateNutritionalInfo(nutriments, true);

            // Add listener for custom serving size
            customGramsEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0 && customServingRadio.isChecked()) {
                        try {
                            float grams = Float.parseFloat(s.toString());
                            updateNutritionalInfo(nutriments, grams);
                        } catch (NumberFormatException e) {
                            showError("Please enter a valid number");
                        }
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
            });

            // Add radio button listener to switch between serving sizes
            servingSizeGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.one_serving_radio) {
                    updateNutritionalInfo(nutriments, true);
                } else if (checkedId == R.id.custom_serving_radio) {
                    // Clear texts until user enters value
                    customGramsEdit.getText().clear();
                    customGramsEdit.requestFocus();
                    // Clear nutritional info
                    caloriesText.setText("-- kcal");
                    carbsText.setText("-- g");
                    proteinText.setText("-- g");
                    fatText.setText("-- g");
                }
            });
        }
    }

    private void updateNutritionalInfo(Nutriments nutriments, boolean perServing) {
        if (perServing) {
            // Use per serving values
            Double energy = nutriments.getEnergyKcalServing();
            Double carbs = nutriments.getCarbohydratesServing();
            Double protein = nutriments.getProteinsServing();
            Double fat = nutriments.getFatServing();

            caloriesText.setText(String.format("%.0f kcal", energy != null ? energy : 0));
            carbsText.setText(String.format("%.1f g", carbs != null ? carbs : 0));
            proteinText.setText(String.format("%.1f g", protein != null ? protein : 0));
            fatText.setText(String.format("%.1f g", fat != null ? fat : 0));
        }
    }

    private void updateNutritionalInfo(Nutriments nutriments, float grams) {
        // Calculate ratios based on 100g values
        float ratio = grams / 100f;

        Double energy100g = nutriments.getEnergyKcal100g();
        Double carbs100g = nutriments.getCarbohydrates100g();
        Double protein100g = nutriments.getProteins100g();
        Double fat100g = nutriments.getFat100g();

        Double energy = energy100g != null ? energy100g * ratio : 0;
        Double carbs = carbs100g != null ? carbs100g * ratio : 0;
        Double protein = protein100g != null ? protein100g * ratio : 0;
        Double fat = fat100g != null ? fat100g * ratio : 0;

        caloriesText.setText(String.format("%.0f kcal", energy));
        carbsText.setText(String.format("%.1f g", carbs));
        proteinText.setText(String.format("%.1f g", protein));
        fatText.setText(String.format("%.1f g", fat));
    }

    private void saveFoodLog() {
        if (productData == null || productData.getProduct() == null) return;

        Product product = productData.getProduct();
        Nutriments nutriments = product.getNutriments();

        if (nutriments == null) {
            showError("No nutritional information available");
            return;
        }

        float calories, carbs, fat, protein;

        if (oneServingRadio.isChecked()) {
            // Use API's per serving values
            calories = nutriments.getEnergyKcalServing() != null ? nutriments.getEnergyKcalServing().floatValue() : 0;
            carbs = nutriments.getCarbohydratesServing() != null ? nutriments.getCarbohydratesServing().floatValue() : 0;
            fat = nutriments.getFatServing() != null ? nutriments.getFatServing().floatValue() : 0;
            protein = nutriments.getProteinsServing() != null ? nutriments.getProteinsServing().floatValue() : 0;
        } else {
            try {
                float customGrams = Float.parseFloat(customGramsEdit.getText().toString());
                // Calculate based on 100g values
                float ratio = customGrams / 100f;

                calories = (nutriments.getEnergyKcal100g() != null ? nutriments.getEnergyKcal100g().floatValue() : 0) * ratio;
                carbs = (nutriments.getCarbohydrates100g() != null ? nutriments.getCarbohydrates100g().floatValue() : 0) * ratio;
                fat = (nutriments.getFat100g() != null ? nutriments.getFat100g().floatValue() : 0) * ratio;
                protein = (nutriments.getProteins100g() != null ? nutriments.getProteins100g().floatValue() : 0) * ratio;
            } catch (NumberFormatException e) {
                showError("Please enter a valid number of grams");
                return;
            }
        }

        FoodLog foodLog = new FoodLog(
                product.getProductName(),
                Math.round(calories),
                Math.round(carbs),
                Math.round(fat),
                Math.round(protein)
        );

        repository.insert(foodLog);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_resultFragment_to_homeFragment);
    }
}