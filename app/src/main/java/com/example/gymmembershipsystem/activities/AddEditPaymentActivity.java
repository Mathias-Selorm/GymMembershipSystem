package com.example.gymmembershipsystem.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.gymmembershipsystem.R;
import com.example.gymmembershipsystem.database.DatabaseHelper;
import com.example.gymmembershipsystem.models.Member;
import com.example.gymmembershipsystem.models.Payment;

import java.util.Calendar;
import java.util.List;

public class AddEditPaymentActivity extends AppCompatActivity {

    private Spinner spinnerMember, spinnerMethod;
    private EditText editAmount, editPaymentDate;
    private AppCompatButton btnSavePayment;
    private android.widget.TextView btnBack;

    private DatabaseHelper dbHelper;
    private List<Member> memberList;

    private static final String[] PAYMENT_METHODS = {"Cash", "Mobile Money", "Card", "Bank Transfer"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_payment);

        dbHelper = new DatabaseHelper(this);

        btnBack = findViewById(R.id.btnBack);
        spinnerMember = findViewById(R.id.spinnerMember);
        spinnerMethod = findViewById(R.id.spinnerMethod);
        editAmount = findViewById(R.id.editAmount);
        editPaymentDate = findViewById(R.id.editPaymentDate);
        btnSavePayment = findViewById(R.id.btnSavePayment);

        btnBack.setOnClickListener(v -> finish());

        setupMemberSpinner();
        setupMethodSpinner();

        editPaymentDate.setOnClickListener(v -> showDatePicker());

        btnSavePayment.setOnClickListener(v -> savePayment());
    }

    private void setupMemberSpinner() {
        memberList = dbHelper.getAllMembers();
        String[] names = new String[memberList.size()];
        for (int i = 0; i < memberList.size(); i++) {
            names[i] = memberList.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, names);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerMember.setAdapter(adapter);
    }

    private void setupMethodSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, PAYMENT_METHODS);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerMethod.setAdapter(adapter);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String date = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);
                    editPaymentDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void savePayment() {
        String amountStr = editAmount.getText().toString().trim();
        String date = editPaymentDate.getText().toString().trim();

        if (memberList.isEmpty()) {
            Toast.makeText(this, "No members available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amountStr.isEmpty()) {
            editAmount.setError("Amount is required");
            editAmount.requestFocus();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        if (amount <= 0) {
            editAmount.setError("Amount must be greater than 0");
            editAmount.requestFocus();
            return;
        }

        if (date.isEmpty()) {
            Toast.makeText(this, "Please select a payment date", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedMemberIndex = spinnerMember.getSelectedItemPosition();
        int memberId = memberList.get(selectedMemberIndex).getId();
        String method = PAYMENT_METHODS[spinnerMethod.getSelectedItemPosition()];

        Payment payment = new Payment(memberId, amount, date, method);
        long result = dbHelper.addPayment(payment);

        if (result != -1) {
            Toast.makeText(this, "Payment recorded successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to record payment", Toast.LENGTH_SHORT).show();
        }
    }
}