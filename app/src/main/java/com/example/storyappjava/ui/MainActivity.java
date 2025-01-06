package com.example.storyappjava.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.storyappjava.R;
import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.dto.StoryDto;
import com.example.storyappjava.data.remote.response.StoryResponse;
import com.example.storyappjava.databinding.ActivityMainBinding;
import com.example.storyappjava.ui.adapter.list.StoriesAdapter;
import com.example.storyappjava.ui.viewmodel.StoryViewModel;
import com.example.storyappjava.ui.viewmodel.ViewModelFactory;
import com.example.storyappjava.util.SharedPreferenceUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private StoryViewModel storyViewModel;
    private StoriesAdapter storiesAdapter;
    private SharedPreferenceUtil pref;

    private final Integer page = 1;
    private final Integer size = 10;
    private final Integer location = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.appBar.toolbarTitleAppBar;
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        pref = new SharedPreferenceUtil(MainActivity.this);

        ViewModelFactory factory = ViewModelFactory.getInstance(this);
        storyViewModel = new ViewModelProvider(this, factory).get(StoryViewModel.class);

        binding.rvStories.setLayoutManager(new LinearLayoutManager(this));
        binding.rvStories.setHasFixedSize(true);

        String token = "Bearer " + pref.getToken();
        storyViewModel.getStories(token, page, size, location).observe(this, result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    binding.pbLoading.setVisibility(View.VISIBLE);
                } else if (result instanceof Result.Success) {
                    binding.pbLoading.setVisibility(View.GONE);
                    List<StoryDto> stories = ((Result.Success<StoryResponse>) result).getData().getListStory();
                    storiesAdapter = new StoriesAdapter(this, stories);
                    binding.rvStories.setAdapter(storiesAdapter);
                } else if (result instanceof Result.Error) {
                    binding.pbLoading.setVisibility(View.GONE);
                    Toast.makeText(this,getString(R.string.failed) + ": "+ ((Result.Error<?>) result).getError(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}