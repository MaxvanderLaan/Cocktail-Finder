package com.maxvanderlaan.cocktailfinder.ui.cocktail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maxvanderlaan.cocktailfinder.R;
import com.maxvanderlaan.cocktailfinder.model.Prepared;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PrepareFormActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE_REQUEST = 1;
    private EditText ratingInput;
    private ImageView selectedImage;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_form);

        ratingInput = findViewById(R.id.rating_input);
        selectedImage = findViewById(R.id.selected_image);

        Button selectImageButton = findViewById(R.id.select_image_button);
        selectImageButton.setOnClickListener(v -> openImageSelector());

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> submitForm());
    }

    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            selectedImage.setImageURI(imageUri);
        }
    }

    private void submitForm() {
        String ratingStr = ratingInput.getText().toString();
        if (ratingStr.isEmpty() || imageUri == null) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        float rating = Float.parseFloat(ratingStr);
        if (rating < 1.0f || rating > 10.0f) {
            Toast.makeText(this, "Rating must be between 1.0 and 10.0", Toast.LENGTH_SHORT).show();
            return;
        }

        String imagePath = saveImageToInternalStorage(imageUri);
        if (imagePath != null) {
            String cocktailName = getIntent().getStringExtra("cocktail_name");
            Prepared prepared = new Prepared(cocktailName, rating, imagePath);
            savePreparedToPreferences(prepared);
            finish();
        } else {
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            File directory = getDir("prepared_images", MODE_PRIVATE);
            String fileName = System.currentTimeMillis() + ".jpg";
            File imageFile = new File(directory, fileName);
            try (OutputStream out = new FileOutputStream(imageFile)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                return imageFile.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void savePreparedToPreferences(Prepared prepared) {
        SharedPreferences sharedPreferences = getSharedPreferences("prepared_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the object to a string using your serialization method
        String preparedData = serializePrepared(prepared);

        // Retrieve the existing data and add the new one
        String existingData = sharedPreferences.getString("prepared_list", "");
        if (!existingData.isEmpty()) {
            existingData += ";";
        }
        existingData += preparedData;

        editor.putString("prepared_list", existingData);
        editor.apply();
    }

    private String serializePrepared(Prepared prepared) {
        // Simple serialization logic (e.g., CSV)
        return prepared.getCocktailName() + "," + prepared.getRating() + "," + prepared.getImagePath();
    }
}
