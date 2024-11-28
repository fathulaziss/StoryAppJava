package com.example.storyappjava.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.storyappjava.R;
import com.example.storyappjava.databinding.ActivityOnBoardingBinding;

public class OnBoardingActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityOnBoardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            Intent intent = new Intent(OnBoardingActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_register) {
            Intent intent = new Intent(OnBoardingActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}