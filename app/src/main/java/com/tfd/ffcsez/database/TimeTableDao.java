package com.tfd.ffcsez.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tfd.ffcsez.models.Coord;
import com.tfd.ffcsez.models.RegisteredCourses;

import java.util.List;

@Dao
public interface TimeTableDao {

    @Query("SELECT * FROM timetable WHERE timeTableId = :timeTableId AND `row` >= :row1 AND `row` <= :row2")
    LiveData<List<TimeTableData>> loadTT(int timeTableId, int row1, int row2);

    @Query("SELECT * FROM timetable WHERE timeTableId = :timeTableId AND (`row` = :row1 OR `row` = :row2) ORDER BY `row`, `column`, currentSlot")
    LiveData<List<TimeTableData>> loadTTDetails(int timeTableId, int row1, int row2);

    @Query("SELECT * FROM timetable WHERE empName = :empName AND slot = :slot")
    List<TimeTableData> loadSlotDetails(String empName, String slot);

    @Query("SELECT * FROM timetable WHERE `row` = :row AND `column` = :column")
    List<TimeTableData> loadClashSlots(int row, int column);

    @Query("SELECT `row`, `column` FROM timetable WHERE timeTableId = :timeTableId")
    List<Coord> getChosenSlots(int timeTableId);

    @Query(",SELECT DISTINCT courseCode, courseType, `c` FROM timetable WHERE `timeTableId` = :timeTableId ORDER BY courseCode")
    LiveData<List<RegisteredCourses>> loadCreditDetails(int timeTableId);

    @Insert
    void insertSlot(TimeTableData timeTableData);

    @Delete
    void deleteSlot(TimeTableData timeTableData);

    @Query("DELETE FROM timetable WHERE timeTableId = :timeTableId")
    void deleteAllTTSlots(int timeTableId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDetail(TimeTableData timeTableData);
}
