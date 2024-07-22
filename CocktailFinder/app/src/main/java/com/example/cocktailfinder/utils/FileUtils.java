package com.example.cocktailfinder.utils;

import android.content.Context;
import android.util.Log;

import com.example.cocktailfinder.model.Cocktail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    private static final String FILE_NAME = "cocktails.json";

    public static void saveCocktailToFile(Context context, Cocktail cocktail) {
        FileWriter fileWriter = null;
        FileReader fileReader = null;
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            JSONArray jsonArray = new JSONArray();

            // If the file exists, read its current content
            if (file.exists()) {
                fileReader = new FileReader(file);
                int content;
                StringBuilder builder = new StringBuilder();
                while ((content = fileReader.read()) != -1) {
                    builder.append((char) content);
                }
                if (builder.length() > 0) {
                    jsonArray = new JSONArray(builder.toString());
                }
                fileReader.close();
            }

            // Add the new cocktail to the JSON array
            JSONObject cocktailJsonObject = new JSONObject();
            cocktailJsonObject.put("idDrink", cocktail.getIdDrink());
            cocktailJsonObject.put("strDrink", cocktail.getStrDrink());
            cocktailJsonObject.put("strTags", cocktail.getStrTags());
            cocktailJsonObject.put("strCategory", cocktail.getStrCategory());
            cocktailJsonObject.put("strAlcoholic", cocktail.getStrAlcoholic());
            cocktailJsonObject.put("strGlass", cocktail.getStrGlass());
            cocktailJsonObject.put("strInstructions", cocktail.getStrInstructions());
            cocktailJsonObject.put("strIngredient1", cocktail.getStrIngredient1());
            cocktailJsonObject.put("strIngredient2", cocktail.getStrIngredient2());
            cocktailJsonObject.put("strIngredient3", cocktail.getStrIngredient3());
            cocktailJsonObject.put("strIngredient4", cocktail.getStrIngredient4());
            cocktailJsonObject.put("strIngredient5", cocktail.getStrIngredient5());
            cocktailJsonObject.put("strIngredient6", cocktail.getStrIngredient6());
            cocktailJsonObject.put("strIngredient7", cocktail.getStrIngredient7());
            cocktailJsonObject.put("strIngredient8", cocktail.getStrIngredient8());
            cocktailJsonObject.put("strIngredient9", cocktail.getStrIngredient9());
            cocktailJsonObject.put("strIngredient10", cocktail.getStrIngredient10());
            cocktailJsonObject.put("strIngredient11", cocktail.getStrIngredient11());
            cocktailJsonObject.put("strIngredient12", cocktail.getStrIngredient12());
            cocktailJsonObject.put("strMeasure1", cocktail.getStrMeasure1());
            cocktailJsonObject.put("strMeasure2", cocktail.getStrMeasure2());
            cocktailJsonObject.put("strMeasure3", cocktail.getStrMeasure3());
            cocktailJsonObject.put("strMeasure4", cocktail.getStrMeasure4());
            cocktailJsonObject.put("strMeasure5", cocktail.getStrMeasure5());
            cocktailJsonObject.put("strMeasure6", cocktail.getStrMeasure6());
            cocktailJsonObject.put("strMeasure7", cocktail.getStrMeasure7());
            cocktailJsonObject.put("strMeasure8", cocktail.getStrMeasure8());
            cocktailJsonObject.put("strMeasure9", cocktail.getStrMeasure9());
            cocktailJsonObject.put("strMeasure10", cocktail.getStrMeasure10());
            cocktailJsonObject.put("strMeasure11", cocktail.getStrMeasure11());
            cocktailJsonObject.put("strMeasure12", cocktail.getStrMeasure12());
            cocktailJsonObject.put("strDrinkThumb", cocktail.getStrDrinkThumb());
            jsonArray.put(cocktailJsonObject);

            // Write the updated JSON array to the file
            fileWriter = new FileWriter(file);
            fileWriter.write(jsonArray.toString());
            Log.d("FileUtils", "Cocktail data saved.");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FileUtils", "Error saving cocktail data.", e);
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
