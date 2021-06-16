package com.tfd.ffcsez.models;

import androidx.room.ColumnInfo;

public class Coord {

    @ColumnInfo(name = "row")
    private int row;
    @ColumnInfo(name = "column")
    private int column;

    public Coord(int row, int column) {
        this.row = row;
        this.column = column;
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
}
