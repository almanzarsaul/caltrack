package com.teamfive.caltrack.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.teamfive.caltrack.R;

public class ProgressIndicatorView extends LinearLayout {
    private TextView title;
    private ProgressBar progressBar;
    private TextView goalValue;
    private TextView unitTextView;
    private TextView currentValue;
    private int progressColor;

    public ProgressIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.progress_indicator, this, true);

        title = findViewById(R.id.indicator_title);
        progressBar = findViewById(R.id.indicator_progress_bar);
        goalValue = findViewById(R.id.indicator_goal_value);
        unitTextView = findViewById(R.id.indicator_unit);
        currentValue = findViewById(R.id.indicator_current_value);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressIndicator, 0, 0);

        try {
            String indicatorTitle = attributes.getString(R.styleable.ProgressIndicator_indicatorTitle);
            int indicatorProgress = attributes.getInteger(R.styleable.ProgressIndicator_indicatorProgress, 0);
            int indicatorGoal = attributes.getInteger(R.styleable.ProgressIndicator_indicatorGoal, 100);
            String unit = attributes.getString(R.styleable.ProgressIndicator_indicatorUnit);
            progressColor = attributes.getColor(R.styleable.ProgressIndicator_indicatorColor,
                    context.getColor(android.R.color.holo_blue_dark)); // default color

            title.setText(indicatorTitle);
            progressBar.setProgress(indicatorProgress);
            goalValue.setText(String.valueOf(indicatorGoal));
            unitTextView.setText(unit);
            setProgressColor(progressColor);
        } finally {
            attributes.recycle();
        }
    }

    public void setProgressColor(int color) {
        progressColor = color;
        if (progressBar.getProgressDrawable() instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) progressBar.getProgressDrawable();
            GradientDrawable progressDrawable = (GradientDrawable) ((RotateDrawable) layerDrawable.getDrawable(1)).getDrawable();
            if (progressDrawable != null) {
                progressDrawable.setColor(color);
            }
        }
    }

    public void updateGoalUI(int totalCalories, int goalCalories) {
        int progress = (int) (((double) totalCalories / goalCalories) * 100);
        progressBar.setProgress(progress);
        currentValue.setText(String.valueOf(totalCalories));
        goalValue.setText(String.valueOf(goalCalories));
    }
}