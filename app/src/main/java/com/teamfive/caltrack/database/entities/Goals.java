package com.teamfive.caltrack.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import com.teamfive.caltrack.database.helpers.Converters;

import java.util.Date;

@Entity(tableName = "goals")
public class Goals {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "start_date")
    @TypeConverters(Converters.class) // Converts Date to Long for Room
    private Date startDate;

    @ColumnInfo(name = "end_date")
    @TypeConverters(Converters.class)
    private Date endDate;

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
     * @param goalCalories
     * @param goalCarbs
     * @param goalFat
     * @param goalProtein
     */
    public Goals(int goalCalories, int goalCarbs, int goalFat, int goalProtein) {
        this.startDate = new Date();
        this.endDate = null;
        this.goalCalories = goalCalories;
        this.goalCarbs = goalCarbs;
        this.goalFat = goalFat;
        this.goalProtein = goalProtein;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getGoalCalories() {
        return goalCalories;
    }

    public void setGoalCalories(int goalCalories) {
        this.goalCalories = goalCalories;
    }

    public int getGoalCarbs() {
        return goalCarbs;
    }

    public void setGoalCarbs(int goalCarbs) {
        this.goalCarbs = goalCarbs;
    }

    public int getGoalFat() {
        return goalFat;
    }

    public void setGoalFat(int goalFat) {
        this.goalFat = goalFat;
    }

    public int getGoalProtein() {
        return goalProtein;
    }

    public void setGoalProtein(int goalProtein) {
        this.goalProtein = goalProtein;
    }

    // Getters and setters
}
