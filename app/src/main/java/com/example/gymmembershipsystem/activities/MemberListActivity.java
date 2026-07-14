package com.example.gymmembershipsystem.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmembershipsystem.R;
import com.example.gymmembershipsystem.adapters.MemberAdapter;
import com.example.gymmembershipsystem.database.DatabaseHelper;
import com.example.gymmembershipsystem.models.Member;

import java.util.List;

public class MemberListActivity extends AppCompatActivity implements MemberAdapter.OnMemberActionListener {

    private RecyclerView recyclerMembers;
    private EditText editSearch;
    private TextView tvEmpty, tvMemberCount;
    private View btnAddMember, btnBack;

    private DatabaseHelper dbHelper;
    private MemberAdapter adapter;
    private List<Member> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        dbHelper = new DatabaseHelper(this);

        recyclerMembers = findViewById(R.id.recyclerMembers);
        editSearch = findViewById(R.id.editSearch);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvMemberCount = findViewById(R.id.tvMemberCount);
        btnAddMember = findViewById(R.id.btnAddMember);
        btnBack = findViewById(R.id.btnBack);

        recyclerMembers.setLayoutManager(new LinearLayoutManager(this));

        loadMembers();

        btnBack.setOnClickListener(v -> finish());

        btnAddMember.setOnClickListener(v ->
                startActivity(new Intent(MemberListActivity.this, AddEditMemberActivity.class)));

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMembers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMembers();
    }

    private void loadMembers() {
        memberList = dbHelper.getAllMembers();
        updateUI();

        if (adapter == null) {
            adapter = new MemberAdapter(memberList, dbHelper, this);
            recyclerMembers.setAdapter(adapter);
        } else {
            adapter.updateList(memberList);
        }
    }

    private void filterMembers(String query) {
        List<Member> filtered = query.isEmpty()
                ? dbHelper.getAllMembers()
                : dbHelper.searchMembers(query);

        if (adapter != null) {
            adapter.updateList(filtered);
        }
        tvEmpty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void updateUI() {
        tvEmpty.setVisibility(memberList.isEmpty() ? View.VISIBLE : View.GONE);
        tvMemberCount.setText(memberList.size() + " total member" + (memberList.size() == 1 ? "" : "s"));
    }

    @Override
    public void onEdit(Member member) {
        Intent intent = new Intent(MemberListActivity.this, AddEditMemberActivity.class);
        intent.putExtra("member_id", member.getId());
        startActivity(intent);
    }

    @Override
    public void onDelete(Member member) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Member")
                .setMessage("Are you sure you want to delete " + member.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteMember(member.getId());
                    Toast.makeText(this, member.getName() + " deleted", Toast.LENGTH_SHORT).show();
                    loadMembers();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}