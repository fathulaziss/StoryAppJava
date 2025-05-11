package com.example.storyappjava.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.storyappjava.R;
import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.dto.StoryDto;
import com.example.storyappjava.data.remote.response.StoryResponse;
import com.example.storyappjava.databinding.ActivityMainBinding;
import com.example.storyappjava.ui.activity.LoginActivity;
import com.example.storyappjava.ui.activity.StoryDetailActivity;
import com.example.storyappjava.ui.activity.StoryFormActivity;
import com.example.storyappjava.ui.adapter.list.StoriesAdapter;
import com.example.storyappjava.ui.viewmodel.StoryViewModel;
import com.example.storyappjava.ui.viewmodel.ViewModelFactory;
import com.example.storyappjava.util.SharedPreferenceUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity implements StoriesAdapter.OnItemClickListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private StoriesAdapter storiesAdapter;
    private SharedPreferenceUtil pref;

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
        StoryViewModel storyViewModel = new ViewModelProvider(this, factory).get(StoryViewModel.class);

        binding.rvStories.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvStories.setHasFixedSize(true);

        String token = "Bearer " + pref.getToken();
        Integer page = 1;
        Integer size = 10;
        Integer location = 0;
        storyViewModel.getStories(this, token, page, size, location);
        storyViewModel.getStoryResult().observe(this, result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    binding.pbLoading.setVisibility(View.VISIBLE);
                } else if (result instanceof Result.Success) {
                    binding.pbLoading.setVisibility(View.GONE);
                    List<StoryDto> stories = ((Result.Success<StoryResponse>) result).getData().getListStory();
                    storiesAdapter = new StoriesAdapter(this, stories, this);
                    binding.rvStories.setAdapter(storiesAdapter);
                } else if (result instanceof Result.Error) {
                    binding.pbLoading.setVisibility(View.GONE);
                    Toast.makeText(this,getString(R.string.failed) + ": "+ ((Result.Error<?>) result).getError(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, StoryFormActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sign_out) {
            pref.removeToken();
            Toast.makeText(this, "Signing out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(StoryDto storyDto) {
        Intent intent = new Intent(this, StoryDetailActivity.class);
        intent.putExtra("id", storyDto.getId());
        startActivity(intent);
    }
}