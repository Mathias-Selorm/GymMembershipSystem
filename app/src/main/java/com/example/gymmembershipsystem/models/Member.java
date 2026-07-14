package com.example.gymmembershipsystem.models;

public class Member {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String joinDate;
    private int planId;

    public Member(int id, String name, String phone, String email, String joinDate, int planId) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.joinDate = joinDate;
        this.planId = planId;
    }

    // Constructor without id (used when inserting a new member)
    public Member(String name, String phone, String email, String joinDate, int planId) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.joinDate = joinDate;
        this.planId = planId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getJoinDate() { return joinDate; }
    public int getPlanId() { return planId; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setJoinDate(String joinDate) { this.joinDate = joinDate; }
    public void setPlanId(int planId) { this.planId = planId; }
}