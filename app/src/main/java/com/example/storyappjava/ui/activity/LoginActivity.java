package com.example.storyappjava.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.storyappjava.R;
import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.dto.LoginDto;
import com.example.storyappjava.data.remote.response.LoginResponse;
import com.example.storyappjava.databinding.ActivityLoginBinding;
import com.example.storyappjava.ui.MainActivity;
import com.example.storyappjava.ui.viewmodel.AuthViewModel;
import com.example.storyappjava.ui.viewmodel.ViewModelFactory;
import com.example.storyappjava.util.SharedPreferenceUtil;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = LoginActivity.class.getSimpleName();
    ActivityLoginBinding binding;
    AuthViewModel authViewModel;
    SharedPreferenceUtil pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pref = new SharedPreferenceUtil(LoginActivity.this);

        ViewModelFactory factory = ViewModelFactory.getInstance(this);
        authViewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);

        binding.main.getViewTreeObserver().addOnPreDrawListener(() -> {
            isKeyboardVisible();
            return true;
        });

        binding.tvNotYetHaveAccount.append(" ");
        binding.tvRegister.setPaintFlags(binding.tvRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.tvRegister.setOnClickListener(this);

        authViewModel.getLoginResult().observe(this, result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnLogin.setVisibility(View.GONE);
                    binding.layoutNotYetHaveAccount.setVisibility(View.GONE);
                } else if (result instanceof Result.Success) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnLogin.setVisibility(View.VISIBLE);
                    binding.layoutNotYetHaveAccount.setVisibility(View.VISIBLE);

                    LoginResponse res = ((Result.Success<LoginResponse>) result).getData();
                    LoginDto data = res.getLoginResult();
                    pref.setToken(data.getToken());
                    pref.setAlreadyHaveAccount(true);
                    Toast.makeText(this,getString(R.string.success) + ": " + res.getMessage(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else if (result instanceof Result.Error) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnLogin.setVisibility(View.VISIBLE);
                    binding.layoutNotYetHaveAccount.setVisibility(View.VISIBLE);
                    Toast.makeText(this,getString(R.string.failed) + ": "+ ((Result.Error<?>) result).getError(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            if (validateLogin()) {
                authViewModel.login(
                        this,
                        Objects.requireNonNull(binding.etEmail.getText()).toString(),
                        Objects.requireNonNull(binding.etPassword.getText()).toString());
            }
        } else if (view.getId() == R.id.tv_register) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (v instanceof EditText) {
//                Rect outRect = new Rect();
//                v.getGlobalVisibleRect(outRect);
//                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
//                    v.clearFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//        }
//        return super.dispatchTouchEvent(event);
//    }

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