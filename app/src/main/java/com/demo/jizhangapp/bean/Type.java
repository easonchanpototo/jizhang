package com.demo.jizhangapp.bean;

public class Type {
    public String name;
    public int image;

    public Type( int id,String name, int image) {
        this.name = name;
        this.image = image;
        this.id = id;
    }

    public int id;
}
