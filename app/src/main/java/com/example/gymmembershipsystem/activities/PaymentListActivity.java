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
import com.example.gymmembershipsystem.adapters.PaymentAdapter;
import com.example.gymmembershipsystem.database.DatabaseHelper;
import com.example.gymmembershipsystem.models.Member;
import com.example.gymmembershipsystem.models.Payment;

import java.util.List;

public class PaymentListActivity extends AppCompatActivity implements PaymentAdapter.OnPaymentActionListener {

    private RecyclerView recyclerPayments;
    private TextView tvEmptyPayments, tvTotalCollected, tvPaymentCount;
    private View btnAddPayment, btnBack;

    private DatabaseHelper dbHelper;
    private PaymentAdapter adapter;
    private List<Payment> paymentList;
    private List<Member> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);

        dbHelper = new DatabaseHelper(this);

        recyclerPayments = findViewById(R.id.recyclerPayments);
        tvEmptyPayments = findViewById(R.id.tvEmptyPayments);
        tvTotalCollected = findViewById(R.id.tvTotalCollected);
        tvPaymentCount = findViewById(R.id.tvPaymentCount);
        btnAddPayment = findViewById(R.id.btnAddPayment);
        btnBack = findViewById(R.id.btnBack);

        recyclerPayments.setLayoutManager(new LinearLayoutManager(this));

        loadPayments();

        btnBack.setOnClickListener(v -> finish());

        btnAddPayment.setOnClickListener(v -> {
            if (dbHelper.getAllMembers().isEmpty()) {
                Toast.makeText(this, "Add a member first before recording payments", Toast.LENGTH_LONG).show();
                return;
            }
            startActivity(new Intent(PaymentListActivity.this, AddEditPaymentActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPayments();
    }

    private void loadPayments() {
        memberList = dbHelper.getAllMembers();
        paymentList = dbHelper.getAllPayments();

        if (adapter == null) {
            adapter = new PaymentAdapter(paymentList, memberList, this);
            recyclerPayments.setAdapter(adapter);
        } else {
            adapter.updateList(paymentList);
        }

        tvEmptyPayments.setVisibility(paymentList.isEmpty() ? View.VISIBLE : View.GONE);

        double total = 0;
        for (Payment p : paymentList) {
            total += p.getAmount();
        }
        tvTotalCollected.setText(String.format("GHS %.0f", total));
        tvPaymentCount.setText(String.valueOf(paymentList.size()));
    }

    @Override
    public void onDelete(Payment payment) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Payment")
                .setMessage("Are you sure you want to delete this payment record?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deletePayment(payment.getId());
                    Toast.makeText(this, "Payment deleted", Toast.LENGTH_SHORT).show();
                    loadPayments();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}