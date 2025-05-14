package com.example.storyappjava.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.example.storyappjava.BuildConfig;
import com.example.storyappjava.R;
import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.response.StoryResponse;
import com.example.storyappjava.databinding.ActivityStoryFormBinding;
import com.example.storyappjava.ui.viewmodel.StoryViewModel;
import com.example.storyappjava.ui.viewmodel.ViewModelFactory;
import com.example.storyappjava.util.CustomLoadingDialog;
import com.example.storyappjava.util.SharedPreferenceUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class StoryFormActivity extends AppCompatActivity {

    private final String TAG = StoryFormActivity.class.getSimpleName();
    private StoryViewModel storyViewModel;
    private ActivityStoryFormBinding binding;
    private SharedPreferenceUtil pref;
    private CustomLoadingDialog loadingDialog;
    private Uri currentImageUri;

    private final Integer page = 1;
    private final Integer size = 10;
    private final Integer location = 0;

    private final ActivityResultLauncher<PickVisualMediaRequest> launcherGallery =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        // Check if the selected media is an image
                        String mimeType = getContentResolver().getType(uri);
                        if (mimeType != null && mimeType.startsWith("image/")) {
                            // Set the image URI to the ImageView
                            currentImageUri = uri;
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

        pref = new SharedPreferenceUtil(StoryFormActivity.this);
        Toolbar toolbar = binding.appBar.toolbarTitleAppBar;
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.story_form_appbar));
        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        ViewModelFactory factory = ViewModelFactory.getInstance(this);
        storyViewModel = new ViewModelProvider(this, factory).get(StoryViewModel.class);
        storyViewModel.getUploadStoryResult().observe(this, result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    loadingDialog.show();
                } else if (result instanceof Result.Success) {
                    loadingDialog.dismiss();
                    String message = ((Result.Success<StoryResponse>) result).getData().getMessage();
                    Toast.makeText(this,getString(R.string.success) + " : "+ message, Toast.LENGTH_SHORT).show();
                    String token = "Bearer " + pref.getToken();
                    storyViewModel.getStories(this, token, page, size, location);
                    finish();
                } else if (result instanceof Result.Error) {
                    loadingDialog.dismiss();
                    Toast.makeText(this,getString(R.string.failed) + " : "+ ((Result.Error<?>) result).getError(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadingDialog = new CustomLoadingDialog(this);

        binding.etDescription.setMinHeight(300);
        binding.etDescription.setMaxHeight(300);
        binding.etDescription.setVerticalScrollBarEnabled(true);

        binding.btnGallery.setOnClickListener(view -> {
            startGallery();
        });

        binding.btnCamera.setOnClickListener(view -> {
            startCamera();
        });

        binding.btnUpload.setOnClickListener(view -> {
            String desc = Objects.requireNonNull(binding.etDescription.getText()).toString();
            String token = "Bearer " + pref.getToken();
            File file = null;
            if (currentImageUri != null) {
                try {
                    file = getFileFromUri(this, currentImageUri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            Log.d(TAG, "token = " + token);
            Log.d(TAG, "desc = " + Objects.requireNonNull(binding.etDescription.getText()).toString());
            Log.d(TAG,"file = " + Objects.requireNonNull(file).getPath());
            storyViewModel.uploadStory(this, token, desc, file, null,null);
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

    private File getFileFromUri(Context context, Uri uri) throws IOException {
        // Decode the image from the URI
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

        // Create a temporary file
        File outputDir = context.getCacheDir(); // Use cache dir for temporary file
        File outputFile = File.createTempFile("compressed_", ".jpg", outputDir);

        // Compress bitmap to file under 1 MB
        int quality = 100;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outStream);

        // Reduce quality until size is under 1MB or quality gets too low
        while (outStream.toByteArray().length / 1024 > 1024 && quality > 10) {
            outStream.reset();
            quality -= 5;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outStream);
        }

        // Write to file
        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(outStream.toByteArray());
        fos.flush();
        fos.close();

        return outputFile;
    }
}