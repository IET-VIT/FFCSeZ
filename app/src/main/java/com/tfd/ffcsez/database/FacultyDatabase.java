package com.tfd.ffcsez.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FacultyData.class, TimeTableData.class, TTDetails.class}, version = 1, exportSchema = false)
public abstract class FacultyDatabase extends RoomDatabase {

    private static final String LOG_TAG = FacultyDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "ffcsdb";
    private static FacultyDatabase instance;

    public static FacultyDatabase getInstance(Context context){
        if (instance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new db instance");
                instance = Room.databaseBuilder(context.getApplicationContext(), FacultyDatabase.class,
                        FacultyDatabase.DATABASE_NAME).build();
            }
        }
        Log.d(LOG_TAG, "Getting the db instance");
        return instance;
    }

    public abstract FacultyDao facultyDao();

    public abstract TimeTableDao timeTableDao();

    public abstract TTDetailsDao ttDetailsDao();
}
