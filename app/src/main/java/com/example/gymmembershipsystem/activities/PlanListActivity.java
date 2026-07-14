package com.example.gymmembershipsystem.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmembershipsystem.R;
import com.example.gymmembershipsystem.adapters.PlanAdapter;
import com.example.gymmembershipsystem.database.DatabaseHelper;
import com.example.gymmembershipsystem.models.Plan;

import java.util.List;

public class PlanListActivity extends AppCompatActivity implements PlanAdapter.OnPlanActionListener {

    private RecyclerView recyclerPlans;
    private TextView tvEmptyPlans, tvPlanCount;
    private View btnAddPlan, btnBack;

    private DatabaseHelper dbHelper;
    private PlanAdapter adapter;
    private List<Plan> planList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list);

        dbHelper = new DatabaseHelper(this);

        recyclerPlans = findViewById(R.id.recyclerPlans);
        tvEmptyPlans = findViewById(R.id.tvEmptyPlans);
        tvPlanCount = findViewById(R.id.tvPlanCount);
        btnAddPlan = findViewById(R.id.btnAddPlan);
        btnBack = findViewById(R.id.btnBack);

        recyclerPlans.setLayoutManager(new LinearLayoutManager(this));

        loadPlans();

        btnBack.setOnClickListener(v -> finish());

        btnAddPlan.setOnClickListener(v ->
                startActivity(new Intent(PlanListActivity.this, AddEditPlanActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPlans();
    }

    private void loadPlans() {
        planList = dbHelper.getAllPlans();

        if (adapter == null) {
            adapter = new PlanAdapter(planList, this);
            recyclerPlans.setAdapter(adapter);
        } else {
            adapter.updateList(planList);
        }

        tvEmptyPlans.setVisibility(planList.isEmpty() ? View.VISIBLE : View.GONE);
        tvPlanCount.setText(planList.size() + " active plan" + (planList.size() == 1 ? "" : "s"));
    }

    @Override
    public void onEdit(Plan plan) {
        Intent intent = new Intent(PlanListActivity.this, AddEditPlanActivity.class);
        intent.putExtra("plan_id", plan.getId());
        startActivity(intent);
    }

    @Override
    public void onDelete(Plan plan) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Plan")
                .setMessage("Are you sure you want to delete \"" + plan.getPlanName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deletePlan(plan.getId());
                    Toast.makeText(this, "Plan deleted", Toast.LENGTH_SHORT).show();
                    loadPlans();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}