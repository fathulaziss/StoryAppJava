package com.example.storyappjava.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.storyappjava.R;
import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.response.RegisterResponse;
import com.example.storyappjava.databinding.ActivityRegisterBinding;
import com.example.storyappjava.ui.viewmodel.AuthViewModel;
import com.example.storyappjava.ui.viewmodel.ViewModelFactory;

import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = RegisterActivity.class.getSimpleName();
    ActivityRegisterBinding binding;
    AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Register");
        }
        binding.toolbar.setTitleTextColor(Color.WHITE);

        ViewModelFactory factory = ViewModelFactory.getInstance(this);
        authViewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);

        authViewModel.getRegisterResult().observe(this, result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnRegister.setVisibility(View.GONE);
                } else if (result instanceof Result.Success) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnRegister.setVisibility(View.VISIBLE);
                    RegisterResponse res = ((Result.Success<RegisterResponse>) result).getData();
                    Toast.makeText(this, "Berhasil: "+ res.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else if (result instanceof Result.Error) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnRegister.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Gagal: "+ ((Result.Error<?>) result).getError(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_register) {
            if (validateRegister()) {
                authViewModel
                        .register(
                                this,
                                Objects.requireNonNull(binding.etName.getText()).toString(),
                                Objects.requireNonNull(binding.etEmail.getText()).toString(),
                                Objects.requireNonNull(binding.etPassword.getText()).toString());
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                // Dismiss the keyboard when touching outside EditText
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                view.clearFocus();
            }
        }
        return super.onTouchEvent(event);
    }

    private boolean validateRegister() {
        String name = Objects.requireNonNull(binding.etName.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(name)) {
            binding.etName.setError("Name can't be empty");
            Toast.makeText(this,"Name can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError("Email can't be empty");
            Toast.makeText(this,"Email can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError("Password can't be empty");
            Toast.makeText(this,"Password can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}