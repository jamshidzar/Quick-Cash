package com.example.registration;

import java.io.Serializable;
import java.sql.Timestamp;

public class JobIncomeHistory implements Serializable {
    private String title;
    private String location;
    private double salary;
    private String timestamp;

    public JobIncomeHistory(){};

    public JobIncomeHistory(String title, String location, double salary, String timestamp){
        this.title = title;
        this.location = location;
        this.salary = salary;
        this.timestamp = timestamp;
    }

    public String getTitle(){
        return title;
    }

    public String getLocation(){
        return location;
    }

    public double getSalary(){
        return salary;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
}
