package at.android.gm.guessthemovie;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * Created by georg on 15-Nov-15.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final String KEY_PREF_EDITTEXT= "edittext_preference";
    private final String KEY_PREF_LIST= "list_preference";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.application_preferences);
    }

    @Override
    public void onStart() {
        super.onStart();
        // register preference change listener
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        // show previously stored values
        Preference listPref = findPreference(KEY_PREF_LIST);
        listPref.setSummary(sharedPreferences.getString(KEY_PREF_LIST, ""));
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setBackgroundColor(Color.WHITE);
    }

    // change text or list values in PreferenceActivity ("Screen/Page")
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPref, String key) {
        Log.d(">>APPLICATION SETTINGS", "key=" + key);
        // Edit Text
        if (key.equals(KEY_PREF_EDITTEXT)) {
            Preference editTextPref = findPreference(key);
            editTextPref.setSummary(sharedPref.getString(key, ""));
            // list value
        } else if (key.equals(KEY_PREF_LIST)) {
            Preference listPref = findPreference(key);
            listPref.setSummary(sharedPref.getString(key, ""));
        }

    }
}
