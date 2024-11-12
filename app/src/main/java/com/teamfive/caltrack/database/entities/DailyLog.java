package com.teamfive.caltrack.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.teamfive.caltrack.database.helpers.DateHelper;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "daily_logs")
public class DailyLog implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "journal")
    private String journal;

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
     * @param date The date of the log.
     * @param goalCalories The calories goal for the day.
     * @param goalCarbs The carbs goal for the day.
     * @param goalFat The fat goal for the day.
     * @param goalProtein The protein goal for the day.
     */
    public DailyLog(Date date, int goalCalories, int goalCarbs, int goalFat, int goalProtein) {
        this.date = DateHelper.normalizeDate(date);
        this.journal = "";
        this.goalCalories = goalCalories;
        this.goalCarbs = goalCarbs;
        this.goalFat = goalFat;
        this.goalProtein = goalProtein;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = DateHelper.normalizeDate(date);
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

    public int getGoalCarbs() {
        return goalCarbs;
    }

    public int getGoalFat() {
        return goalFat;
    }
    public int getGoalProtein() {
        return goalProtein;
    }
}
