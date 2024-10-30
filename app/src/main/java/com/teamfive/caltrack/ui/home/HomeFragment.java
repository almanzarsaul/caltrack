package com.teamfive.caltrack.ui.home;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teamfive.caltrack.Food;
import com.teamfive.caltrack.FoodRepository;
import com.teamfive.caltrack.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private String selectedDate;
    private HashMap<String, String> notesMap;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hom3, container, false);
        LinearLayout layout = (LinearLayout) rootView;

        CalendarView calendarView = rootView.findViewById(R.id.calendarView);
        Button editNoteButton = rootView.findViewById(R.id.editNoteButton);
        notesMap = new HashMap<>();

        selectedDate = String.valueOf(calendarView.getDate());
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth);

        // Button click to edit notes
        editNoteButton.setOnClickListener(v -> showNoteDialog());

        // Create the buttons using the food names and ids from FoodRepository
        List<Food> foodList = FoodRepository.getInstance(requireContext()).getFoods();
        for (Food food : foodList) {
            Button button = new Button(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 10);   // 10px bottom margin
            button.setLayoutParams(layoutParams);

            // Display food's name on button
            button.setText(food.getName());
            button.setTag(food.getId());
            // Add button to the LinearLayout
            layout.addView(button);

        /*View.OnClickListener onClickListener = itemView -> {
            int selectedFoodId = (int) itemView.getTag();
            Bundle args = new Bundle();
            args.putInt( ,selectedFoodId);
        };*/
            //set up the recyclerView
            RecyclerView recyclerView = rootView.findViewById(R.id.food_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            FoodAdapter foodAdapter = new FoodAdapter();
            recyclerView.setAdapter(foodAdapter);

            foodAdapter.updateFoods(foodList);

            return rootView;
        }
        return rootView;
    }

    private void showNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Edit Note for " + selectedDate);


        final EditText input = new EditText(requireContext());
        input.setText(notesMap.get(selectedDate)); // Load existing note if any
        builder.setView(input);


        builder.setPositiveButton("Save", (dialog, which) -> {
            String note = input.getText().toString();
            notesMap.put(selectedDate, note); // Save the note
            Toast.makeText(requireActivity(), "Note saved!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}