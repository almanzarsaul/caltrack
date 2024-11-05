package com.teamfive.caltrack.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "goals")
public class Goals {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "goal_calories")
    private int goalCalories;

    @ColumnInfo(name = "goal_carbs")
    private int goalCarbs;

    @ColumnInfo(name = "goal_fat")
    private int goalFat;

    @ColumnInfo(name = "goal_protein")
    private int goalProtein;

    /**
     * Constructor
     * @param goalCalories The new calories goal for the user.
     * @param goalCarbs The new carbs goal for the user.
     * @param goalFat The new fat goal for the user.
     * @param goalProtein The new protein goal for the user.
     */
    public Goals(int goalCalories, int goalCarbs, int goalFat, int goalProtein) {
        this.goalCalories = goalCalories;
        this.goalCarbs = goalCarbs;
        this.goalFat = goalFat;
        this.goalProtein = goalProtein;
    }

    public int getGoalCalories() {
        return goalCalories;
    }

    public int getGoalCarbs() {
        return goalCarbs;
    }


    public int getGoalFat() {
        return goalFat;
    }

    public int getGoalProtein() {
        return goalProtein;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
