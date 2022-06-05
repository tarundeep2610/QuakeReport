package com.example.quakereport;

public class Earthquake {
    private double magnitude;
    private String Cityname;
    private long Date;
    private String mUrl;

    Earthquake(double mag,String city, long date,String url){
        magnitude= mag;
        Cityname= city;
        Date= date;
        mUrl= url;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getCityname() {
        return Cityname;
    }

    public long getDate() {
        return Date;
    }

    public String getMurl(){
        return mUrl;
    }
}
