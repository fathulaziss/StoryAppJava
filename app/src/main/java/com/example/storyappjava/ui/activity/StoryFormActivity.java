package com.example.storyappjava.ui.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.example.storyappjava.BuildConfig;
import com.example.storyappjava.R;
import com.example.storyappjava.databinding.ActivityStoryFormBinding;

import java.io.File;

public class StoryFormActivity extends AppCompatActivity {

    private final String TAG = StoryFormActivity.class.getSimpleName();
    private ActivityStoryFormBinding binding;
    private Uri currentImageUri;

    private final ActivityResultLauncher<PickVisualMediaRequest> launcherGallery =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        // Check if the selected media is an image
                        String mimeType = getContentResolver().getType(uri);
                        if (mimeType != null && mimeType.startsWith("image/")) {
                            // Set the image URI to the ImageView
                            binding.ivPhoto.setImageURI(uri);
                        } else {
                            // Handle case if it's not an image (this shouldn't happen with the filter, but it's a safeguard)
                            Toast.makeText(StoryFormActivity.this, "Selected media is not an image", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(StoryFormActivity.this, "No media selected", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private final ActivityResultLauncher<Uri> launcherCamera =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean isSuccess) {
                    if (isSuccess) {
                        binding.ivPhoto.setImageURI(currentImageUri);
                    } else {
                        currentImageUri = null;
                    }
                }
            });

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

        binding.btnGallery.setOnClickListener(view -> {
            startGallery();
        });

        binding.btnCamera.setOnClickListener(view -> {
            startCamera();
        });
    }

    private void startGallery() {
        launcherGallery.launch(new PickVisualMediaRequest());
    }

    private void startCamera() {
        currentImageUri = getImageUri(this);
        if (currentImageUri != null) {
            launcherCamera.launch(currentImageUri);
        }
    }

    private Uri getImageUri(Context context) {
        // Get the directory for storing pictures
        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Construct the image file path with a timestamp
        String timeStamp = String.valueOf(System.currentTimeMillis());
        File imageFile = new File(filesDir, "story_app_" + timeStamp + ".jpg");

        // Create parent directories if they don't exist
        File parentFile = imageFile.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            boolean isCreateFileSuccess = parentFile.mkdirs();
            Log.d(TAG,"isCreateFileSuccess : " + isCreateFileSuccess);
        }

        // Get the URI using FileProvider
        return FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                imageFile
        );
    }
}