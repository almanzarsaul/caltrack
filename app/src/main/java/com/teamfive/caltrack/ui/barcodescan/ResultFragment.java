package com.teamfive.caltrack.ui.barcodescan;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamfive.caltrack.R;

public class ResultFragment extends Fragment {

    private String upcCode;

    private TextView textTextEdit;

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
        textTextEdit = view.findViewById(R.id.text_view);
        if (upcCode != null) {
            textTextEdit.setText(upcCode);
            if (getActivity() instanceof AppCompatActivity) { // Set action bar to UPC
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(upcCode);
            }
        } else {
            textTextEdit.setText("No UPC code found");
        }
        return view;
    }

}