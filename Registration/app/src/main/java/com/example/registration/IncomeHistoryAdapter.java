package com.example.registration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class IncomeHistoryAdapter extends RecyclerView.Adapter<IncomeHistoryAdapter.IncomeViewHolder>{

    private List<JobIncomeHistory> jobList;

    public IncomeHistoryAdapter(List<JobIncomeHistory> jobList){
        this.jobList = jobList;
    }

    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the completed_job_item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_completed_incomehistory, parent, false);
        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        // Bind data to views
        JobIncomeHistory job = jobList.get(position);
        holder.jobTitle.setText("Job Name: " + job.getTitle());
        holder.location.setText("Location: " + job.getLocation());
        holder.salary.setText("Total: $" + job.getSalary());
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    // ViewHolder class for IncomeHistoryAdapter
    public static class IncomeViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, location, salary;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            location = itemView.findViewById(R.id.location);
            salary = itemView.findViewById(R.id.salary);
        }
    }
}

