package com.example.storyappjava.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.storyappjava.R;

import java.util.Objects;

public class EmailEditText extends AppCompatEditText {
    private int defaultBorderColor = Color.LTGRAY;
    private int focusedBorderColor = Color.BLUE;
    private int errorBorderColor = Color.RED;
    private float cornerRadius = 20;
    private Drawable prefixIcon;
    private int maxLines = 1;
    private boolean scrollHorizontally = true;

    public EmailEditText(@NonNull Context context) {
        super(context);
        init(null);
    }

    public EmailEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmailEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CustomEditText,
                    0, 0);

            if (a.hasValue(R.styleable.CustomEditText_prefixIcon)) {
                prefixIcon = a.getDrawable(R.styleable.CustomEditText_prefixIcon);
            }

            defaultBorderColor = a.getColor(R.styleable.CustomEditText_borderColor, Color.LTGRAY);
            focusedBorderColor = a.getColor(R.styleable.CustomEditText_focusBorderColor, Color.BLUE);
            errorBorderColor = a.getColor(R.styleable.CustomEditText_errorBorderColor, Color.RED);
            cornerRadius = a.getDimension(R.styleable.CustomEditText_cornerRadius, 20);
            maxLines = a.getInt(R.styleable.CustomEditText_maxLines, 1);
            scrollHorizontally = a.getBoolean(R.styleable.CustomEditText_scrollHorizontally, true);

            a.recycle();
        }

        setMaxLines(maxLines);
        setHorizontallyScrolling(scrollHorizontally);
        updateBorderDrawable(defaultBorderColor);

        if (prefixIcon != null) {
            setPrefixIcon(prefixIcon);
        }

        setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                validateInput();
            } else {
                if (isValidInput(Objects.requireNonNull(getText()).toString())) {
                    updateBorderDrawable(defaultBorderColor);
                } else {
                    updateBorderDrawable(errorBorderColor);
                }
            }
        });

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });
    }

    private void setPrefixIcon(Drawable drawable) {
        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        setCompoundDrawablePadding(16);
    }

    private void validateInput() {
        String text = Objects.requireNonNull(getText()).toString().trim();
        if (text.isEmpty()) {
            updateBorderDrawable(errorBorderColor);
            setError("Email can't be Empty");
        } else {
            if (isValidInput(text)) {
                updateBorderDrawable(focusedBorderColor);
                setError(null);
            } else {
                updateBorderDrawable(errorBorderColor);
                setError("Email not valid");
            }
        }
    }

    private boolean isValidInput(String email) {
         return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void updateBorderDrawable(int borderColor) {
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setShape(GradientDrawable.RECTANGLE);
        borderDrawable.setStroke(4, borderColor);
        borderDrawable.setCornerRadius(cornerRadius);
        borderDrawable.setColor(Color.TRANSPARENT);

        setBackground(borderDrawable);
    }
}
