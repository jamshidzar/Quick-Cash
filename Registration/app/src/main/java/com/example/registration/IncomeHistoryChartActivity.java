package com.example.registration;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IncomeHistoryChartActivity extends AppCompatActivity {

    private ArrayList<JobIncomeHistory> jobs;
    private FirebaseFirestore db;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_history_chart);

        userID = getIntent().getStringExtra("userID");
        db = FirebaseSingleton.getInstance().getDb();

        jobs = (ArrayList<JobIncomeHistory>) getIntent().getSerializableExtra("jobs");

//        getData();
        sortJobs();
        setUpBarChart();
//        BarChart barChart = findViewById(R.id.barchart);
//        List<BarEntry> entries = new ArrayList<>();
//        List<String> labels = new ArrayList<>();
//
//        for (int i = 0; i < jobs.size(); i++){
//            JobIncomeHistory job = jobs.get(i);
//            entries.add(new BarEntry(i, (float) job.getSalary()));
//            labels.add(job.getTimestamp());
//        }
//
//        BarDataSet barDataSet = new BarDataSet(entries, "Income History");
//        BarData barData = new BarData(barDataSet);
//        barChart.setData(barData);
//        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        barDataSet.setValueTextColor(Color.BLACK);
//        barDataSet.setValueTextSize(16f);
//        barChart.getDescription().setEnabled(true);
//
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setGranularity(1f); // One label per bar
//        xAxis.setGranularityEnabled(true);
//
//        barChart.setFitBars(true); // Make bars fit nicely
//        barChart.animateY(1000);
//        barChart.invalidate();
    }

    private void sortJobs(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm:ss a z", Locale.CANADA);
        jobs.sort((r1, r2) -> {
            try {
                ZonedDateTime zdt1 = ZonedDateTime.parse(r1.getTimestamp(), formatter);
                ZonedDateTime zdt2 = ZonedDateTime.parse(r2.getTimestamp(), formatter);
                return zdt1.compareTo(zdt2);
            } catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        });
    }

    private void setUpBarChart(){
        BarChart barChart = findViewById(R.id.barchart);
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < jobs.size(); i++){
            JobIncomeHistory job = jobs.get(i);
            entries.add(new BarEntry(i, (float) job.getSalary()));
            labels.add(job.getTimestamp());
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Jobs");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(18f);
        barChart.getDescription().setEnabled(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // One label per bar
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(-45);

        // Enable scaling and dragging
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setScaleXEnabled(true);
        barChart.setScaleYEnabled(false);

        // Set visible range
        barChart.setVisibleXRangeMaximum(2f); // Show 2 bars at a time

        // Enable pinch-to-zoom
        barChart.setPinchZoom(true);

        barChart.setFitBars(true);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}
