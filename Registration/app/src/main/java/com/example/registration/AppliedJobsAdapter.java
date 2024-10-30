package com.example.registration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppliedJobsAdapter extends RecyclerView.Adapter<AppliedJobsAdapter.AppliedJobViewHolder> {
    private List<Job> appliedJobsList;
    private OnCompleteJobListener onCompleteJobListener;

    // Constructor
    public AppliedJobsAdapter(List<Job> appliedJobsList, OnCompleteJobListener listener) {
        this.appliedJobsList = appliedJobsList;
        this.onCompleteJobListener = listener;
    }

    @NonNull
    @Override
    public AppliedJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applied_job, parent, false);
        return new AppliedJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppliedJobViewHolder holder, int position) {
        Job job = appliedJobsList.get(position);
        holder.jobTitle.setText(job.getJobTitle());
        holder.companyName.setText(job.getCompany());

        // Handle "Complete" button click
        holder.completeButton.setOnClickListener(v -> onCompleteJobListener.onCompleteJob(job));
    }

    @Override
    public int getItemCount() {
        return appliedJobsList.size();
    }

    public static class AppliedJobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, companyName;
        Button completeButton;

        public AppliedJobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            companyName = itemView.findViewById(R.id.companyName);
            completeButton = itemView.findViewById(R.id.completeButton);
        }
    }

    // Listener interface for handling "Complete" button clicks
    public interface OnCompleteJobListener {
        void onCompleteJob(Job job);
    }
}

