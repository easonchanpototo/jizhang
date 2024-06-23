package com.demo.jizhangapp.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FinanceRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userid;
    public double price;
    public String name;
    public double upDown;
    public String startDate;
    public String endDate;
}
