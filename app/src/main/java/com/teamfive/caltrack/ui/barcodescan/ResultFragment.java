package com.teamfive.caltrack.ui.barcodescan;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.appcompat.app.AppCompatActivity;

import com.teamfive.caltrack.network.OpenFoodFactsApi;
import com.teamfive.caltrack.network.models.Nutriments;
import com.teamfive.caltrack.network.models.ProductResponse;

import com.teamfive.caltrack.R;

public class ResultFragment extends Fragment {

    //change below back to upcCode when done testing
    private String upcCode;

    private TextView productName;
    private TextView servingSizeInput;
    private TextView productCalories, productFat, productProtein, productCarbs;
    private Button fetchButton, calculateButton;
    private TextView barcodeInput;

    private ProductResponse productResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            upcCode = getArguments().getString("upc_code");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        barcodeInput = view.findViewById(R.id.barcode_input);
        fetchButton = view.findViewById(R.id.fetch_button);
        productName = view.findViewById(R.id.product_name);
        servingSizeInput = view.findViewById(R.id.serving_size);
        calculateButton = view.findViewById(R.id.calculate_button);
        productCalories = view.findViewById(R.id.product_calories);
        productFat = view.findViewById(R.id.product_fat);
        productProtein = view.findViewById(R.id.product_protein);
        productCarbs = view.findViewById(R.id.product_carbs);

        fetchButton.setOnClickListener(v -> fetchProductInfo());
        calculateButton.setOnClickListener(v -> calculateNutrients());

        if (upcCode != null) {
            barcodeInput.setText(upcCode);
            if (getActivity() instanceof AppCompatActivity) { // Set action bar to UPC
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("upcCode");
            }
        } else {
            barcodeInput.setText("No UPC code found");
        }
        return view;
    }

    private void fetchProductInfo() {
        String barcode = barcodeInput.getText().toString();
        if (!barcode.isEmpty()) {
            fetchFromApi(barcode);
        } else {
            Toast.makeText(getContext(), "Please enter a barcode", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchFromApi(String barcode) {
        String url = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenFoodFactsApi api = retrofit.create(OpenFoodFactsApi.class);
        Call<ProductResponse> call = api.getProduct(barcode);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayProductInfo(response.body());
                } else {
                    Toast.makeText(getContext(), "Product not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProductInfo(ProductResponse response) {
        if (response != null && response.getProduct() != null) {
            productResponse = response; // Set productResponse here for later use in calculateNutrients
            productName.setText("Product Name: " + productResponse.getProduct().getName());
        }
    }


    private void calculateNutrients() {
        if (productResponse != null && productResponse.getProduct().getNutriments() != null) {
            try {
                float servingSize = Float.parseFloat(servingSizeInput.getText().toString());

                Nutriments nutriments = productResponse.getProduct().getNutriments();

                // Nutrients per 100g
                float caloriesPer100g = nutriments.getEnergy();
                float proteinsPer100g = nutriments.getProtein();
                float carbsPer100g = nutriments.getCarbs();
                float fatsPer100g = nutriments.getFat();

                // Calculate nutrients based on user input serving size
                float caloriesConsumed = (caloriesPer100g / 100) * servingSize;
                float proteinsConsumed = (proteinsPer100g / 100) * servingSize;
                float carbsConsumed = (carbsPer100g / 100) * servingSize;
                float fatsConsumed = (fatsPer100g / 100) * servingSize;

                // Display the calculated values
                productCalories.setText("Calories Consumed: " + caloriesConsumed + " kJ");
                productProtein.setText("Proteins Consumed: " + proteinsConsumed + " g");
                productCarbs.setText("Carbohydrates Consumed: " + carbsConsumed + " g");
                productFat.setText("Fats Consumed: " + fatsConsumed + " g");

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Please enter a valid serving size", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Nutrient data not available", Toast.LENGTH_SHORT).show();
        }
    }
}