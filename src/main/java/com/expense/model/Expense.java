package com.expense.model;

import java.time.LocalDateTime;

public class Expense {
    private int eid;
    private String name;
    private double amount;
    private LocalDateTime date;
    private String categoryName;
    private int cid; // Foreign key


    public Expense(int eid, String name, double amount, LocalDateTime date, int cid, String categoryName) {
        this.eid = eid;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.cid = cid;
        this.categoryName = categoryName;
    }
    public int getEid() {
        return eid;
    }
    public void setEid(int eid) {
        this.eid = eid;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName(){
        return categoryName;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}