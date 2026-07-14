package com.example.gymmembershipsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmembershipsystem.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 5000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View dot1 = findViewById(R.id.dot1);
        View dot2 = findViewById(R.id.dot2);
        View dot3 = findViewById(R.id.dot3);

        dot1.post(() -> {
            animateDot(dot1, 0);
            animateDot(dot2, 150);
            animateDot(dot3, 300);
        });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }

    private void animateDot(View dot, int delay) {
        dot.animate()
                .alpha(1f)
                .scaleX(1.3f)
                .scaleY(1.3f)
                .setStartDelay(delay)
                .setDuration(400)
                .withEndAction(() -> dot.animate()
                        .alpha(0.3f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(400)
                        .start())
                .start();
    }
}