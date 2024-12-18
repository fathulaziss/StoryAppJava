package com.example.storyappjava.ui.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.example.storyappjava.R;

import java.util.Objects;

public class PasswordEditText extends AppCompatEditText {
    private int defaultBorderColor = Color.LTGRAY;
    private int focusedBorderColor = Color.BLUE;
    private int errorBorderColor = Color.RED;
    private float cornerRadius = 20;
    private Drawable prefixIcon;
    private Drawable eyeIconOn;
    private Drawable eyeIconOff;
    private boolean isPasswordVisible = false;
    private int maxLines = 1;
    private boolean scrollHorizontally = true;

    private boolean isValid = false;

    public PasswordEditText(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public PasswordEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PasswordEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

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

        setInputType(129);
        eyeIconOff = ContextCompat.getDrawable(context, R.drawable.ic_visibility_off);
        eyeIconOn = ContextCompat.getDrawable(context, R.drawable.ic_visibility_on);

        setMaxLines(maxLines);
        setHorizontallyScrolling(scrollHorizontally);
        updateBorderDrawable(defaultBorderColor);

        if (prefixIcon != null) {
            setIcon(prefixIcon, eyeIconOff);
        }

        setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                validateInput();
            } else {
                if (isValid) {
                    updateBorderDrawable(defaultBorderColor);
                } else {
                    updateBorderDrawable(errorBorderColor);
                }
            }
            updateEyeIconVisibility();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int touchPosition = (int) event.getX();
            int drawableRight = getWidth() - getPaddingRight() - eyeIconOff.getIntrinsicWidth();

            // Check if the user touched near the eye icon (on the right side of the EditText)
            if (touchPosition >= drawableRight) {
                togglePasswordVisibility();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isPasswordVisible = false;
            setCompoundDrawablesWithIntrinsicBounds(prefixIcon, null, eyeIconOff, null);
        } else {
            setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isPasswordVisible = true;
            setCompoundDrawablesWithIntrinsicBounds(prefixIcon, null, eyeIconOn, null);
        }

        setSelection(getText().length());
        invalidate();
    }

    private void setIcon(Drawable prefixIcon, Drawable suffixIcon) {
        setCompoundDrawablesWithIntrinsicBounds(prefixIcon, null, suffixIcon, null);
        setCompoundDrawablePadding(16);
    }

    private void validateInput() {
        String text = Objects.requireNonNull(getText()).toString().trim();
        if (text.isEmpty()) {
            isValid = false;
            updateBorderDrawable(errorBorderColor);
            setError("Password can't be Empty");
        } else {
            if (isValidInput(text)) {
                isValid = false;
                updateBorderDrawable(errorBorderColor);
                setError("Password can't be less than 8 character");
            } else {
                isValid = true;
                updateBorderDrawable(focusedBorderColor);
                setError(null);
            }
        }


    }

    private boolean isValidInput(String password) {
        return password.length() < 8;
    }

    private void updateBorderDrawable(int borderColor) {
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setShape(GradientDrawable.RECTANGLE);
        borderDrawable.setStroke(4, borderColor);
        borderDrawable.setCornerRadius(cornerRadius);
        borderDrawable.setColor(Color.TRANSPARENT);

        setBackground(borderDrawable);
    }

    private void updateEyeIconVisibility() {
        if (isPasswordVisible) {
            // Set the eye icon to "on" when the password is visible
            setCompoundDrawablesWithIntrinsicBounds(prefixIcon, null, eyeIconOn, null);
        } else {
            // Set the eye icon to "off" when the password is hidden
            setCompoundDrawablesWithIntrinsicBounds(prefixIcon, null, eyeIconOff, null);
        }
    }
}
