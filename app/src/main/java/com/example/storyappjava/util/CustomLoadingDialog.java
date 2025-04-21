package com.example.storyappjava.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.storyappjava.R;

import java.util.Objects;

public class CustomLoadingDialog extends Dialog {
    public CustomLoadingDialog(@NonNull Context context) {
        super(context);
        WindowManager.LayoutParams params = Objects.requireNonNull(getWindow()).getAttributes();

        params.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(params);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_loading_indicator, null);
        setContentView(view);
    }
}
