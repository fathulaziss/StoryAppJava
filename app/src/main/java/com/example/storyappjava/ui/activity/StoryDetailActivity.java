package com.example.storyappjava.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.storyappjava.R;
import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.dto.StoryDto;
import com.example.storyappjava.data.remote.response.StoryResponse;
import com.example.storyappjava.databinding.ActivityStoryDetailBinding;
import com.example.storyappjava.ui.viewmodel.StoryViewModel;
import com.example.storyappjava.ui.viewmodel.ViewModelFactory;
import com.example.storyappjava.util.SharedPreferenceUtil;

public class StoryDetailActivity extends AppCompatActivity {

    private static String TAG = StoryDetailActivity.class.getSimpleName();
    private ActivityStoryDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.appBar.toolbarTitleAppBar;
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.story_detail));

        SharedPreferenceUtil pref = new SharedPreferenceUtil(StoryDetailActivity.this);

        String id = getIntent().getStringExtra("id");
        String token = "Bearer " + pref.getToken();

        ViewModelFactory factory = ViewModelFactory.getInstance(this);
        StoryViewModel storyViewModel = new ViewModelProvider(this, factory).get(StoryViewModel.class);
        storyViewModel.getDetailStory(this, token, id);
        storyViewModel.getDetailStoryResult().observe(this, result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    binding.pbLoading.setVisibility(View.VISIBLE);
                } else if (result instanceof Result.Success) {
                    binding.pbLoading.setVisibility(View.GONE);
                    StoryDto detailStory = ((Result.Success<StoryResponse>) result).getData().getDetailStory();

                    String[] names = detailStory.getName().split(" ");
                    StringBuilder nameCapitalized = new StringBuilder();

                    for (String name : names) {
                        if (!name.isEmpty()) {
                            nameCapitalized.append(Character.toUpperCase(name.charAt(0)))
                                    .append(name.substring(1).toLowerCase())
                                    .append(" ");
                        }
                    }

                    binding.tvName.setText(nameCapitalized);
                    binding.tvDesc.setText(detailStory.getDescription());
                    Glide.with(this)
                            .load(detailStory.getPhotoUrl())
                            .into(binding.ivPhoto);
                } else if (result instanceof Result.Error) {
                    binding.tvDesc.setText("Data Not Found!");
                    binding.pbLoading.setVisibility(View.GONE);
                    Toast.makeText(this,getString(R.string.failed) + ": "+ ((Result.Error<?>) result).getError(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed(); // Recommended replacement
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}