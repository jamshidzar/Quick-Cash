package com.example.registration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Job implements Serializable {
    private String id;           // Firestore document ID
    private String jobName;      // Job title
    private String employerID;   // Employer ID
    private String location;     // Job location
    private String duration;     // Job duration
    private String salary;       // Job salary
    private String urgency;      // Job urgency
    private String postalCode;
    private List<User> userList = new ArrayList<User>();
    private int state;
    // Default constructor (required for Firestore)
    public Job() {}
    // Constructor with parameters (without ID)
    public Job(String jobName, String employerID, String location, String duration, String salary, String urgency,String postalCode) {
        this.jobName = jobName;
        this.employerID = employerID;
        this.location = location;
        this.duration = duration;
        this.salary = salary;
        this.urgency = urgency;
        this.postalCode = postalCode;
    }
    public void attach(User user){
        userList.add(user);
    }

    public void setState(int state){
        this.state = state;
        notifyAllUsers();
    }

    public void notifyAllUsers() {
        for(User user : userList){
            //send notification
            user.onJobUpdate();
        }
    }
    public int getState(){
        return this.state;
    }
    // Getters and Setters for each field, including ID
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }

    public String getEmployerID() { return employerID; }
    public void setEmployerID(String employerID) { this.employerID = employerID; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }

    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    @Override
    public String toString() {
        return "Job{" +
                "id='" + id + '\'' +
                ", jobName='" + jobName + '\'' +
                ", employerID='" + employerID + '\'' +
                ", location='" + location + '\'' +
                ", duration='" + duration + '\'' +
                ", salary='" + salary + '\'' +
                ", urgency='" + urgency + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}

