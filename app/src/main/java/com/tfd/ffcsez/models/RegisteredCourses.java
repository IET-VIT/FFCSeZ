package com.tfd.ffcsez.models;

import androidx.room.ColumnInfo;

public class RegisteredCourses {

    @ColumnInfo(name = "courseType")
    private String courseType;

    @ColumnInfo(name = "courseCode")
    private String courseCode;

    @ColumnInfo(name = "c")
    private int c;

    public RegisteredCourses(String courseType, String courseCode, int c) {
        this.courseType = courseType;
        this.courseCode = courseCode;
        this.c = c;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }
}
