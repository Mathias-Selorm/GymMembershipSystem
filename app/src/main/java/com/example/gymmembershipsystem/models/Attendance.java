package com.example.gymmembershipsystem.models;

public class Attendance {
    private int id;
    private int memberId;
    private String checkInTime;
    private String checkOutTime;

    public Attendance(int id, int memberId, String checkInTime, String checkOutTime) {
        this.id = id;
        this.memberId = memberId;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    public Attendance(int memberId, String checkInTime, String checkOutTime) {
        this.memberId = memberId;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    public int getId() { return id; }
    public int getMemberId() { return memberId; }
    public String getCheckInTime() { return checkInTime; }
    public String getCheckOutTime() { return checkOutTime; }

    public void setId(int id) { this.id = id; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }
    public void setCheckOutTime(String checkOutTime) { this.checkOutTime = checkOutTime; }
}