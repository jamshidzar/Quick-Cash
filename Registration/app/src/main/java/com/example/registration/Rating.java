package com.example.registration;

public class Rating {
    private float rating;       // Rating value (e.g., 4.5 stars)
    private String feedback;    // Feedback text
    //private String timestamp;   // When the rating was submitted

    // Default constructor for Firebase deserialization
    public Rating() {}

    public Rating(float rating, String feedback) {
        this.rating = rating;
        this.feedback = feedback;

    }


    // Getters and Setters
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

//    public String getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(String timestamp) {
//        this.timestamp = timestamp;
//    }

}