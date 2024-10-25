package com.teamfive.caltrack;

import android.app.Application;
import androidx.room.Room;

import com.teamfive.caltrack.database.AppDatabase;

public class CaltrackApplication extends Application {

    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "logsDatabase").build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}
