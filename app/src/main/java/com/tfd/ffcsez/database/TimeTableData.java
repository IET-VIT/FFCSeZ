package com.tfd.ffcsez.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "timetable")
public class TimeTableData {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int row;
    private int column;
    private int timeTableId;
    private String batch;
    private String classOption;
    private String courseCode;
    private String courseStatus;
    private String courseTitle;
    private String courseType;
    private String empName;
    private String empSchool;
    private String l;
    private String t;
    private String p;
    private String j;
    private String c;
    private String roomNumber;
    private String slot;
    private String time;
    private String startTime;
    private String endTime;
    private String currentSlot;
    private boolean isClash;

    // Constructors
    @Ignore
    public TimeTableData(){

    }

    @Ignore
    public TimeTableData(int row, int column, int timeTableId, String batch, String classOption, String courseCode,
                         String courseStatus, String courseTitle, String courseType, String empName,
                         String empSchool, String l, String t, String p, String j, String c,
                         String roomNumber, String slot, String time, String startTime, String endTime,
                         String currentSlot, boolean isClash) {
        this.row = row;
        this.column = column;
        this.timeTableId = timeTableId;
        this.batch = batch;
        this.classOption = classOption;
        this.courseCode = courseCode;
        this.courseStatus = courseStatus;
        this.courseTitle = courseTitle;
        this.courseType = courseType;
        this.empName = empName;
        this.empSchool = empSchool;
        this.l = l;
        this.t = t;
        this.p = p;
        this.j = j;
        this.c = c;
        this.roomNumber = roomNumber;
        this.slot = slot;
        this.time = time;
        this.startTime = startTime;
        this.endTime = endTime;
        this.currentSlot = currentSlot;
        this.isClash = isClash;
    }

    public TimeTableData(int id, int row, int timeTableId, int column, String batch,
                         String classOption, String courseCode, String courseStatus,
                         String courseTitle, String courseType, String empName,
                         String empSchool, String l, String t, String p, String j,
                         String c, String roomNumber, String slot, String time, String startTime,
                         String endTime, String currentSlot, boolean isClash) {
        this.id = id;
        this.row = row;
        this.timeTableId = timeTableId;
        this.column = column;
        this.batch = batch;
        this.classOption = classOption;
        this.courseCode = courseCode;
        this.courseStatus = courseStatus;
        this.courseTitle = courseTitle;
        this.courseType = courseType;
        this.empName = empName;
        this.empSchool = empSchool;
        this.l = l;
        this.t = t;
        this.p = p;
        this.j = j;
        this.c = c;
        this.roomNumber = roomNumber;
        this.slot = slot;
        this.time = time;
        this.startTime = startTime;
        this.endTime = endTime;
        this.currentSlot = currentSlot;
        this.isClash = isClash;
    }

    @Ignore
    public TimeTableData(FacultyData course, int timeTableId, int row, int column, String currentSlot,
                         String startTime, String endTime, boolean isClash){
        this.batch = course.getBatch();
        this.c = course.getC();
        this.classOption = course.getClassOption();
        this.courseCode = course.getCourseCode();
        this.courseStatus = course.getCourseStatus();
        this.courseTitle = course.getCourseTitle();
        this.courseType = course.getCourseType();
        this.empName = course.getEmpName();
        this.empSchool = course.getEmpSchool();
        this.j = course.getJ();
        this.l = course.getL();
        this.p = course.getP();
        this.roomNumber = course.getRoomNumber();
        this.slot = course.getSlot();
        this.t = course.getT();
        this.time = course.getTime();
        this.startTime = startTime;
        this.endTime = endTime;
        this.currentSlot = currentSlot;
        this.timeTableId = timeTableId;
        this.row = row;
        this.column = column;
        this.isClash = isClash;
    }

    // Standard getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
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

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getJ() {
        return j;
    }

    public void setJ(String j) {
        this.j = j;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTimeTableId() {
        return timeTableId;
    }

    public void setTimeTableId(int timeTableId) {
        this.timeTableId = timeTableId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCurrentSlot() {
        return currentSlot;
    }

    public void setCurrentSlot(String currentSlot) {
        this.currentSlot = currentSlot;
    }

    public boolean isClash() {
        return isClash;
    }

    public void setClash(boolean clash) {
        isClash = clash;
    }
}
