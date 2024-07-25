package com.maxvanderlaan.cocktailfinder.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.maxvanderlaan.cocktailfinder.model.Prepared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PreparedRepository {
    private static final String PREFS_NAME = "prepared_prefs";
    private static final String KEY_PREPARED_LIST = "prepared_list";

    private final SharedPreferences sharedPreferences;

    public PreparedRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void savePrepared(Prepared prepared) {
        List<Prepared> preparedList = getPreparedList();
        preparedList.add(prepared);
        String json = serializePreparedList(preparedList);
        sharedPreferences.edit().putString(KEY_PREPARED_LIST, json).apply();
    }

    public List<Prepared> getPreparedList() {
        String json = sharedPreferences.getString(KEY_PREPARED_LIST, null);
        if (json == null) {
            return new ArrayList<>();
        }
        return deserializePreparedList(json);
    }

    private String serializePreparedList(List<Prepared> preparedList) {
        JSONArray jsonArray = new JSONArray();
        for (Prepared prepared : preparedList) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cocktailName", prepared.getCocktailName());
                jsonObject.put("rating", prepared.getRating());
                jsonObject.put("imagePath", prepared.getImagePath());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    private List<Prepared> deserializePreparedList(String json) {
        List<Prepared> preparedList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String cocktailName = jsonObject.getString("cocktailName");
                float rating = (float) jsonObject.getDouble("rating");
                String imagePath = jsonObject.getString("imagePath");
                preparedList.add(new Prepared(cocktailName, rating, imagePath));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return preparedList;
    }
}
