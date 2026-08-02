package com.example.gymmembershipsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.gymmembershipsystem.models.Attendance;
import com.example.gymmembershipsystem.models.Member;
import com.example.gymmembershipsystem.models.Payment;
import com.example.gymmembershipsystem.models.Plan;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gym_management.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_MEMBERS = "members";
    public static final String TABLE_PLANS = "plans";
    public static final String TABLE_PAYMENTS = "payments";
    public static final String TABLE_ATTENDANCE = "attendance";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Plans table
        String createPlans = "CREATE TABLE " + TABLE_PLANS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "plan_name TEXT NOT NULL, " +
                "duration_months INTEGER NOT NULL, " +
                "price REAL NOT NULL)";
        db.execSQL(createPlans);

        // Members table
        String createMembers = "CREATE TABLE " + TABLE_MEMBERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "phone TEXT NOT NULL, " +
                "email TEXT, " +
                "join_date TEXT NOT NULL, " +
                "plan_id INTEGER, " +
                "FOREIGN KEY(plan_id) REFERENCES " + TABLE_PLANS + "(id))";
        db.execSQL(createMembers);

        // Payments table
        String createPayments = "CREATE TABLE " + TABLE_PAYMENTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "member_id INTEGER NOT NULL, " +
                "amount REAL NOT NULL, " +
                "payment_date TEXT NOT NULL, " +
                "method TEXT, " +
                "FOREIGN KEY(member_id) REFERENCES " + TABLE_MEMBERS + "(id))";
        db.execSQL(createPayments);

        // Attendance table
        String createAttendance = "CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "member_id INTEGER NOT NULL, " +
                "check_in_time TEXT, " +
                "check_out_time TEXT, " +
                "FOREIGN KEY(member_id) REFERENCES " + TABLE_MEMBERS + "(id))";
        db.execSQL(createAttendance);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANS);
        onCreate(db);
    }

    // ------------------- PLAN CRUD -------------------

    public long addPlan(Plan plan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("plan_name", plan.getPlanName());
        values.put("duration_months", plan.getDurationMonths());
        values.put("price", plan.getPrice());
        long id = db.insert(TABLE_PLANS, null, values);
        db.close();
        return id;
    }

    public String getPlanNameById(int planId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLANS, new String[]{"plan_name"}, "id=?",
                new String[]{String.valueOf(planId)}, null, null, null);
        String name = "No Plan";
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow("plan_name"));
        }
        cursor.close();
        db.close();
        return name;
    }

    public List<Plan> getAllPlans() {
        List<Plan> plans = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLANS, null, null, null, null, null, "id ASC");

        if (cursor.moveToFirst()) {
            do {
                Plan plan = new Plan(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("plan_name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("duration_months")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                );
                plans.add(plan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return plans;
    }

    public int updatePlan(Plan plan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("plan_name", plan.getPlanName());
        values.put("duration_months", plan.getDurationMonths());
        values.put("price", plan.getPrice());
        int rows = db.update(TABLE_PLANS, values, "id=?", new String[]{String.valueOf(plan.getId())});
        db.close();
        return rows;
    }

    public void deletePlan(int planId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLANS, "id=?", new String[]{String.valueOf(planId)});
        db.close();
    }

    // ------------------- MEMBER CRUD -------------------

    public long addMember(Member member) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", member.getName());
        values.put("phone", member.getPhone());
        values.put("email", member.getEmail());
        values.put("join_date", member.getJoinDate());
        values.put("plan_id", member.getPlanId());
        long id = db.insert(TABLE_MEMBERS, null, values);
        db.close();
        return id;
    }

    public int getMemberCountByPlanId(int planId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_MEMBERS + " WHERE plan_id=?",
                new String[]{String.valueOf(planId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEMBERS, null, null, null, null, null, "id DESC");

        if (cursor.moveToFirst()) {
            do {
                Member member = new Member(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("join_date")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("plan_id"))
                );
                members.add(member);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return members;
    }

    public List<Member> searchMembers(String query) {
        List<Member> members = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEMBERS, null, "name LIKE ? OR phone LIKE ?",
                new String[]{"%" + query + "%", "%" + query + "%"}, null, null, "id DESC");

        if (cursor.moveToFirst()) {
            do {
                Member member = new Member(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("join_date")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("plan_id"))
                );
                members.add(member);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return members;
    }

    public int updateMember(Member member) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", member.getName());
        values.put("phone", member.getPhone());
        values.put("email", member.getEmail());
        values.put("join_date", member.getJoinDate());
        values.put("plan_id", member.getPlanId());
        int rows = db.update(TABLE_MEMBERS, values, "id=?", new String[]{String.valueOf(member.getId())});
        db.close();
        return rows;
    }

    public void deleteMember(int memberId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEMBERS, "id=?", new String[]{String.valueOf(memberId)});
        db.close();
    }

    // ------------------- PAYMENT CRUD -------------------

    public long addPayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("member_id", payment.getMemberId());
        values.put("amount", payment.getAmount());
        values.put("payment_date", payment.getPaymentDate());
        values.put("method", payment.getMethod());
        long id = db.insert(TABLE_PAYMENTS, null, values);
        db.close();
        return id;
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PAYMENTS, null, null, null, null, null, "id DESC");

        if (cursor.moveToFirst()) {
            do {
                Payment payment = new Payment(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("member_id")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("amount")),
                        cursor.getString(cursor.getColumnIndexOrThrow("payment_date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("method"))
                );
                payments.add(payment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return payments;
    }

    public List<Payment> getPaymentsByMember(int memberId) {
        List<Payment> payments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PAYMENTS, null, "member_id=?",
                new String[]{String.valueOf(memberId)}, null, null, "id DESC");

        if (cursor.moveToFirst()) {
            do {
                Payment payment = new Payment(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("member_id")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("amount")),
                        cursor.getString(cursor.getColumnIndexOrThrow("payment_date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("method"))
                );
                payments.add(payment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return payments;
    }

    public void deletePayment(int paymentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PAYMENTS, "id=?", new String[]{String.valueOf(paymentId)});
        db.close();
    }

    // ------------------- ATTENDANCE CRUD -------------------

    public long addAttendance(Attendance attendance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("member_id", attendance.getMemberId());
        values.put("check_in_time", attendance.getCheckInTime());
        values.put("check_out_time", attendance.getCheckOutTime());
        long id = db.insert(TABLE_ATTENDANCE, null, values);
        db.close();
        return id;
    }

    public int getTodayAttendanceCount(String todayDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_ATTENDANCE + " WHERE check_in_time LIKE ?",
                new String[]{todayDate + "%"});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public List<Attendance> getAllAttendance() {
        List<Attendance> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTENDANCE, null, null, null, null, null, "id DESC");

        if (cursor.moveToFirst()) {
            do {
                Attendance attendance = new Attendance(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("member_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("check_in_time")),
                        cursor.getString(cursor.getColumnIndexOrThrow("check_out_time"))
                );
                list.add(attendance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void updateCheckOut(int attendanceId, String checkOutTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("check_out_time", checkOutTime);
        db.update(TABLE_ATTENDANCE, values, "id=?", new String[]{String.valueOf(attendanceId)});
        db.close();
    }
}