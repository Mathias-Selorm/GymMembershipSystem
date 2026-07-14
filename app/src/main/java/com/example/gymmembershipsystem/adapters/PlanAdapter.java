package com.example.gymmembershipsystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmembershipsystem.R;
import com.example.gymmembershipsystem.models.Plan;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    public interface OnPlanActionListener {
        void onEdit(Plan plan);
        void onDelete(Plan plan);
    }

    private List<Plan> planList;
    private final OnPlanActionListener listener;

    public PlanAdapter(List<Plan> planList, OnPlanActionListener listener) {
        this.planList = planList;
        this.listener = listener;
    }

    public void updateList(List<Plan> newList) {
        this.planList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plan plan = planList.get(position);
        holder.tvPlanName.setText(plan.getPlanName());
        holder.tvDuration.setText("Duration: " + plan.getDurationMonths() + " month(s)");
        holder.tvPrice.setText("GHS " + plan.getPrice());

        holder.btnEditPlan.setOnClickListener(v -> listener.onEdit(plan));
        holder.btnDeletePlan.setOnClickListener(v -> listener.onDelete(plan));
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlanName, tvDuration, tvPrice;
        View btnEditPlan, btnDeletePlan;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlanName = itemView.findViewById(R.id.tvPlanName);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnEditPlan = itemView.findViewById(R.id.btnEditPlan);
            btnDeletePlan = itemView.findViewById(R.id.btnDeletePlan);
        }
    }
}