package com.tfd.ffcsez.models;

import androidx.room.ColumnInfo;

public class CourseDetails {

    @ColumnInfo(name="courseCode")
    private String courseCode;
    @ColumnInfo(name="courseTitle")
    private String courseTitle;

    public CourseDetails(String courseCode, String courseTitle) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
}
