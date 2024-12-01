package com.example.registration;

public class JobHistory {
    private String jobId;
    private String jobName;
    private String completionDate;
    private String location;
    private String postalCode;
    private String salary;
    private String duration;
    private String urgency;

    public JobHistory(String jobId, String jobName, String completionDate, String location, String postalCode, String salary, String duration, String urgency) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.completionDate = completionDate;
        this.location = location;
        this.postalCode = postalCode;
        this.salary = salary;
        this.duration = duration;
        this.urgency = urgency;
    }
    public String getJobId() {
        return jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public String getLocation() {
        return location;
    }
    public String getPostalCode() {
        return postalCode;
    }

    public String getSalary() {
        return salary;
    }

    public String getDuration() {
        return duration;
    }

    public String getUrgency() {
        return urgency;
    }
}



