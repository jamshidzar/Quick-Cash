package com.example.registration;

public class Notification {
    private String jobTitleTV;
    private String bodyTV;
    private String jobIDTV;
    private String jobLocationTV;

    public Notification(String jobTitle, String body, String jobID, String jobLocation) {
        this.jobTitleTV = jobTitle;
        this.bodyTV = body;
        this.jobIDTV = jobID;
        this.jobLocationTV = jobLocation;
    }

    public String getJobTitle() {
        return jobTitleTV;
    }

    public String getBody() {
        return bodyTV;
    }

    public String getJobID() {
        return jobIDTV;
    }

    public String getJobLocation() {
        return jobLocationTV;
    }

}
