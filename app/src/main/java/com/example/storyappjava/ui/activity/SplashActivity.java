package com.example.storyappjava.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.storyappjava.R;
import com.example.storyappjava.databinding.ActivitySplashBinding;
import com.example.storyappjava.ui.MainActivity;
import com.example.storyappjava.util.SharedPreferenceUtil;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    SharedPreferenceUtil pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pref = new SharedPreferenceUtil(getApplicationContext());
        checkToken();
    }

    private void checkToken() {
        new Handler().postDelayed(() -> {
            Intent intent;

            if (pref.getToken().isEmpty()) {
                if (pref.isAlreadyHaveAccount()) {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, OnBoardingActivity.class);
                }
            } else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }

            startActivity(intent);
            finish();
        }, 1000);
    }
}