package com.teamfive.caltrack.ui.home;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamfive.caltrack.Food;
import com.teamfive.caltrack.R;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter  extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<Food> mFoods = new ArrayList<>();
    private View.OnClickListener mOnClickListener;

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        private final TextView foodNameTextView;
        private final TextView foodCaloriesTextView;
        private final ImageView foodImageView;

        public FoodViewHolder(View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.foodName);
            foodCaloriesTextView = itemView.findViewById(R.id.foodCalories);
            foodImageView = itemView.findViewById(R.id.scannedfood);
        }

        public void bind(Food food) {
            foodNameTextView.setText(food.getName());
            foodCaloriesTextView.setText(food.getCalories());
            foodImageView.setImageResource(food.getImageResource());
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
        Food food = mFoods.get(position);
        holder.bind(food);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    public void updateFoods(List<Food> newFoods) {
        mFoods.clear();
        mFoods.addAll(newFoods);
        notifyDataSetChanged();
    }
}
