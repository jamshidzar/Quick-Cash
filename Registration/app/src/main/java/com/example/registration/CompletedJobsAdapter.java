package com.example.registration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CompletedJobsAdapter extends RecyclerView.Adapter<CompletedJobsAdapter.AppliedJobViewHolder> {
    private List<Job> appliedJobsList;
    private OnCompleteJobListener onCompleteJobListener;

    // Constructor
    public CompletedJobsAdapter(List<Job> appliedJobsList, OnCompleteJobListener listener) {
        this.appliedJobsList = appliedJobsList;
        this.onCompleteJobListener = listener;
    }

    @NonNull
    @Override
    public AppliedJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_completed_job, parent, false);
        return new AppliedJobViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull AppliedJobViewHolder holder, int position) {
        Job job = appliedJobsList.get(position);

        // Adding labels to each field for clarity
        holder.jobTitle.setText("Job Title: " + job.getJobName());
        holder.companyName.setText("CompanyID: " + job.getEmployerID()); // Assuming employerID represents company name
        holder.location.setText("Location: " + job.getLocation());
        holder.duration.setText("Duration: " + job.getDuration() + " hours");
        holder.salary.setText("Salary: $" + job.getSalary());
        holder.urgency.setText("Urgency: " + job.getUrgency());

        // Handle "Complete" button click
        holder.payEmployee.setOnClickListener(v -> onCompleteJobListener.onCompleteJob(job));
    }


    @Override
    public int getItemCount() {
        return appliedJobsList.size();
    }

    public static class AppliedJobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, companyName, location, duration, salary, urgency;
        Button payEmployee;

        public AppliedJobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            companyName = itemView.findViewById(R.id.companyName);
            location = itemView.findViewById(R.id.location);
            duration = itemView.findViewById(R.id.duration);
            salary = itemView.findViewById(R.id.salary);
            urgency = itemView.findViewById(R.id.urgency);
            payEmployee = itemView.findViewById(R.id.payEmployee);
        }
    }

    // Listener interface for handling "Complete" button clicks
    public interface OnCompleteJobListener {
        void onCompleteJob(Job job);
    }
}
