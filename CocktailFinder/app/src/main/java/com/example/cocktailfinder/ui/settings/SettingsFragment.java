package com.example.cocktailfinder.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.cocktailfinder.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("dark_mode")) {
            // Send a broadcast to notify MainActivity of the theme change
            Intent intent = new Intent("com.example.cocktailfinder.THEME_CHANGE");
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

            // Also recreate the settings activity to apply the new theme
            getActivity().recreate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register the preference change listener
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the preference change listener
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }
}
