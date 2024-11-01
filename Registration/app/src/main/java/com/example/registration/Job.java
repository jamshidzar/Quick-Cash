package com.example.registration;

public class Job {
    private String id;           // Firestore document ID
    private String jobName;      // Job title
    private String employerID;   // Employer ID
    private String location;     // Job location
    private String duration;     // Job duration
    private String salary;       // Job salary
    private String urgency;      // Job urgency

    // Default constructor (required for Firestore)
    public Job() {}

    // Constructor with parameters (without ID)
    public Job(String jobName, String employerID, String location, String duration, String salary, String urgency) {
        this.jobName = jobName;
        this.employerID = employerID;
        this.location = location;
        this.duration = duration;
        this.salary = salary;
        this.urgency = urgency;
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
}

