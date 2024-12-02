package com.example.registration;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
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
    private OnSaveToFavoritesListener onSaveToFavoritesListener;

    TextView jobTitle, companyName, location, duration, salary, urgency, postalCode;;
    Button applyButton, saveToFavoritesButton;
    public JobAdapter(List<Job> jobList, OnApplyJobListener applyListener, OnSaveToFavoritesListener favoritesListener) {
        this.jobList = jobList;
        this.onApplyJobListener = applyListener;
        this.onSaveToFavoritesListener = favoritesListener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);

        // Adding labels to each field for clarity
        holder.jobTitle.setText("Job Title: " + job.getJobName());
        holder.companyName.setText("CompanyID: " + job.getEmployerID());
        holder.location.setText("Location: " + job.getLocation());
        holder.duration.setText("Duration: " + job.getDuration() + " hours");
        holder.salary.setText("Salary: $" + job.getSalary());
        holder.urgency.setText("Urgency: " + job.getUrgency());
        holder.postalCode.setText("Postal Code: "+ job.getPostalCode());
        // Apply button click
        holder.applyButton.setOnClickListener(v -> onApplyJobListener.onApplyJob(job));
        // Save to Favorites button click
        holder.saveToFavoritesButton.setOnClickListener(v -> onSaveToFavoritesListener.onSaveToFavorites(job));
    }
    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {

        TextView jobTitle, companyName, location, duration, salary, urgency, postalCode;;
        Button applyButton, saveToFavoritesButton;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            companyName = itemView.findViewById(R.id.companyName);
            location = itemView.findViewById(R.id.location);
            duration = itemView.findViewById(R.id.duration);
            salary = itemView.findViewById(R.id.salary);
            urgency = itemView.findViewById(R.id.urgency);
            postalCode = itemView.findViewById(R.id.postalCode);
            applyButton = itemView.findViewById(R.id.applyButton);
            saveToFavoritesButton = itemView.findViewById(R.id.saveToFavoritesButton); // New button for "Save to Favorites"
        }
    }

    // Interface for "Apply" button click
    public interface OnApplyJobListener {
        void onApplyJob(Job job);
    }

    // Interface for "Save to Favorites" button click
    public interface OnSaveToFavoritesListener {
        void onSaveToFavorites(Job job);
    }
}
