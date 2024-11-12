//package com.teamfive.caltrack;
//
//import android.os.Bundle;
//import androidx.fragment.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.teamfive.caltrack.network.OpenFoodFactsApi;
//import com.teamfive.caltrack.network.models.NutriscoreData;
//import com.teamfive.caltrack.network.models.ProductResponse;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link LogOnFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class LogOnFragment extends Fragment {
//
//    private EditText barcodeInput;
//    private TextView productName;
//    private TextView productCalories;
//    private Button fetchButton;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_log_on, container, false);
//
//        barcodeInput = view.findViewById(R.id.barcode_input);
//        productName = view.findViewById(R.id.product_name);
//        productCalories = view.findViewById(R.id.product_calories);
//        fetchButton = view.findViewById(R.id.fetch_button);
//
//        fetchButton.setOnClickListener(v -> fetchProductInfo());
//
//        return view;
//    }
//
//    private void fetchProductInfo() {
//        String barcode = barcodeInput.getText().toString();
//        if (!barcode.isEmpty()) {
//            fetchFromApi(barcode);
//        } else {
//            Toast.makeText(getContext(), "Please enter a barcode", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void fetchFromApi(String barcode) {
//        String url = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://world.openfoodfacts.org/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        OpenFoodFactsApi api = retrofit.create(OpenFoodFactsApi.class);
//        Call<ProductResponse> call = api.getProduct(barcode);
//
//        call.enqueue(new Callback<ProductResponse>() {
//            @Override
//            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    displayProductInfo(response.body());
//                } else {
//                    Toast.makeText(getContext(), "Product not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ProductResponse> call, Throwable t) {
//                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void displayProductInfo(ProductResponse productResponse) {
//        if (productResponse != null && productResponse.getProduct() != null) {
//            productName.setText("Product Name: " + productResponse.getProduct().getName());
//
//            NutriscoreData nutriscoreData = productResponse.getProduct().getNutriscoreData();
//            if (nutriscoreData != null) {
//                productCalories.setText("Calories: " + nutriscoreData.getEnergy() + " kJ");
//            } else {
//                productCalories.setText("Calories: Not available");
//            }
//        }
//    }
//
//
//
//
//    public LogOnFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment LogOnFragment.
//     */
//    public static LogOnFragment newInstance(String param1, String param2) {
//        LogOnFragment fragment = new LogOnFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//}
//
