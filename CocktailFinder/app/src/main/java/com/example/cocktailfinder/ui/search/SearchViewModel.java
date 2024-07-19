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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<List<String>> cocktailNames;
    private String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita";

    public SearchViewModel() {
        cocktailNames = new MutableLiveData<>();
    }

    public void loadCocktailNames(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<String> names = new ArrayList<>();
                try {
                    JSONArray drinks = response.getJSONArray("drinks");
                    for (int i = 0; i < drinks.length(); i++) {
                        JSONObject drink = drinks.getJSONObject(i);
                        String drinkName = drink.getString("strDrink");
                        names.add(drinkName);
                    }
                } catch (Exception e) {
                    Log.e("VolleyResponse", "Error parsing JSON", e);
                }
                cocktailNames.setValue(names);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", "Error occurred", error);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public LiveData<List<String>> getCocktailNames() {
        return cocktailNames;
    }
}
