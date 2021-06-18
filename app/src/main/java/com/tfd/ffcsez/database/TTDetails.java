package com.tfd.ffcsez.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "ttdetails")
public class TTDetails {

    @PrimaryKey (autoGenerate = true)
    private int timeTableId;
    private String timeTableName;

    @Ignore
    public TTDetails(String timeTableName){
        this.timeTableName = timeTableName;
    }

    public TTDetails(int timeTableId, String timeTableName){
        this.timeTableId = timeTableId;
        this.timeTableName = timeTableName;
    }

    public int getTimeTableId() {
        return timeTableId;
    }

    public void setTimeTableId(int timeTableId) {
        this.timeTableId = timeTableId;
    }

    public String getTimeTableName() {
        return timeTableName;
    }

    public void setTimeTableName(String timeTableName) {
        this.timeTableName = timeTableName;
    }
}
