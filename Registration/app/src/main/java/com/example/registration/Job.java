package com.example.registration;



public class Job {
    private String id;
    private String jobTitle;
    private String company;

    // Default constructor (required for Firestore)
    public Job() {}

    // Constructor with parameters
    public Job(String id, String jobTitle, String company) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.company = company;
    }

    // Getter for jobTitle
    public String getJobTitle() {
        return jobTitle;
    }

    // Setter for jobTitle
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    // Getter for company
    public String getCompany() {
        return company;
    }

    // Setter for company
    public void setCompany(String company) {
        this.company = company;
    }

    // Getter and Setter for id (optional if you need it)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
