package com.example.storyappjava.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.storyappjava.R;

import java.util.Objects;

public class CustomEditText extends AppCompatEditText {
    private int defaultBorderColor = Color.LTGRAY;
    private int focusedBorderColor = Color.BLUE;
    private int errorBorderColor = Color.RED;
    private float cornerRadius = 20;
    private Drawable prefixIcon;
    private int maxLines = 1;
    private boolean scrollHorizontally = true;

    public CustomEditText(@NonNull Context context) {
        super(context);
        init(null);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
                updateBorderDrawable(focusedBorderColor);
            } else {
                updateBorderDrawable(defaultBorderColor);
            }
        });
    }

    private void setPrefixIcon(Drawable drawable) {
        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        setCompoundDrawablePadding(16);
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
