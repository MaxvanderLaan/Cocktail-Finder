package com.maxvanderlaan.cocktailfinder.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.maxvanderlaan.cocktailfinder.MainActivity;
import com.maxvanderlaan.cocktailfinder.R;

import java.io.File;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference wipeDataButton = findPreference("wipe_data");
        if (wipeDataButton != null) {
            wipeDataButton.setOnPreferenceClickListener(preference -> {
                showWipeDataConfirmation();
                return true;
            });
        }
    }

    private void showWipeDataConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Confirm Wipe Data")
                .setMessage("Are you sure you want to wipe all data? This action cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> wipeAllData())
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Determine the text color based on the current theme
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        int positiveButtonColor = isDarkMode ? getResources().getColor(android.R.color.white) : getResources().getColor(android.R.color.black);
        int negativeButtonColor = positiveButtonColor;

        // Set the text color for the dialog buttons
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(positiveButtonColor);
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(negativeButtonColor);
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

        Toast.makeText(context, "All data wiped and restarting", Toast.LENGTH_SHORT).show();

        // Restart the app by clearing the task stack and reopening the main activity
        restartApp();
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    private void restartApp() {
        Context context = getActivity();
        if (context != null) {
            // This will restart the app by launching the launcher activity and clearing the task stack
            Intent restartIntent = new Intent(context, MainActivity.class);
            restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(restartIntent);
            // Close the app by ending the current process
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ("dark_mode".equals(key)) {
            // Send broadcast to MainActivity
            Intent intent = new Intent("com.maxvanderlaan.cocktailfinder.THEME_CHANGE");
            getContext().sendBroadcast(intent);

            // Recreate the activity to apply the new theme
            getActivity().recreate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
