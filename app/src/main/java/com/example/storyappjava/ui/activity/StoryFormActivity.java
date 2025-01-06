package com.example.storyappjava.ui.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.storyappjava.R;
import com.example.storyappjava.databinding.ActivityRegisterBinding;
import com.example.storyappjava.databinding.ActivityStoryFormBinding;

public class StoryFormActivity extends AppCompatActivity {

    private ActivityStoryFormBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStoryFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.appBar.toolbarTitleAppBar;
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.story_form_appbar));
        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        binding.etDescription.setMinHeight(300);
        binding.etDescription.setMaxHeight(300);
        binding.etDescription.setVerticalScrollBarEnabled(true);
    }
}