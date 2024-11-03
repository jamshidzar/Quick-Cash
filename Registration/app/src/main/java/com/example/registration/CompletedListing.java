package com.example.registration;

public class CompletedListing {
    // Variables
    private String duration;
    private String employerID;
    private String id;
    private String jobName;
    private String location;
    private String paymentStatus;
    private String postalCode;
    private String salary;
    private String status;
    private String urgency;

    // Empty constructor
    public CompletedListing() {
    }

    // Regular constructor
    public CompletedListing(String duration, String employerID, String id, String jobName, String location,
                            String paymentStatus, String postalCode, String salary, String status, String urgency) {
        this.duration = duration;
        this.employerID = employerID;
        this.id = id;
        this.jobName = jobName;
        this.location = location;
        this.paymentStatus = paymentStatus;
        this.postalCode = postalCode;
        this.salary = salary;
        this.status = status;
        this.urgency = urgency;
    }

    // Getters and Setters
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEmployerID() {
        return employerID;
    }

    public void setEmployerID(String employerID) {
        this.employerID = employerID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }
}
