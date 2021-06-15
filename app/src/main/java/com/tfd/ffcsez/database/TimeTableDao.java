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
public interface TimeTableDao {

    @Query("SELECT * FROM timetable WHERE timeTableId = :timeTableId AND (`row` = :row1 OR `row` = :row2) ORDER BY `column`")
    LiveData<List<TimeTableData>> loadTTDetails(int timeTableId, int row1, int row2);

    @Query("SELECT * FROM timetable WHERE empName = :empName AND slot = :slot")
    List<TimeTableData> loadSlotDetails(String empName, String slot);

    @Query("SELECT * FROM timetable WHERE `row` = :row AND `column` = :column")
    List<TimeTableData> loadClashSlots(int row, int column);

    @Insert
    void insertSlot(TimeTableData timeTableData);

    @Delete
    void deleteSlot(TimeTableData timeTableData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDetail(TimeTableData timeTableData);
}
