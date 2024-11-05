package com.teamfive.caltrack.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.teamfive.caltrack.database.entities.Goals;

import java.util.Date;

@Dao
public interface GoalsDao {


    /**
     * NOTE: This method should never be used directly, it is a helper function for the
     * updateActiveGoalAndInsertNew method which nullifies the previous goal and inserts a new
     * one.
     * @param newStartDate
     */
    @Query("UPDATE goals SET end_date = :newStartDate WHERE end_date IS NULL")
    void setEndDateForActiveGoal(Date newStartDate);

    /**
     * NOTE: This method should never be used directly, it is a helper function for the
     * updateActiveGoalAndInsertNew method which nullifies the previous goal and inserts a new
     * one.
     * @param goals
     */
    @Insert
    void insertGoal(Goals goals);

    /**
     * This method nullifies the end_date of the previous goal and inserts a new date. It should
     * never be null.
     * @param newGoal
     */
    @Transaction
    default void updateActiveGoalAndInsertNew(Goals newGoal) {
        // Step 1: Use newGoal's start_date as the end_date for the current active goal
        setEndDateForActiveGoal(newGoal.getStartDate());

        // Step 2: Insert the new goal with start_date set to newGoal's start_date
        insertGoal(newGoal);
    }
}
