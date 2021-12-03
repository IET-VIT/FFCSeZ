package com.tfd.ffcsez.models;

import androidx.room.ColumnInfo;

public class Coord {

    @ColumnInfo(name = "row")
    private int row;
    @ColumnInfo(name = "column")
    private int column;
    @ColumnInfo(name = "courseType")
    private String courseType;

    public Coord(int row, int column, String courseType) {
        this.row = row;
        this.column = column;
        this.courseType = courseType;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }
}
