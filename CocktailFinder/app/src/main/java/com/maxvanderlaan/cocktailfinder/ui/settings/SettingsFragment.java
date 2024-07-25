package com.maxvanderlaan.cocktailfinder.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.maxvanderlaan.cocktailfinder.R;

import java.io.File;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference wipeDataButton = findPreference("wipe_data");
        if (wipeDataButton != null) {
            wipeDataButton.setOnPreferenceClickListener(preference -> {
                wipeAllData();
                return true;
            });
        }
    }

    private void wipeAllData() {
        Context context = getActivity();
        if (context == null) return;

        // Wipe all SharedPreferences
        File sharedPrefsDir = new File(context.getFilesDir().getParentFile(), "shared_prefs");
        deleteRecursive(sharedPrefsDir);

        // Remove prepared images
        File preparedImagesDir = context.getDir("prepared_images", Context.MODE_PRIVATE);
        deleteRecursive(preparedImagesDir);

        // Remove cocktail data file
        File cocktailsFile = new File(context.getFilesDir(), "cocktails.json");
        if (cocktailsFile.exists()) {
            cocktailsFile.delete();
        }

        Toast.makeText(context, "All data wiped including preferences", Toast.LENGTH_SHORT).show();
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }
}
