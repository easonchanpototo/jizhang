package com.demo.jizhangapp.bean;

public class FinanceCompany {
    public FinanceCompany(String name, String info, double upDown,String url) {
        this.name = name;
        this.info = info;
        this.upDown = upDown;
        this.url = url;
    }

    public String name;
    public String info;
    public String url;
    public double upDown;
}
