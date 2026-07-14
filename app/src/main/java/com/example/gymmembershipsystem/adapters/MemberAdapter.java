package com.example.gymmembershipsystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmembershipsystem.R;
import com.example.gymmembershipsystem.database.DatabaseHelper;
import com.example.gymmembershipsystem.models.Member;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    public interface OnMemberActionListener {
        void onEdit(Member member);
        void onDelete(Member member);
    }

    private List<Member> memberList;
    private final OnMemberActionListener listener;
    private final DatabaseHelper dbHelper;

    public MemberAdapter(List<Member> memberList, DatabaseHelper dbHelper, OnMemberActionListener listener) {
        this.memberList = memberList;
        this.dbHelper = dbHelper;
        this.listener = listener;
    }

    public void updateList(List<Member> newList) {
        this.memberList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = memberList.get(position);

        String initial = member.getName().isEmpty() ? "?" : member.getName().substring(0, 1).toUpperCase();
        holder.tvAvatar.setText(initial);

        holder.tvName.setText(member.getName());
        holder.tvPhone.setText(member.getPhone());
        holder.tvJoinDate.setText("Joined: " + member.getJoinDate());

        String planName = dbHelper.getPlanNameById(member.getPlanId());
        holder.tvPlanPill.setText(planName.toUpperCase());

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(member));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(member));
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView tvAvatar, tvName, tvPhone, tvJoinDate, tvPlanPill, btnEdit, btnDelete;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAvatar = itemView.findViewById(R.id.tvAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvJoinDate = itemView.findViewById(R.id.tvJoinDate);
            tvPlanPill = itemView.findViewById(R.id.tvPlanPill);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}