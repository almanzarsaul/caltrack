package com.teamfive.caltrack;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teamfive.caltrack.network.OpenFoodFactsApi;
import com.teamfive.caltrack.network.models.Nutriments;
import com.teamfive.caltrack.network.models.ProductResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogOnFragment extends Fragment {

    private EditText barcodeInput;
    private TextView productName;
    private TextView productCalories;
    private TextView productFat;
    private TextView productSodium;
    private Button fetchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        barcodeInput = view.findViewById(R.id.barcode_input);
        productName = view.findViewById(R.id.product_name);
        productCalories = view.findViewById(R.id.product_calories);
        productFat = view.findViewById(R.id.product_fat);
        productSodium = view.findViewById(R.id.product_sodium);
        fetchButton = view.findViewById(R.id.fetch_button);
        fetchButton.setOnClickListener(v -> fetchProductInfo());

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

    private void displayProductInfo(ProductResponse productResponse) {
        if (productResponse != null && productResponse.getProduct() != null) {
            productName.setText("Product Name: " + productResponse.getProduct().getName());

            Nutriments nutriments = productResponse.getProduct().getNutriments();
            if (nutriments != null) {
                productCalories.setText("Calories: " + nutriments.getEnergy() + " kJ");
                productFat.setText("Fat: " + nutriments.getFat() + " g");
                productSodium.setText("Sodium: " + nutriments.getSodium() + " g");
            } else {
                productCalories.setText("Nutriments not available");
            }
        }
    }

    public LogOnFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogOnFragment.
     */

}