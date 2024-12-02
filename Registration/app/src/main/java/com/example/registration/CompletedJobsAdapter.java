package com.example.registration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.content.Context;
import android.content.Intent;
public class CompletedJobsAdapter extends RecyclerView.Adapter<CompletedJobsAdapter.AppliedJobViewHolder> {
    private List<CompletedListing> appliedJobsList;
    private OnPaymentListener onPaymentListener;
    private OnRatingListener onRatingListener;
    private Context context;

    // Constructor
    public CompletedJobsAdapter(List<CompletedListing> appliedJobsList, OnPaymentListener listener, OnRatingListener ratingListener, Context context) {
        this.appliedJobsList = appliedJobsList;
        this.context = context; // Initialize context
        this.onPaymentListener = listener;
        this.onRatingListener = ratingListener;
    }

    @NonNull
    @Override
    public AppliedJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_completed_job, parent, false);
        return new AppliedJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppliedJobViewHolder holder, int position) {
        CompletedListing completedListing = appliedJobsList.get(position);

        // Adding labels to each field for clarity
        holder.jobTitle.setText("Job Title: " + completedListing.getJobName());
        holder.companyName.setText("CompanyID: " + completedListing.getEmployerID()); // Assuming employerID represents company name
        holder.location.setText("Location: " + completedListing.getLocation());
        holder.duration.setText("Duration: " + completedListing.getDuration() + " hours");
        holder.salary.setText("Salary: $" + completedListing.getSalary());
        holder.urgency.setText("Urgency: " + completedListing.getUrgency());

        // Handle "Complete" button click
        holder.payEmployee.setOnClickListener(v -> onPaymentListener.onPayment(completedListing));
//        holder.rateJob.setOnClickListener(v -> {
//            Intent intent = new Intent(context, RatingActivity.class); // Navigate to RatingActivity
//            intent.putExtra("jobName", completedListing.getJobName()); // Pass jobId or other relevant data
//            context.startActivity(intent);
//        });
        holder.rateJob.setOnClickListener(v -> onRatingListener.onRating(completedListing));
    }


    @Override
    public int getItemCount() {
        return appliedJobsList.size();
    }

    public static class AppliedJobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, companyName, location, duration, salary, urgency;
        Button payEmployee, rateJob;

        public AppliedJobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            companyName = itemView.findViewById(R.id.companyName);
            location = itemView.findViewById(R.id.location);
            duration = itemView.findViewById(R.id.duration);
            salary = itemView.findViewById(R.id.salary);
            urgency = itemView.findViewById(R.id.urgency);
            payEmployee = itemView.findViewById(R.id.payEmployee);
            rateJob = itemView.findViewById(R.id.rateJob);
        }
    }

    // Listener interface for handling "Complete" button clicks
    public interface OnPaymentListener {
        void onPayment(CompletedListing completedListing);
    }

    public interface OnRatingListener{
        void onRating(CompletedListing completedListing);
    }
}
