package com.example.registration;
import com.example.registration.Job;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;



public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private List<Job> jobList;
    private OnApplyJobListener onApplyJobListener;

    public JobAdapter(List<Job> jobList, OnApplyJobListener listener) {
        this.jobList = jobList;
        this.onApplyJobListener = listener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.jobTitle.setText(job.getJobTitle());
        holder.companyName.setText(job.getCompany());

        // Handle "Apply" button click
        holder.applyButton.setOnClickListener(v -> onApplyJobListener.onApplyJob(job));
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, companyName;
        Button applyButton;

        public JobViewHolder(@NonNull View itemView) {
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
