package com.example.gymmembershipsystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmembershipsystem.R;
import com.example.gymmembershipsystem.models.Attendance;
import com.example.gymmembershipsystem.models.Member;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    public interface OnAttendanceActionListener {
        void onCheckOut(Attendance attendance);
    }

    private List<Attendance> attendanceList;
    private final List<Member> memberList;
    private final OnAttendanceActionListener listener;

    public AttendanceAdapter(List<Attendance> attendanceList, List<Member> memberList, OnAttendanceActionListener listener) {
        this.attendanceList = attendanceList;
        this.memberList = memberList;
        this.listener = listener;
    }

    public void updateList(List<Attendance> newList) {
        this.attendanceList = newList;
        notifyDataSetChanged();
    }

    private String resolveMemberName(int memberId) {
        for (Member m : memberList) {
            if (m.getId() == memberId) return m.getName();
        }
        return "Unknown Member";
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Attendance attendance = attendanceList.get(position);
        String name = resolveMemberName(attendance.getMemberId());

        String initial = name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase();
        holder.tvAttendanceAvatar.setText(initial);
        holder.tvAttendanceMember.setText(name);

        String checkIn = attendance.getCheckInTime();
        String checkOut = attendance.getCheckOutTime();

        String inTime = checkIn != null && checkIn.contains(" ") ? checkIn.split(" ")[1] : checkIn;

        if (checkOut == null || checkOut.isEmpty()) {
            holder.tvTimeInfo.setText("In: " + inTime + " · Out: —");
            holder.tvStatusBadge.setText("ACTIVE");
            holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_status_active);
            holder.tvStatusBadge.setTextColor(
                    holder.itemView.getResources().getColor(R.color.accent_orange_light));
            holder.btnCheckOut.setVisibility(View.VISIBLE);
        } else {
            String outTime = checkOut.contains(" ") ? checkOut.split(" ")[1] : checkOut;
            holder.tvTimeInfo.setText("In: " + inTime + " · Out: " + outTime);
            holder.tvStatusBadge.setText("DONE");
            holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_status_done);
            holder.tvStatusBadge.setTextColor(
                    holder.itemView.getResources().getColor(R.color.text_muted));
            holder.btnCheckOut.setVisibility(View.GONE);
        }

        holder.btnCheckOut.setOnClickListener(v -> listener.onCheckOut(attendance));
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvAttendanceAvatar, tvAttendanceMember, tvTimeInfo, tvStatusBadge, btnCheckOut;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAttendanceAvatar = itemView.findViewById(R.id.tvAttendanceAvatar);
            tvAttendanceMember = itemView.findViewById(R.id.tvAttendanceMember);
            tvTimeInfo = itemView.findViewById(R.id.tvTimeInfo);
            tvStatusBadge = itemView.findViewById(R.id.tvStatusBadge);
            btnCheckOut = itemView.findViewById(R.id.btnCheckOut);
        }
    }
}