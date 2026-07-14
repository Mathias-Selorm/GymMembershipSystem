package com.example.gymmembershipsystem.models;

public class Payment {
    private int id;
    private int memberId;
    private double amount;
    private String paymentDate;
    private String method;

    public Payment(int id, int memberId, double amount, String paymentDate, String method) {
        this.id = id;
        this.memberId = memberId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.method = method;
    }

    public Payment(int memberId, double amount, String paymentDate, String method) {
        this.memberId = memberId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.method = method;
    }

    public int getId() { return id; }
    public int getMemberId() { return memberId; }
    public double getAmount() { return amount; }
    public String getPaymentDate() { return paymentDate; }
    public String getMethod() { return method; }

    public void setId(int id) { this.id = id; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
    public void setMethod(String method) { this.method = method; }
}