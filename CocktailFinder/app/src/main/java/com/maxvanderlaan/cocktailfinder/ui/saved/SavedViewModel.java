package com.maxvanderlaan.cocktailfinder.ui.saved;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.maxvanderlaan.cocktailfinder.model.Cocktail;
import com.maxvanderlaan.cocktailfinder.utils.FileUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SavedViewModel extends ViewModel {

    private final MutableLiveData<List<Cocktail>> cocktailList;

    public SavedViewModel() {
        cocktailList = new MutableLiveData<>();
    }

    public LiveData<List<Cocktail>> getCocktailList() {
        return cocktailList;
    }

    public void loadSavedCocktails(Context context) {
        List<Cocktail> cocktails = FileUtils.readCocktailsFromFile(context);

        Collections.sort(cocktails, new Comparator<Cocktail>() {
            @Override
            public int compare(Cocktail c1, Cocktail c2) {
                return c1.getStrDrink().compareToIgnoreCase(c2.getStrDrink());
            }
        });

        cocktailList.setValue(cocktails);
    }
}
