package com.example.registration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

//link favorites and jobs
public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.notificationViewHolder>{
    private Job preferredJob;
    private OnApplyJobListener onApplyJobListener;

    public notificationAdapter(Job preferredJob, OnApplyJobListener listener) {
        this.preferredJob = preferredJob;
        this.onApplyJobListener = listener;
    }
    @NonNull
    @Override
    public notificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new notificationViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull notificationViewHolder holder, int position) {
        Job job = preferredJob;

        // Handle "Apply" button click
        holder.applyButton.setOnClickListener(v -> onApplyJobListener.onApplyJob(job));
    }
    @Override
    public int getItemCount() {
        return jobList.size();
    }
    public static class notificationViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, companyName;
        Button applyButton;

        public notificationViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            companyName = itemView.findViewById(R.id.companyName);
            applyButton = itemView.findViewById(R.id.applyButton);
        }
    }
    public interface OnApplyJobListener {
        void onApplyJob(Job job);
    }
}
