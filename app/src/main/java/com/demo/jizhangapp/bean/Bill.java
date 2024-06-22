package com.demo.jizhangapp.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Bill implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userid;
    public int type;
    public int iconId;
    public String typeName;
    public String description;
    public String date;
    public double price;
}
