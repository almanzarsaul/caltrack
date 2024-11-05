package com.teamfive.caltrack.ui.home;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.kizitonwose.calendar.core.WeekDay;
import com.kizitonwose.calendar.view.WeekCalendarView;
import com.kizitonwose.calendar.view.WeekDayBinder;
import com.teamfive.caltrack.DayViewContainer;
import com.teamfive.caltrack.R;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private LocalDate currentWeek;
    private final LocalDate today = LocalDate.now();

    private LocalDate activeDate = today;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WeekCalendarView weekCalendarView = view.findViewById(R.id.weekCalendarView);
        TextView weekTitle = view.findViewById(R.id.weekTitle);

        DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("MMM d", Locale.getDefault());
        currentWeek = LocalDate.now();

        // Set initial week title
        updateWeekTitle(weekTitle, currentWeek);

        // Set up the WeekDayBinder with WeekDay instead of CalendarDay
        weekCalendarView.setDayBinder(new WeekDayBinder<DayViewContainer>() {
            @Override
            public DayViewContainer create(View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(DayViewContainer container, WeekDay weekDay) {
                LocalDate date = weekDay.getDate();
                container.textView.setText(String.valueOf(date.getDayOfMonth()));
                if (date.isAfter(today)) {
                    container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray));
                    container.textView.setEnabled(false);
                    container.textView.setOnClickListener(null); // Remove click listener for future dates
                    container.textView.setBackground(null);
                } else {
                    container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                    container.textView.setEnabled(true);

                    // Set background based on whether the date is the active date
                    if (date.equals(activeDate)) {
                        container.textView.setBackgroundResource(R.drawable.active_date_background);
                        container.textView.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                    } else {
                        container.textView.setBackground(null); // Clear background for non-active dates
                    }

                    // Add click listener for selectable dates
                    container.textView.setOnClickListener(v -> {
                        // Update the activeDate to the clicked date
                        if (activeDate != date) { // Don't call the date change again if it's already the active date.
                            activeDate = date;
                            weekCalendarView.notifyCalendarChanged(); // Refresh calendar to apply background

                            // Optional: Show selected date in a Toast or perform other actions
                            Toast.makeText(requireContext(), "Selected date: " + date, Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            }
        });

        LocalDate startDate = currentWeek.minusWeeks(10); // Customize start range as needed
        LocalDate endDate = currentWeek; // End date is the current week to prevent forward navigation

        weekCalendarView.setup(startDate, endDate, DayOfWeek.SUNDAY);
        weekCalendarView.scrollToWeek(currentWeek);

        // Use weekScrollListener to restrict forward navigation
        weekCalendarView.setWeekScrollListener(week -> {
            LocalDate firstDayOfWeek = week.getDays().get(0).getDate(); // Access the first day of the week
            if (firstDayOfWeek.isAfter(currentWeek)) {
                // Prevent scrolling forward by scrolling back to the current week
                weekCalendarView.scrollToWeek(currentWeek);
            } else {
                // Update the week title if scrolling to a previous week
                updateWeekTitle(weekTitle, firstDayOfWeek);
            }
            return null;
        });
    }

    private void updateWeekTitle(TextView weekTitle, LocalDate date) {
        LocalDate startOfWeek = date.with(DayOfWeek.SUNDAY);
        LocalDate endOfWeek = date.with(DayOfWeek.SATURDAY);

        // Ensure the dates fall in the correct order
        if (endOfWeek.isBefore(startOfWeek)) {
            endOfWeek = endOfWeek.plusWeeks(1);
        }

        weekTitle.setText(String.format("%s - %s",
                startOfWeek.format(DateTimeFormatter.ofPattern("MMM d")),
                endOfWeek.format(DateTimeFormatter.ofPattern("MMM d"))));
    }
}