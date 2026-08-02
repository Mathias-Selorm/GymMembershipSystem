package com.example.gymmembershipsystem.utils;

import android.app.Activity;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.gymmembershipsystem.R;
import com.example.gymmembershipsystem.activities.DashboardActivity;
import com.example.gymmembershipsystem.activities.MemberListActivity;
import com.example.gymmembershipsystem.activities.PaymentListActivity;
import com.example.gymmembershipsystem.activities.ReportsActivity;

public class BottomNavHelper {

    public static final int TAB_HOME = 0;
    public static final int TAB_MEMBERS = 1;
    public static final int TAB_PAYMENTS = 2;
    public static final int TAB_REPORTS = 3;

    public static void setup(Activity activity, int activeTab) {
        LinearLayout navHome = activity.findViewById(R.id.navHome);
        LinearLayout navMembers = activity.findViewById(R.id.navMembers);
        LinearLayout navPayments = activity.findViewById(R.id.navPayments);
        LinearLayout navReports = activity.findViewById(R.id.navReports);

        TextView iconHome = activity.findViewById(R.id.iconWrapHome);
        TextView labelHome = activity.findViewById(R.id.labelHome);
        TextView iconMembers = activity.findViewById(R.id.iconWrapMembers);
        TextView labelMembers = activity.findViewById(R.id.labelMembers);
        TextView iconPayments = activity.findViewById(R.id.iconWrapPayments);
        TextView labelPayments = activity.findViewById(R.id.labelPayments);
        TextView iconReports = activity.findViewById(R.id.iconWrapReports);
        TextView labelReports = activity.findViewById(R.id.labelReports);

        reset(activity, iconHome, labelHome);
        reset(activity, iconMembers, labelMembers);
        reset(activity, iconPayments, labelPayments);
        reset(activity, iconReports, labelReports);

        switch (activeTab) {
            case TAB_HOME:
                highlight(activity, iconHome, labelHome);
                break;
            case TAB_MEMBERS:
                highlight(activity, iconMembers, labelMembers);
                break;
            case TAB_PAYMENTS:
                highlight(activity, iconPayments, labelPayments);
                break;
            case TAB_REPORTS:
                highlight(activity, iconReports, labelReports);
                break;
        }

        navHome.setOnClickListener(v -> {
            if (activeTab != TAB_HOME) {
                activity.startActivity(new Intent(activity, DashboardActivity.class));
            }
        });

        navMembers.setOnClickListener(v -> {
            if (activeTab != TAB_MEMBERS) {
                activity.startActivity(new Intent(activity, MemberListActivity.class));
            }
        });

        navPayments.setOnClickListener(v -> {
            if (activeTab != TAB_PAYMENTS) {
                activity.startActivity(new Intent(activity, PaymentListActivity.class));
            }
        });

        navReports.setOnClickListener(v -> {
            if (activeTab != TAB_REPORTS) {
                activity.startActivity(new Intent(activity, ReportsActivity.class));
            }
        });
    }

    private static void highlight(Activity activity, TextView icon, TextView label) {
        icon.setBackgroundResource(R.drawable.bg_nav_active_pill);
        label.setTextColor(ContextCompat.getColor(activity, R.color.accent_orange_light));
    }

    private static void reset(Activity activity, TextView icon, TextView label) {
        icon.setBackground(null);
        label.setTextColor(ContextCompat.getColor(activity, R.color.text_muted));
    }
}