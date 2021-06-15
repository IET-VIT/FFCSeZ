package com.tfd.ffcsez.models;

import androidx.room.ColumnInfo;

public class FacultyDetails {

    @ColumnInfo(name="empName")
    private String empName;
    @ColumnInfo(name="empSchool")
    private String empSchool;

    public FacultyDetails(String empName, String empSchool) {
        this.empName = empName;
        this.empSchool = empSchool;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpSchool() {
        return empSchool;
    }

    public void setEmpSchool(String empSchool) {
        this.empSchool = empSchool;
    }
}
