package com.example.cocktailfinder.ui.search;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cocktailfinder.model.Cocktail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<List<Cocktail>> cocktailList;

    public SearchViewModel() {
        cocktailList = new MutableLiveData<>();
    }

    public void loadCocktailNames(Context context, String query) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Cocktail> cocktails = new ArrayList<>();
                try {
                    JSONArray drinks = response.getJSONArray("drinks");
                    for (int i = 0; i < drinks.length(); i++) {
                        JSONObject drink = drinks.getJSONObject(i);
                        Cocktail cocktail = new Cocktail(
                                drink.getString("idDrink"),
                                drink.getString("strDrink"),
                                drink.optString("strTags"),
                                drink.getString("strCategory"),
                                drink.getString("strAlcoholic"),
                                drink.getString("strGlass"),
                                drink.getString("strInstructions"),
                                drink.optString("strIngredient1"),
                                drink.optString("strIngredient2"),
                                drink.optString("strIngredient3"),
                                drink.optString("strIngredient4"),
                                drink.optString("strIngredient5"),
                                drink.optString("strIngredient6"),
                                drink.optString("strIngredient7"),
                                drink.optString("strIngredient8"),
                                drink.optString("strIngredient9"),
                                drink.optString("strIngredient10"),
                                drink.optString("strIngredient11"),
                                drink.optString("strIngredient12"),
                                drink.optString("strMeasure1"),
                                drink.optString("strMeasure2"),
                                drink.optString("strMeasure3"),
                                drink.optString("strMeasure4"),
                                drink.optString("strMeasure5"),
                                drink.optString("strMeasure6"),
                                drink.optString("strMeasure7"),
                                drink.optString("strMeasure8"),
                                drink.optString("strMeasure9"),
                                drink.optString("strMeasure10"),
                                drink.optString("strMeasure11"),
                                drink.optString("strMeasure12"),
                                drink.getString("strDrinkThumb")
                        );
                        cocktails.add(cocktail);
                    }
                } catch (Exception e) {
                    Log.e("VolleyResponse", "Error parsing JSON", e);
                }
                cocktailList.setValue(cocktails);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", "Error occurred", error);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public LiveData<List<Cocktail>> getCocktailList() {
        return cocktailList;
    }
}
