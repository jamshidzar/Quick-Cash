package com.example.registration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PreferredJobsAdapter extends BaseAdapter {

    private Context context;
    private List<String> jobDetailsList;
    private List<String> documentIds;
    private FirebaseFirestore db;

    public PreferredJobsAdapter(Context context, List<String> jobDetailsList, List<String> documentIds) {
        this.context = context;
        this.jobDetailsList = jobDetailsList;
        this.documentIds = documentIds;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public int getCount() {
        return jobDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return jobDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.preferred_job_item, parent, false);
        }

        TextView tvJobDetails = convertView.findViewById(R.id.tvJobDetails);
        Button btnDeleteJob = convertView.findViewById(R.id.btnDeleteJob);

        tvJobDetails.setText(jobDetailsList.get(position));

        btnDeleteJob.setOnClickListener(v -> {
            String documentId = documentIds.get(position);

            // Delete the job from Firestore
            db.collection("favorites").document(documentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        jobDetailsList.remove(position);
                        documentIds.remove(position);
                        notifyDataSetChanged(); // Update the list
                        Toast.makeText(context, "Job deleted successfully.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to delete job.", Toast.LENGTH_SHORT).show();
                    });
        });

        return convertView;
    }
}
