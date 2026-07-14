package com.example.gymmembershipsystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmembershipsystem.R;
import com.example.gymmembershipsystem.models.Member;
import com.example.gymmembershipsystem.models.Payment;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    public interface OnPaymentActionListener {
        void onDelete(Payment payment);
    }

    private List<Payment> paymentList;
    private final List<Member> memberList; // used to resolve member names
    private final OnPaymentActionListener listener;

    public PaymentAdapter(List<Payment> paymentList, List<Member> memberList, OnPaymentActionListener listener) {
        this.paymentList = paymentList;
        this.memberList = memberList;
        this.listener = listener;
    }

    public void updateList(List<Payment> newList) {
        this.paymentList = newList;
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
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = paymentList.get(position);
        holder.tvPaymentMember.setText(resolveMemberName(payment.getMemberId()));
        holder.tvPaymentAmount.setText("Amount: GHS " + payment.getAmount());
        holder.tvPaymentDate.setText("Date: " + payment.getPaymentDate());
        holder.tvPaymentMethod.setText("Method: " + payment.getMethod());

        holder.btnDeletePayment.setOnClickListener(v -> listener.onDelete(payment));
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tvPaymentMember, tvPaymentAmount, tvPaymentDate, tvPaymentMethod;
        Button btnDeletePayment;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPaymentMember = itemView.findViewById(R.id.tvPaymentMember);
            tvPaymentAmount = itemView.findViewById(R.id.tvPaymentAmount);
            tvPaymentDate = itemView.findViewById(R.id.tvPaymentDate);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            btnDeletePayment = itemView.findViewById(R.id.btnDeletePayment);
        }
    }
}