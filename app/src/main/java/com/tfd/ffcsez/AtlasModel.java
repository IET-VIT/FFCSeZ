package com.tfd.ffcsez;

import com.tfd.ffcsez.models.CourseData;

import java.util.List;

public class AtlasModel {
    private String status;
    private int length;
    private List<CourseData> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<CourseData> getData() {
        return data;
    }

    public void setData(List<CourseData> data) {
        this.data = data;
    }
}
