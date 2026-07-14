package com.example.gymmembershipsystem.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmembershipsystem.R;
import com.example.gymmembershipsystem.adapters.AttendanceAdapter;
import com.example.gymmembershipsystem.database.DatabaseHelper;
import com.example.gymmembershipsystem.models.Attendance;
import com.example.gymmembershipsystem.models.Member;

import java.util.Calendar;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity implements AttendanceAdapter.OnAttendanceActionListener {

    private RecyclerView recyclerAttendance;
    private TextView tvEmptyAttendance, tvCheckedInNow, tvTotalToday;
    private View btnCheckIn, btnBack;

    private DatabaseHelper dbHelper;
    private AttendanceAdapter adapter;
    private List<Attendance> attendanceList;
    private List<Member> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        dbHelper = new DatabaseHelper(this);

        recyclerAttendance = findViewById(R.id.recyclerAttendance);
        tvEmptyAttendance = findViewById(R.id.tvEmptyAttendance);
        tvCheckedInNow = findViewById(R.id.tvCheckedInNow);
        tvTotalToday = findViewById(R.id.tvTotalToday);
        btnCheckIn = findViewById(R.id.btnCheckIn);
        btnBack = findViewById(R.id.btnBack);

        recyclerAttendance.setLayoutManager(new LinearLayoutManager(this));

        loadAttendance();

        btnBack.setOnClickListener(v -> finish());

        btnCheckIn.setOnClickListener(v -> showCheckInDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAttendance();
    }

    private String getTodayDate() {
        return DateFormat.format("yyyy-MM-dd", Calendar.getInstance()).toString();
    }

    private void loadAttendance() {
        memberList = dbHelper.getAllMembers();
        attendanceList = dbHelper.getAllAttendance();

        if (adapter == null) {
            adapter = new AttendanceAdapter(attendanceList, memberList, this);
            recyclerAttendance.setAdapter(adapter);
        } else {
            adapter.updateList(attendanceList);
        }

        tvEmptyAttendance.setVisibility(attendanceList.isEmpty() ? View.VISIBLE : View.GONE);

        int checkedInNow = 0;
        for (Attendance a : attendanceList) {
            if (a.getCheckOutTime() == null || a.getCheckOutTime().isEmpty()) {
                checkedInNow++;
            }
        }
        tvCheckedInNow.setText(String.valueOf(checkedInNow));

        int totalToday = dbHelper.getTodayAttendanceCount(getTodayDate());
        tvTotalToday.setText(String.valueOf(totalToday));
    }

    private String getCurrentTimestamp() {
        return DateFormat.format("yyyy-MM-dd HH:mm", Calendar.getInstance()).toString();
    }

    private void showCheckInDialog() {
        if (memberList.isEmpty()) {
            Toast.makeText(this, "Add a member first before checking in", Toast.LENGTH_LONG).show();
            return;
        }

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_check_in, null);
        Spinner spinner = dialogView.findViewById(R.id.spinnerCheckInMember);

        String[] names = new String[memberList.size()];
        for (int i = 0; i < memberList.size(); i++) {
            names[i] = memberList.get(i).getName();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, names);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        new AlertDialog.Builder(this)
                .setTitle("Check In")
                .setView(dialogView)
                .setPositiveButton("Check In", (dialog, which) -> {
                    int selectedIndex = spinner.getSelectedItemPosition();
                    int memberId = memberList.get(selectedIndex).getId();

                    Attendance attendance = new Attendance(memberId, getCurrentTimestamp(), null);
                    dbHelper.addAttendance(attendance);

                    Toast.makeText(this, names[selectedIndex] + " checked in", Toast.LENGTH_SHORT).show();
                    loadAttendance();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onCheckOut(Attendance attendance) {
        dbHelper.updateCheckOut(attendance.getId(), getCurrentTimestamp());
        Toast.makeText(this, "Checked out", Toast.LENGTH_SHORT).show();
        loadAttendance();
    }
}