package com.example.storyappjava.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.storyappjava.R;
import com.example.storyappjava.databinding.ActivityLoginBinding;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = LoginActivity.class.getSimpleName();
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.main.getViewTreeObserver().addOnPreDrawListener(() -> {
            isKeyboardVisible();
            return true;
        });

        binding.tvNotYetHaveAccount.append(" ");
        binding.tvRegister.setPaintFlags(binding.tvRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.tvRegister.setOnClickListener(this);

        binding.btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            if (validateLogin()) {
                Log.d(TAG, "do login");
            } else {
                Log.d(TAG,"failed to login because validation");
            }
        } else if (view.getId() == R.id.tv_register) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
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

    private boolean validateLogin() {
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError(getString(R.string.empty_email_validation));
            Toast.makeText(this,getString(R.string.empty_email_validation), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
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