package com.demo.jizhangapp.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String password;
    public String face;
    public String phone;
    public int age;

}
