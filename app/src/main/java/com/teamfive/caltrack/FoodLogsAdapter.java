package com.teamfive.caltrack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.teamfive.caltrack.R;
import com.teamfive.caltrack.database.entities.FoodLog;

public class FoodLogsAdapter extends ListAdapter<FoodLog, FoodLogsAdapter.FoodLogViewHolder> {

    public FoodLogsAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<FoodLog> DIFF_CALLBACK = new DiffUtil.ItemCallback<FoodLog>() {
        @Override
        public boolean areItemsTheSame(@NonNull FoodLog oldItem, @NonNull FoodLog newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull FoodLog oldItem, @NonNull FoodLog newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public FoodLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_log, parent, false);
        return new FoodLogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodLogViewHolder holder, int position) {
        FoodLog currentFoodLog = getItem(position);
        holder.bind(currentFoodLog);
    }

    class FoodLogViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView caloriesTextView;
        private final TextView carbsTextView;
        private final TextView fatTextView;
        private final TextView proteinTextView;

        public FoodLogViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.food_name_text_view);
            caloriesTextView = itemView.findViewById(R.id.calories_text_view);
            carbsTextView = itemView.findViewById(R.id.carbs_text_view);
            fatTextView = itemView.findViewById(R.id.fat_text_view);
            proteinTextView = itemView.findViewById(R.id.protein_text_view);
        }

        public void bind(FoodLog foodLog) {
            nameTextView.setText(foodLog.getName());
            caloriesTextView.setText("Calories: " + foodLog.getCalories());
            carbsTextView.setText("Carbs: " + foodLog.getCarbs() + "g");
            fatTextView.setText("Fat: " + foodLog.getFat() + "g");
            proteinTextView.setText("Protein: " + foodLog.getProtein() + "g");
        }
    }
}