package com.example.storyappjava.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.storyappjava.R;
import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.databinding.ActivityMainBinding;
import com.example.storyappjava.ui.viewmodel.StoryViewModel;
import com.example.storyappjava.ui.viewmodel.ViewModelFactory;
import com.example.storyappjava.util.SharedPreferenceUtil;

public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName();
    ActivityMainBinding binding;
    StoryViewModel storyViewModel;
    SharedPreferenceUtil pref;

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

        String token = "Bearer " + pref.getToken();
        storyViewModel.getStories(token, page, size, location).observe(this, result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    Toast.makeText(this,"Loading", Toast.LENGTH_SHORT).show();
                } else if (result instanceof Result.Success) {
                    Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show();
                } else if (result instanceof Result.Error) {
                    Toast.makeText(this,getString(R.string.failed) + ": "+ ((Result.Error<?>) result).getError(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}