package com.example.gymmembershipsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmembershipsystem.R;
import com.example.gymmembershipsystem.database.DatabaseHelper;
import com.example.gymmembershipsystem.models.Attendance;
import com.example.gymmembershipsystem.models.Payment;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private View btnMembers, btnPlans, btnPayments, btnAttendance, btnReports;
    private Button btnLogout;

    private TextView tvTotalMembers, tvTotalRevenue, tvCheckedIn;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dbHelper = new DatabaseHelper(this);

        btnMembers = findViewById(R.id.btnMembers);
        btnPlans = findViewById(R.id.btnPlans);
        btnPayments = findViewById(R.id.btnPayments);
        btnAttendance = findViewById(R.id.btnAttendance);
        btnReports = findViewById(R.id.btnReports);
        btnLogout = findViewById(R.id.btnLogout);

        tvTotalMembers = findViewById(R.id.tvTotalMembers);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvCheckedIn = findViewById(R.id.tvCheckedIn);

        btnMembers.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, MemberListActivity.class)));

        btnPlans.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, PlanListActivity.class)));

        btnPayments.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, PaymentListActivity.class)));

        btnAttendance.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, AttendanceActivity.class)));

        btnReports.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, ReportsActivity.class)));

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStats();
    }

    private void loadStats() {
        int totalMembers = dbHelper.getAllMembers().size();

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
        tvTotalRevenue.setText(String.format("GHS %.0f", totalRevenue));
        tvCheckedIn.setText(String.valueOf(checkedInCount));
    }
}