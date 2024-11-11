package com.teamfive.caltrack.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.teamfive.caltrack.database.entities.Goals;

@Dao
public interface GoalsDao {

    /**
     * Inserts a new goal, replacing any existing one.
     * If a goal already exists, it is deleted first.
     */
    @Transaction
    default void replaceActiveGoal(Goals newGoal) {
        clearActiveGoal(); // Remove any existing goal
        insertGoal(newGoal); // Insert the new goal
    }

    /**
     * Deletes any existing goal.
     */
    @Query("DELETE FROM goals")
    void clearActiveGoal();

    /**
     * Inserts the new goal.
     */
    @Insert
    void insertGoal(Goals goal);

    /**
     * Retrieves the active goal, if any.
     */
    @Query("SELECT * FROM goals LIMIT 1")
    LiveData<Goals> getActiveGoal();
}