package com.example.storyappjava.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.storyappjava.R;
import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.response.RegisterResponse;
import com.example.storyappjava.databinding.ActivityRegisterBinding;
import com.example.storyappjava.ui.viewmodel.AuthViewModel;
import com.example.storyappjava.ui.viewmodel.ViewModelFactory;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = RegisterActivity.class.getSimpleName();
    ActivityRegisterBinding binding;
    AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.main.getViewTreeObserver().addOnPreDrawListener(() -> {
            isKeyboardVisible();
            return true;
        });

        ViewModelFactory factory = ViewModelFactory.getInstance(this);
        authViewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);

        Toolbar toolbar = binding.appBar.toolbarTitleAppBar;
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.register));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        authViewModel.getRegisterResult().observe(this, result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnRegister.setVisibility(View.GONE);
                } else if (result instanceof Result.Success) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnRegister.setVisibility(View.VISIBLE);
                    RegisterResponse res = ((Result.Success<RegisterResponse>) result).getData();
                    Toast.makeText(this,getString(R.string.success) + ": " + res.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else if (result instanceof Result.Error) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnRegister.setVisibility(View.VISIBLE);
                    Toast.makeText(this,getString(R.string.failed) + ": "+ ((Result.Error<?>) result).getError(), Toast.LENGTH_SHORT).show();
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
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private boolean validateRegister() {
        String name = Objects.requireNonNull(binding.etName.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(name)) {
            binding.etName.requestFocus();
            binding.etName.setError(getString(R.string.empty_name_validation));
            Toast.makeText(this,getString(R.string.empty_name_validation), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            binding.etEmail.requestFocus();
            binding.etEmail.setError(getString(R.string.empty_email_validation));
            Toast.makeText(this,getString(R.string.empty_email_validation), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            binding.etPassword.requestFocus();
            binding.etPassword.setError(getString(R.string.empty_password_validation));
            Toast.makeText(this,getString(R.string.empty_password_validation), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void isKeyboardVisible() {
        Rect rect = new Rect();
        binding.main.getWindowVisibleDisplayFrame(rect);
        int screenHeight = binding.main.getHeight();
        int keypadHeight = screenHeight - rect.bottom;

        if (keypadHeight > screenHeight * 0.15) {
            binding.layoutContent.setPadding(0, 0, 0, rect.bottom / 2);
        } else {
            binding.layoutContent.setPadding(0, 0, 0, 0);
        }
    }
}