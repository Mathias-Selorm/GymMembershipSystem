package com.example.gymmembershipsystem.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.gymmembershipsystem.R;
import com.example.gymmembershipsystem.database.DatabaseHelper;
import com.example.gymmembershipsystem.models.Plan;

public class AddEditPlanActivity extends AppCompatActivity {

    private EditText editPlanName, editDuration, editPrice;
    private AppCompatButton btnSavePlan;
    private TextView tvPlanFormTitle, tvPreviewLabel, tvPreviewPrice;
    private TextView btnBack;

    private DatabaseHelper dbHelper;
    private int planId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_plan);

        dbHelper = new DatabaseHelper(this);

        tvPlanFormTitle = findViewById(R.id.tvPlanFormTitle);
        tvPreviewLabel = findViewById(R.id.tvPreviewLabel);
        tvPreviewPrice = findViewById(R.id.tvPreviewPrice);
        btnBack = findViewById(R.id.btnBack);
        editPlanName = findViewById(R.id.editPlanName);
        editDuration = findViewById(R.id.editDuration);
        editPrice = findViewById(R.id.editPrice);
        btnSavePlan = findViewById(R.id.btnSavePlan);

        btnBack.setOnClickListener(v -> finish());

        TextWatcher previewWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePreview();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        editPlanName.addTextChangedListener(previewWatcher);
        editDuration.addTextChangedListener(previewWatcher);
        editPrice.addTextChangedListener(previewWatcher);

        planId = getIntent().getIntExtra("plan_id", -1);
        if (planId != -1) {
            tvPlanFormTitle.setText("Edit Plan");
            btnSavePlan.setText("Update Plan");
            loadPlanData(planId);
        }

        btnSavePlan.setOnClickListener(v -> savePlan());
    }

    private void updatePreview() {
        String name = editPlanName.getText().toString().trim();
        String duration = editDuration.getText().toString().trim();
        String price = editPrice.getText().toString().trim();

        String displayName = name.isEmpty() ? "Plan Name" : name;
        String displayDuration = duration.isEmpty() ? "--" : duration;
        String displayPrice = price.isEmpty() ? "0" : price;

        tvPreviewLabel.setText(displayName + " · " + displayDuration + " month(s)");
        tvPreviewPrice.setText("GHS " + displayPrice);
    }

    private void loadPlanData(int id) {
        for (Plan p : dbHelper.getAllPlans()) {
            if (p.getId() == id) {
                editPlanName.setText(p.getPlanName());
                editDuration.setText(String.valueOf(p.getDurationMonths()));
                editPrice.setText(String.valueOf(p.getPrice()));
                break;
            }
        }
        updatePreview();
    }

    private void savePlan() {
        String name = editPlanName.getText().toString().trim();
        String durationStr = editDuration.getText().toString().trim();
        String priceStr = editPrice.getText().toString().trim();

        if (name.isEmpty()) {
            editPlanName.setError("Plan name is required");
            editPlanName.requestFocus();
            return;
        }

        if (durationStr.isEmpty()) {
            editDuration.setError("Duration is required");
            editDuration.requestFocus();
            return;
        }

        if (priceStr.isEmpty()) {
            editPrice.setError("Price is required");
            editPrice.requestFocus();
            return;
        }

        int duration = Integer.parseInt(durationStr);
        double price = Double.parseDouble(priceStr);

        if (duration <= 0) {
            editDuration.setError("Duration must be greater than 0");
            editDuration.requestFocus();
            return;
        }

        if (price <= 0) {
            editPrice.setError("Price must be greater than 0");
            editPrice.requestFocus();
            return;
        }

        if (planId == -1) {
            Plan plan = new Plan(name, duration, price);
            long result = dbHelper.addPlan(plan);
            if (result != -1) {
                Toast.makeText(this, "Plan added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add plan", Toast.LENGTH_SHORT).show();
            }
        } else {
            Plan plan = new Plan(planId, name, duration, price);
            int rows = dbHelper.updatePlan(plan);
            if (rows > 0) {
                Toast.makeText(this, "Plan updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update plan", Toast.LENGTH_SHORT).show();
            }
        }
    }
}