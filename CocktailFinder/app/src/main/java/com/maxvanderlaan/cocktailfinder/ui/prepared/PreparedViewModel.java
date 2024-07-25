package com.maxvanderlaan.cocktailfinder.ui.prepared;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.maxvanderlaan.cocktailfinder.model.Prepared;

import java.util.ArrayList;
import java.util.List;

public class PreparedViewModel extends AndroidViewModel {

    private MutableLiveData<List<Prepared>> preparedList;

    public PreparedViewModel(@NonNull Application application) {
        super(application);
        preparedList = new MutableLiveData<>();
        loadPreparedData();
    }

    public LiveData<List<Prepared>> getPreparedList() {
        return preparedList;
    }

    private void loadPreparedData() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("prepared_prefs", Context.MODE_PRIVATE);
        String preparedData = sharedPreferences.getString("prepared_list", "");

        List<Prepared> list = new ArrayList<>();
        if (!preparedData.isEmpty()) {
            String[] preparedEntries = preparedData.split(";");
            for (String entry : preparedEntries) {
                Prepared prepared = deserializePrepared(entry);
                if (prepared != null) {
                    list.add(prepared);
                }
            }
        }
        preparedList.setValue(list);
    }

    private Prepared deserializePrepared(String data) {
        String[] parts = data.split(",");
        if (parts.length == 3) {
            String cocktailName = parts[0];
            float rating = 0;
            try {
                rating = Float.parseFloat(parts[1].trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            String imagePath = parts[2];
            return new Prepared(cocktailName, rating, imagePath);
        }
        return null;
    }
}
