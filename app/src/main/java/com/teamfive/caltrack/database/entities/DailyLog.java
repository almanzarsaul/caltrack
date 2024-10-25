package com.teamfive.caltrack.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "daily_logs")
public class DailyLog {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date date;
    private String journal;
    private int goalCalories;
    private int goalCarbs;
    private int goalFat;
    private int goalProtein;

    public DailyLog(Date date, int goalCalories, int goalCarbs, int goalFat, int goalProtein) {
        this.date = date;
        this.journal = "";
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
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
}
