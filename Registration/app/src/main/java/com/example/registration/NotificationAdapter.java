package com.example.registration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<Notification> notificationList ;

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }
    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView titleTV,bodyTV,jobIdTV,jobLocationTV;
        public NotificationViewHolder(View view){
            super(view);
            titleTV = view.findViewById(R.id.titleTV);
            bodyTV = view.findViewById(R.id.bodyTV);
            jobIdTV = view.findViewById(R.id.jobIdTV);
            jobLocationTV = view.findViewById(R.id.jobLocationTV);
        }
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View notificationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(notificationView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.titleTV.setText(notification.getJobTitle());
        holder.bodyTV.setText(notification.getBody());
        holder.jobIdTV.setText(notification.getJobID());
        holder.jobLocationTV.setText(notification.getJobLocation());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
