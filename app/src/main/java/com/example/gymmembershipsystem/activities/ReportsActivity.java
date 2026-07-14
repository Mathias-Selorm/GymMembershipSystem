package com.example.gymmembershipsystem.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmembershipsystem.R;
import com.example.gymmembershipsystem.database.DatabaseHelper;
import com.example.gymmembershipsystem.models.Attendance;
import com.example.gymmembershipsystem.models.Payment;

import java.util.List;

public class ReportsActivity extends AppCompatActivity {

    private TextView tvTotalMembers, tvTotalRevenue, tvTotalPlans, tvCheckedIn;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        dbHelper = new DatabaseHelper(this);

        tvTotalMembers = findViewById(R.id.tvTotalMembers);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvTotalPlans = findViewById(R.id.tvTotalPlans);
        tvCheckedIn = findViewById(R.id.tvCheckedIn);

        loadReportData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReportData();
    }

    private void loadReportData() {
        int totalMembers = dbHelper.getAllMembers().size();
        int totalPlans = dbHelper.getAllPlans().size();

        List<Payment> payments = dbHelper.getAllPayments();
        double totalRevenue = 0;
        for (Payment p : payments) {
            totalRevenue += p.getAmount();
        }

        List<Attendance> attendanceList = dbHelper.getAllAttendance();
        int checkedInCount = 0;
        for (Attendance a : attendanceList) {
            if (a.getCheckOutTime() == null || a.getCheckOutTime().isEmpty()) {
                checkedInCount++;
            }
        }

        tvTotalMembers.setText(String.valueOf(totalMembers));
        tvTotalRevenue.setText(String.format("GHS %.2f", totalRevenue));
        tvTotalPlans.setText(String.valueOf(totalPlans));
        tvCheckedIn.setText(String.valueOf(checkedInCount));
    }
}