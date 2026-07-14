package com.example.gymmembershipsystem.models;

public class Plan {
    private int id;
    private String planName;
    private int durationMonths;
    private double price;

    public Plan(int id, String planName, int durationMonths, double price) {
        this.id = id;
        this.planName = planName;
        this.durationMonths = durationMonths;
        this.price = price;
    }

    public Plan(String planName, int durationMonths, double price) {
        this.planName = planName;
        this.durationMonths = durationMonths;
        this.price = price;
    }

    public int getId() { return id; }
    public String getPlanName() { return planName; }
    public int getDurationMonths() { return durationMonths; }
    public double getPrice() { return price; }

    public void setId(int id) { this.id = id; }
    public void setPlanName(String planName) { this.planName = planName; }
    public void setDurationMonths(int durationMonths) { this.durationMonths = durationMonths; }
    public void setPrice(double price) { this.price = price; }
}