package com.tfd.ffcsez.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tfd.ffcsez.models.CourseDetails;
import com.tfd.ffcsez.models.FacultyDetails;

import java.util.List;

@Dao
public interface FacultyDao {

    @Query("SELECT * FROM facultylist ORDER BY id")
    List<FacultyData> loadDetails();

    @Query("SELECT DISTINCT courseCode, courseTitle FROM facultylist ORDER BY courseCode")
    List<CourseDetails> loadCourses();

    @Query("SELECT DISTINCT empName, empSchool FROM facultylist WHERE courseCode LIKE :courseCode ORDER BY empName")
    List<FacultyDetails> loadFaculties(String courseCode);

    @Query("DELETE FROM facultylist")
    void deleteAll();

    @Query("SELECT * FROM facultylist WHERE courseCode LIKE :courseCode AND empName LIKE :empName ORDER BY empName")
    List<FacultyData> loadData(String courseCode, String empName);

    @Insert
    void insertDetail(FacultyData facultyData);

    @Delete
    void deleteDetail(FacultyData facultyData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDetail(FacultyData facultyData);

    @Query("SELECT * FROM facultylist WHERE courseCode LIKE :courseCode AND empName LIKE :empName " +
            "AND (courseType in (:courseTH,:courseETH,:courseELA,:courseEPJ,:courseSS,:courseLO) " +
            "AND time in (:timeFN, :timeAN)) ORDER BY empName")
    List<FacultyData> loadAsPerFilterAND(String courseCode, String empName, String courseTH,
                                      String courseETH, String courseELA, String courseEPJ,
                                      String courseSS, String courseLO, String timeFN, String timeAN);

    @Query("SELECT * FROM facultylist WHERE courseCode LIKE :courseCode AND empName LIKE :empName " +
            "AND (courseType in (:courseTH,:courseETH,:courseELA,:courseEPJ,:courseSS,:courseLO) " +
            "OR time in (:timeFN, :timeAN)) ORDER BY empName")
    List<FacultyData> loadAsPerFilterOR(String courseCode, String empName, String courseTH,
                                      String courseETH, String courseELA, String courseEPJ,
                                      String courseSS, String courseLO, String timeFN, String timeAN);
}
