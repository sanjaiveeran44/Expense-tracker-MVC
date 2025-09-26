package com.expense.model;

public class Category {
    private int cid;
    private String name;
    private int count;


    public Category(int cid, String name, int count) {
        this.cid = cid;
        this.name = name;
        this.count = count;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}