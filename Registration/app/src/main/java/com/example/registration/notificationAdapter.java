package com.example.registration;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

//link favorites and jobs
public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.ViewHolder> {
    private final Context context;
    private List<Job> notifications;

    public notificationAdapter(Context context, List<Job> notifications) {
        this.notifications = notifications;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Job job = notifications.get(position);

        holder.jobTitleTextView.setText(job.getJobName());
        holder.companyNameTextView.setText(job.getSalary());
        holder.jobDescriptionTextView.setText(job.getLocation());
        // Set up a click listener for the notification item
        holder.itemView.setOnClickListener(v -> {
            // Handle click action, e.g., open job details
            Intent intent = new Intent(context, JobListActivity.class);
            intent.putExtra("jobId", job.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitleTextView, companyNameTextView, jobDescriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.jobTitleNotification);
            companyNameTextView = itemView.findViewById(R.id.SalaryNotification);
        }
    }
}
