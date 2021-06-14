package com.tfd.ffcsez.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FacultyDao {

    @Query("SELECT * FROM facultylist ORDER BY id")
    List<FacultyData> loadDetails();

    @Insert
    void insertDetail(FacultyData facultyData);

    @Delete
    void deleteDetail(FacultyData facultyData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDetail(FacultyData facultyData);
}
