package com.example.cocktailfinder.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cocktailfinder.R;
import com.example.cocktailfinder.model.Cocktail;

import java.io.InputStream;
import java.net.URL;

public class CocktailDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cocktail_detail);

        Cocktail cocktail = getIntent().getParcelableExtra("cocktail");

        TextView nameTextView = findViewById(R.id.cocktail_name);
        TextView tagsTextView = findViewById(R.id.cocktail_tags);
        TextView categoryTextView = findViewById(R.id.cocktail_category);
        TextView alcoholicTextView = findViewById(R.id.cocktail_alcoholic);
        TextView glassTextView = findViewById(R.id.cocktail_glass);
        TextView instructionsTextView = findViewById(R.id.cocktail_instructions);
        TextView ingredientsTextView = findViewById(R.id.cocktail_ingredients);
        ImageView cocktailImageView = findViewById(R.id.cocktail_image);

        if (cocktail != null) {
            nameTextView.setText(cocktail.getStrDrink());
            tagsTextView.setText(cocktail.getStrTags());
            categoryTextView.setText(cocktail.getStrCategory());
            alcoholicTextView.setText(cocktail.getStrAlcoholic());
            glassTextView.setText(cocktail.getStrGlass());
            instructionsTextView.setText(cocktail.getStrInstructions());

            StringBuilder ingredients = new StringBuilder();
            if (cocktail.getStrIngredient1() != null) ingredients.append(cocktail.getStrIngredient1()).append(" - ").append(cocktail.getStrMeasure1()).append("\n");
            if (cocktail.getStrIngredient2() != null) ingredients.append(cocktail.getStrIngredient2()).append(" - ").append(cocktail.getStrMeasure2()).append("\n");
            if (cocktail.getStrIngredient3() != null) ingredients.append(cocktail.getStrIngredient3()).append(" - ").append(cocktail.getStrMeasure3()).append("\n");
            if (cocktail.getStrIngredient4() != null) ingredients.append(cocktail.getStrIngredient4()).append(" - ").append(cocktail.getStrMeasure4()).append("\n");
            if (cocktail.getStrIngredient5() != null) ingredients.append(cocktail.getStrIngredient5()).append(" - ").append(cocktail.getStrMeasure5()).append("\n");
            if (cocktail.getStrIngredient6() != null) ingredients.append(cocktail.getStrIngredient6()).append(" - ").append(cocktail.getStrMeasure6()).append("\n");
            ingredientsTextView.setText(ingredients.toString());

            // Load the cocktail image URL using AsyncTask
            new LoadImageTask(cocktailImageView).execute(cocktail.getStrDrinkThumb());
        }
    }

    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new URL(urlDisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }
}
