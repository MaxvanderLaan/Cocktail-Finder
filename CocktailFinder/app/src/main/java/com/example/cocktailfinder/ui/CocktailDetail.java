package com.example.cocktailfinder.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cocktailfinder.R;

public class CocktailDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cocktail_detail);

        String cocktailName = getIntent().getStringExtra("cocktail_name");

        TextView textView = findViewById(R.id.cocktail_name);
        textView.setText(cocktailName);
    }
}
