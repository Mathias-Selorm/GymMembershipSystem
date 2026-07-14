package com.example.gymmembershipsystem.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.gymmembershipsystem.R;
import com.example.gymmembershipsystem.database.DatabaseHelper;
import com.example.gymmembershipsystem.models.Member;
import com.example.gymmembershipsystem.models.Plan;

import java.util.Calendar;
import java.util.List;

public class AddEditMemberActivity extends AppCompatActivity {

    private EditText editName, editPhone, editEmail, editJoinDate;
    private Spinner spinnerPlan;
    private AppCompatButton btnSave;
    private TextView tvTitle, tvAvatarPreview;
    private TextView btnBack;

    private DatabaseHelper dbHelper;
    private List<Plan> planList;
    private int memberId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_member);

        dbHelper = new DatabaseHelper(this);

        tvTitle = findViewById(R.id.tvTitle);
        tvAvatarPreview = findViewById(R.id.tvAvatarPreview);
        btnBack = findViewById(R.id.btnBack);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        editJoinDate = findViewById(R.id.editJoinDate);
        spinnerPlan = findViewById(R.id.spinnerPlan);
        btnSave = findViewById(R.id.btnSave);

        btnBack.setOnClickListener(v -> finish());

        setupPlanSpinner();

        editJoinDate.setOnClickListener(v -> showDatePicker());

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    tvAvatarPreview.setText(String.valueOf(s.charAt(0)).toUpperCase());
                } else {
                    tvAvatarPreview.setText("?");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        memberId = getIntent().getIntExtra("member_id", -1);
        if (memberId != -1) {
            tvTitle.setText("Edit Member");
            btnSave.setText("Update Member");
            loadMemberData(memberId);
        }

        btnSave.setOnClickListener(v -> saveMember());
    }

    private void setupPlanSpinner() {
        planList = dbHelper.getAllPlans();

        if (planList.isEmpty()) {
            Toast.makeText(this, "Please add a membership plan first", Toast.LENGTH_LONG).show();
        }

        String[] planNames = new String[planList.size()];
        for (int i = 0; i < planList.size(); i++) {
            planNames[i] = planList.get(i).getPlanName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, planNames);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerPlan.setAdapter(adapter);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String date = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);
                    editJoinDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void loadMemberData(int id) {
        for (Member m : dbHelper.getAllMembers()) {
            if (m.getId() == id) {
                editName.setText(m.getName());
                editPhone.setText(m.getPhone());
                editEmail.setText(m.getEmail());
                editJoinDate.setText(m.getJoinDate());

                if (!m.getName().isEmpty()) {
                    tvAvatarPreview.setText(String.valueOf(m.getName().charAt(0)).toUpperCase());
                }

                for (int i = 0; i < planList.size(); i++) {
                    if (planList.get(i).getId() == m.getPlanId()) {
                        spinnerPlan.setSelection(i);
                        break;
                    }
                }
                break;
            }
        }
    }

    private void saveMember() {
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String joinDate = editJoinDate.getText().toString().trim();

        if (name.isEmpty()) {
            editName.setError("Name is required");
            editName.requestFocus();
            return;
        }

        if (phone.isEmpty() || phone.length() < 7) {
            editPhone.setError("Enter a valid phone number");
            editPhone.requestFocus();
            return;
        }

        if (joinDate.isEmpty()) {
            Toast.makeText(this, "Please select a join date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (planList.isEmpty()) {
            Toast.makeText(this, "No plans available. Add a plan first.", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPlanIndex = spinnerPlan.getSelectedItemPosition();
        int planId = planList.get(selectedPlanIndex).getId();

        if (memberId == -1) {
            Member member = new Member(name, phone, email, joinDate, planId);
            long result = dbHelper.addMember(member);
            if (result != -1) {
                Toast.makeText(this, "Member added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add member", Toast.LENGTH_SHORT).show();
            }
        } else {
            Member member = new Member(memberId, name, phone, email, joinDate, planId);
            int rows = dbHelper.updateMember(member);
            if (rows > 0) {
                Toast.makeText(this, "Member updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update member", Toast.LENGTH_SHORT).show();
            }
        }
    }
}