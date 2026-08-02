package com.example.gymmembershipsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmembershipsystem.R;
import com.example.gymmembershipsystem.database.DatabaseHelper;
import com.example.gymmembershipsystem.models.Attendance;
import com.example.gymmembershipsystem.models.Payment;
import com.example.gymmembershipsystem.models.Plan;
import com.example.gymmembershipsystem.utils.BottomNavHelper;

import java.util.Calendar;
import java.util.List;

public class ReportsActivity extends AppCompatActivity {

    private TextView tvTotalRevenue, tvRevenueNote, tvTotalMembers, tvTotalPlans, tvCheckedIn, tvVisitsToday;
    private LinearLayout distributionContainer;
    private View btnBack, btnViewMembers, btnViewPayments;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        dbHelper = new DatabaseHelper(this);

        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvRevenueNote = findViewById(R.id.tvRevenueNote);
        tvTotalMembers = findViewById(R.id.tvTotalMembers);
        tvTotalPlans = findViewById(R.id.tvTotalPlans);
        tvCheckedIn = findViewById(R.id.tvCheckedIn);
        tvVisitsToday = findViewById(R.id.tvVisitsToday);
        distributionContainer = findViewById(R.id.distributionContainer);
        btnBack = findViewById(R.id.btnBack);
        btnViewMembers = findViewById(R.id.btnViewMembers);
        btnViewPayments = findViewById(R.id.btnViewPayments);

        btnBack.setOnClickListener(v -> finish());

        btnViewMembers.setOnClickListener(v ->
                startActivity(new Intent(ReportsActivity.this, MemberListActivity.class)));

        btnViewPayments.setOnClickListener(v ->
                startActivity(new Intent(ReportsActivity.this, PaymentListActivity.class)));

        loadReportData();

        BottomNavHelper.setup(this, BottomNavHelper.TAB_REPORTS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReportData();
    }

    private void loadReportData() {
        int totalMembers = dbHelper.getAllMembers().size();
        List<Plan> plans = dbHelper.getAllPlans();
        int totalPlans = plans.size();

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

        String today = android.text.format.DateFormat.format("yyyy-MM-dd", Calendar.getInstance()).toString();
        int visitsToday = dbHelper.getTodayAttendanceCount(today);

        tvTotalRevenue.setText(String.format("GHS %.2f", totalRevenue));
        tvRevenueNote.setText("From " + payments.size() + " payments across all members");
        tvTotalMembers.setText(String.valueOf(totalMembers));
        tvTotalPlans.setText(String.valueOf(totalPlans));
        tvCheckedIn.setText(String.valueOf(checkedInCount));
        tvVisitsToday.setText(String.valueOf(visitsToday));

        buildDistribution(plans);
    }

    private void buildDistribution(List<Plan> plans) {
        distributionContainer.removeAllViews();

        if (plans.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No plans added yet.");
            empty.setTextColor(getResources().getColor(R.color.text_muted));
            empty.setTextSize(13);
            distributionContainer.addView(empty);
            return;
        }

        int maxCount = 1;
        int[] counts = new int[plans.size()];
        for (int i = 0; i < plans.size(); i++) {
            counts[i] = dbHelper.getMemberCountByPlanId(plans.get(i).getId());
            if (counts[i] > maxCount) maxCount = counts[i];
        }

        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < plans.size(); i++) {
            Plan plan = plans.get(i);
            int count = counts[i];

            View row = inflater.inflate(R.layout.item_plan_distribution, distributionContainer, false);
            TextView tvDistName = row.findViewById(R.id.tvDistName);
            TextView tvDistCount = row.findViewById(R.id.tvDistCount);
            View barFill = row.findViewById(R.id.barFill);

            tvDistName.setText(plan.getPlanName());
            tvDistCount.setText(count + " member" + (count == 1 ? "" : "s"));

            int percentage = maxCount == 0 ? 0 : (count * 100 / maxCount);
            if (percentage < 4 && count > 0) percentage = 4;

            FrameLayout.LayoutParams params =
                    (FrameLayout.LayoutParams) barFill.getLayoutParams();
            params.width = 0;
            barFill.setLayoutParams(params);

            barFill.setTag(percentage);

            barFill.post(() -> {
                View parent = (View) barFill.getParent();
                int parentWidth = parent.getWidth();
                FrameLayout.LayoutParams p =
                        (FrameLayout.LayoutParams) barFill.getLayoutParams();
                p.width = parentWidth * percentageSafe(barFill) / 100;
                barFill.setLayoutParams(p);
            });

            distributionContainer.addView(row);
        }
    }

    private int percentageSafe(View barFill) {
        Object tag = barFill.getTag();
        return tag instanceof Integer ? (Integer) tag : 0;
    }
}