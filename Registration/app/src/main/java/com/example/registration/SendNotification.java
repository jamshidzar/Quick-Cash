package com.example.registration;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import com.android.volley.AuthFailureError;
import com.google.auth.oauth2.GoogleCredentials;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendNotification {

    private static final String CREDENTIALS_FILE_PATH = "key.json";
    private static final String PUSH_NOTIFICATION_ENDPOINT ="https://fcm.googleapis.com/v1/projects/quickcash-df5e5/messages:send";
    //private static final org.apache.commons.logging.Log log = LogFactory.getLog(SendNotification.class);
    private RequestQueue requestQueue;
    private Context context;
    private static String TAG = "NotificationResponse";

    public SendNotification(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.context = context;
    }
    protected void subscribeToTopic(String jobLocation) {
        FirebaseMessaging.getInstance().subscribeToTopic(jobLocation);
        Log.d(TAG, "subscribeToTopic: subscribing to topic" + jobLocation);
    }
    protected void sendJobNotification(Job job){
        getAccessToken(this.context, new AccessTokenListener() {
            @Override
            public void onAccessTokenReceived(String token) {
                Log.d(TAG, "onAccessTokenReceived: the topic of new job is "+ job.getLocation());
                sendNotification(token, job.getLocation(), job.getId(), job.getLocation());

            }
            @Override
            public void onAccessTokenError(Exception exception) {
                Toast.makeText(context,
                        "Error getting access token: " + exception.getMessage(),
                        Toast.LENGTH_LONG).show();
                exception.printStackTrace();
            }
        });

    }
    private void sendNotification(String token, String jobLocation, String jobId, String topicName) {
        try {
            JSONObject notificationJSONBody = new JSONObject();
            notificationJSONBody.put("title", "Job list");
            notificationJSONBody.put("body", "that fits your preference:");
            JSONObject dataJSONBody = new JSONObject();
            dataJSONBody.put("jobLocation", jobLocation);
            dataJSONBody.put("job_id", jobId);
            Log.d("SendNotification", "sendNotification: the jobId for sendNotification is "+ jobId);
            JSONObject messageJSONBody = new JSONObject();
            messageJSONBody.put("topic", topicName);
            Log.d(TAG, "sendNotification: topic name is " + topicName);
            messageJSONBody.put("notification", notificationJSONBody);
            messageJSONBody.put("data", dataJSONBody);

            JSONObject pushNotificationJSONBody = new JSONObject();
            pushNotificationJSONBody.put("message", messageJSONBody);
            // Add Volley request
            requestQueue = Volley.newRequestQueue(this.context);
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    PUSH_NOTIFICATION_ENDPOINT,
                    pushNotificationJSONBody,
                    response -> Toast.makeText(context,
                            "Notification Sent", Toast.LENGTH_SHORT).show(),
                    error -> {
                        Log.e(TAG, "sendNotification: could not send notification");
                        error.printStackTrace();
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            requestQueue.add(request);
        } catch (JSONException e) {
            Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void getAccessToken(Context context, AccessTokenListener listener) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                InputStream serviceAccountStream = context.getAssets().open(CREDENTIALS_FILE_PATH);
                GoogleCredentials googleCredentials = GoogleCredentials
                        .fromStream(serviceAccountStream)
                        .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));

                googleCredentials.refreshIfExpired();
                String token = googleCredentials.getAccessToken().getTokenValue();
                listener.onAccessTokenReceived(token);
            } catch (IOException e) {
                listener.onAccessTokenError(e);
            }
        });
        executorService.shutdown();
    }
}
