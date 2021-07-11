package com.tfd.ffcsez.models;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CourseData {

    @PrimaryKey
    private String _id;
    private String batch;
    private String c;
    private String classOption;
    private String courseCode;
    private String courseStatus;
    private String courseTitle;
    private String courseType;
    private String empName;
    private String empSchool;
    private String j;
    private String l;
    private String p;
    private String roomNumber;
    private String slot;
    private String t;
    private String time;

    public CourseData(String _id, String batch, String c, String classOption,
                      String courseCode, String courseStatus, String courseTitle,
                      String courseType, String empName, String empSchool, String j,
                      String l, String p, String roomNumber, String slot, String t, String time) {
        this._id = _id;
        this.batch = batch;
        this.c = c;
        this.classOption = classOption;
        this.courseCode = courseCode;
        this.courseStatus = courseStatus;
        this.courseTitle = courseTitle;
        this.courseType = courseType;
        this.empName = empName;
        this.empSchool = empSchool;
        this.j = j;
        this.l = l;
        this.p = p;
        this.roomNumber = roomNumber;
        this.slot = slot;
        this.t = t;
        this.time = time;
    }

    // Standard getters & setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getClassOption() {
        return classOption;
    }

    public void setClassOption(String classOption) {
        this.classOption = classOption;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
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

    public String getJ() {
        return j;
    }

    public void setJ(String j) {
        this.j = j;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}