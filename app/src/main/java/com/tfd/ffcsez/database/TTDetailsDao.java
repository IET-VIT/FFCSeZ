package com.tfd.ffcsez.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TTDetailsDao {

    @Query("SELECT * FROM ttdetails ORDER BY timeTableId")
    LiveData<List<TTDetails>> loadAllTimeTables();

    @Query("SELECT * FROM ttdetails WHERE timeTableName = :timeTableName ORDER BY timeTableId")
    List<TTDetails> getTimeTable(String timeTableName);

    @Query("DELETE FROM ttdetails")
    void deleteAll();

    @Insert
    void insertTimeTable(TTDetails ttDetails);

    @Delete
    void deleteTimeTable(TTDetails ttDetails);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void updateTimeTable(TTDetails ttDetails);
}
