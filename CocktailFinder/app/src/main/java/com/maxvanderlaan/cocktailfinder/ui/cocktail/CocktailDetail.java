package com.maxvanderlaan.cocktailfinder.ui.cocktail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.maxvanderlaan.cocktailfinder.R;
import com.maxvanderlaan.cocktailfinder.model.Cocktail;

import java.io.InputStream;
import java.net.URL;

public class CocktailDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Retrieve the dark mode preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);

        // Apply the theme based on the preference
        if (isDarkMode) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme_Light);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cocktail_detail);

        Cocktail cocktail = getIntent().getParcelableExtra("cocktail");

        TextView nameTextView = findViewById(R.id.cocktail_name);
        TextView tagsTextView = findViewById(R.id.cocktail_tags);
        TextView glassTextView = findViewById(R.id.cocktail_glass);
        TextView instructionsTextView = findViewById(R.id.cocktail_instructions);
        TextView ingredientsTextView = findViewById(R.id.cocktail_ingredients);
        TextView instructionsLabel = findViewById(R.id.instructions_label);
        TextView ingredientsLabel = findViewById(R.id.ingredients_label);
        ImageView cocktailImageView = findViewById(R.id.cocktail_image);

        if (cocktail != null) {
            setTextView(nameTextView, cocktail.getStrDrink());

            //set and null check tags.
            String formattedTags = formatTags(cocktail.getStrTags());
            setTextView(tagsTextView, "Tags: " + formattedTags);
            if (formattedTags == null || formattedTags.isEmpty() || formattedTags.equals("null")) {
                tagsTextView.setVisibility(View.GONE);
            } else {
                setTextView(tagsTextView, "Tags: " + formattedTags);
                tagsTextView.setVisibility(View.VISIBLE);
            }

            setTextView(glassTextView, cocktail.getStrGlass());
            setTextView(instructionsTextView, cocktail.getStrInstructions());

            if (cocktail.getStrInstructions() == null || cocktail.getStrInstructions().isEmpty()) {
                instructionsLabel.setVisibility(TextView.GONE);
            } else {
                instructionsLabel.setVisibility(TextView.VISIBLE);
            }

            StringBuilder ingredients = new StringBuilder();
            int ingredientNumber = 1;
            ingredientNumber = appendIngredient(ingredients, cocktail.getStrIngredient1(), cocktail.getStrMeasure1(), ingredientNumber);
            ingredientNumber = appendIngredient(ingredients, cocktail.getStrIngredient2(), cocktail.getStrMeasure2(), ingredientNumber);
            ingredientNumber = appendIngredient(ingredients, cocktail.getStrIngredient3(), cocktail.getStrMeasure3(), ingredientNumber);
            ingredientNumber = appendIngredient(ingredients, cocktail.getStrIngredient4(), cocktail.getStrMeasure4(), ingredientNumber);
            ingredientNumber = appendIngredient(ingredients, cocktail.getStrIngredient5(), cocktail.getStrMeasure5(), ingredientNumber);
            ingredientNumber = appendIngredient(ingredients, cocktail.getStrIngredient6(), cocktail.getStrMeasure6(), ingredientNumber);
            ingredientNumber = appendIngredient(ingredients, cocktail.getStrIngredient7(), cocktail.getStrMeasure7(), ingredientNumber);
            ingredientNumber = appendIngredient(ingredients, cocktail.getStrIngredient8(), cocktail.getStrMeasure8(), ingredientNumber);
            ingredientNumber = appendIngredient(ingredients, cocktail.getStrIngredient9(), cocktail.getStrMeasure9(), ingredientNumber);
            ingredientNumber = appendIngredient(ingredients, cocktail.getStrIngredient10(), cocktail.getStrMeasure10(), ingredientNumber);
            ingredientNumber = appendIngredient(ingredients, cocktail.getStrIngredient11(), cocktail.getStrMeasure11(), ingredientNumber);
            ingredientNumber = appendIngredient(ingredients, cocktail.getStrIngredient12(), cocktail.getStrMeasure12(), ingredientNumber);

            if (ingredients.length() == 0) {
                ingredientsLabel.setVisibility(TextView.GONE);
                ingredientsTextView.setVisibility(TextView.GONE);
            } else {
                ingredientsLabel.setVisibility(TextView.VISIBLE);
                ingredientsTextView.setText(ingredients.toString());
            }

            // Load the cocktail image URL using AsyncTask
            new LoadImageTask(cocktailImageView).execute(cocktail.getStrDrinkThumb());

            // Set up the button click event after cocktail details are loaded
            Button prepareButton = findViewById(R.id.prepare_button);
            prepareButton.setOnClickListener(v -> {
                Intent intent = new Intent(CocktailDetail.this, PrepareFormActivity.class);
                intent.putExtra("cocktail_name", cocktail.getStrDrink());
                startActivity(intent);
            });
        }
    }

    private void setTextView(TextView textView, String text) {
        if (text == null || text.isEmpty()) {
            textView.setVisibility(TextView.GONE);
        } else {
            textView.setText(text);
        }
    }

    private int appendIngredient(StringBuilder ingredients, String ingredient, String measure, int number) {
        if (ingredient != null && !ingredient.isEmpty() && !ingredient.equals("null")) {
            ingredients.append(number).append(". ").append(ingredient);
            if (measure != null && !measure.isEmpty() && !measure.equals("null")) {
                ingredients.append(" - ").append(measure);
            }
            ingredients.append("\n");
            number++;
        }
        return number;
    }

    private String formatTags(String tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        return tags.replace(",", ", ");
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
