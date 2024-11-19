package com.teamfive.caltrack.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamfive.caltrack.Food;
import com.teamfive.caltrack.R;
import com.teamfive.caltrack.database.entities.FoodLog;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter  extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<FoodLog> mFoodLogs = new ArrayList<>();
    private View.OnClickListener mOnClickListener;

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        private final TextView foodNameTextView;
        private final TextView foodProteinTextView;
        private final TextView foodCarbsTextView;
        private final TextView foodFatTextView;
        private final TextView foodCaloriesTextView;
        private Context context;

        public FoodViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            foodNameTextView = itemView.findViewById(R.id.foodName);
            foodCaloriesTextView = itemView.findViewById(R.id.calories_text);
            foodProteinTextView = itemView.findViewById(R.id.protein_text);
            foodCarbsTextView = itemView.findViewById(R.id.carbs_text);
            foodFatTextView = itemView.findViewById(R.id.fat_text);
        }

        public void bind(FoodLog foodLog) {
            foodNameTextView.setText(foodLog.getName());
            foodCaloriesTextView.setText(context.getString(R.string.calories_value_format, foodLog.getCalories()));
            foodProteinTextView.setText(context.getString(R.string.macro_value_format, foodLog.getProtein()));
            foodCarbsTextView.setText(context.getString(R.string.macro_value_format, foodLog.getCarbs()));
            foodFatTextView.setText(context.getString(R.string.macro_value_format, foodLog.getFat()));

        }
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scan_items_list, parent, false);
        return new FoodViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        FoodLog food = mFoodLogs.get(position);
        holder.bind(food);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mFoodLogs.size();
    }

    public void updateFoods(List<FoodLog> newFoods) {
        mFoodLogs.clear();
        mFoodLogs.addAll(newFoods);
        notifyDataSetChanged();
    }
}
