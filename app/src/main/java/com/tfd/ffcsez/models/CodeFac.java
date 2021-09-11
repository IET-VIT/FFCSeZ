package com.tfd.ffcsez.models;

import androidx.room.ColumnInfo;

public class CodeFac {

    @ColumnInfo(name="empName")
    private String empName;
    @ColumnInfo(name="courseCode")
    private String courseCode;
    @ColumnInfo(name="slot")
    private String slot;

    public CodeFac(String empName, String courseCode, String slot) {
        this.empName = empName;
        this.courseCode = courseCode;
        this.slot = slot;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }
}
