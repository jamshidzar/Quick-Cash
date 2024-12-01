package com.example.registration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JobHistoryAdapter extends RecyclerView.Adapter<JobHistoryAdapter.JobHistoryViewHolder> {
    private List<JobHistory> jobHistoryList;

    public JobHistoryAdapter(List<JobHistory> jobHistoryList) {
        this.jobHistoryList = jobHistoryList;
    }

    @NonNull
    @Override
    public JobHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_history, parent, false);
        return new JobHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobHistoryViewHolder holder, int position) {
        JobHistory jobHistory = jobHistoryList.get(position);
        holder.jobName.setText(jobHistory.getJobName());
        holder.completionDate.setText(jobHistory.getCompletionDate());
        holder.location.setText(jobHistory.getLocation());
        holder.postalCode.setText(jobHistory.getPostalCode());
        holder.salary.setText("Salary: " + jobHistory.getSalary());
        holder.duration.setText("Duration: " + jobHistory.getDuration());
        holder.urgency.setText("Urgency: " + jobHistory.getUrgency());
    }

    @Override
    public int getItemCount() {
        return jobHistoryList.size();
    }

    public static class JobHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView jobName, completionDate, location, postalCode, salary, duration, urgency;

        public JobHistoryViewHolder(View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.tvJobName);
            completionDate = itemView.findViewById(R.id.tvCompletionDate);
            location = itemView.findViewById(R.id.tvLocation);
            postalCode = itemView.findViewById(R.id.tvPostalCode);
            salary = itemView.findViewById(R.id.tvSalary);
            duration = itemView.findViewById(R.id.tvDuration);
            urgency = itemView.findViewById(R.id.tvUrgency);
        }
    }
}
