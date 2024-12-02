package com.example.registration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// RatingAdapter for displaying user ratings and feedback
public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {
    private List<Rating> ratingList;

    // Constructor
    public RatingAdapter(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }

    // ViewHolder class for holding the UI components for each list item
    public static class RatingViewHolder extends RecyclerView.ViewHolder {
        TextView feedbackTextView;
        TextView timestampTextView;
        RatingBar ratingBar;

        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            feedbackTextView = itemView.findViewById(R.id.feedbackTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rating, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        // Bind the data to the ViewHolder
        Rating currentRating = ratingList.get(position);

        holder.feedbackTextView.setText(currentRating.getFeedback());
        //holder.timestampTextView.setText(formatTimestamp(currentRating.getTimestamp()));
        holder.ratingBar.setRating(currentRating.getRating());
    }

    @Override
    public int getItemCount() {
        // Number of items in the list
        return ratingList.size();
    }

//    // Utility method to format timestamp into a readable date
//    private String formatTimestamp(String timestamp) {
//        // Replace this with your preferred formatting logic
//        long ts = Long.parseLong(timestamp); // Ensure proper conversion from String
//        return android.text.format.DateFormat.format("dd-MM-yyyy HH:mm", ts).toString();
//    }
}
