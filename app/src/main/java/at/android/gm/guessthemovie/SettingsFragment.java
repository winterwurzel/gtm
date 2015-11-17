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
    private final String KEY_PREF_LIST_ORDER= "list_preference_order";
    private final String KEY_PREF_LIST_GENRES= "list_preference_genres";

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
        Preference listPrefOrder = findPreference(KEY_PREF_LIST_ORDER);
        listPrefOrder.setSummary(sharedPreferences.getString(KEY_PREF_LIST_ORDER, ""));
        Preference listPrefGenre = findPreference(KEY_PREF_LIST_GENRES);
        listPrefGenre.setSummary(sharedPreferences.getString(KEY_PREF_LIST_GENRES, ""));
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
        if (key.equals(KEY_PREF_LIST_ORDER)) {
            Preference listPrefOrder = findPreference(key);
            listPrefOrder.setSummary(sharedPref.getString(key, ""));
        } else if (key.equals(KEY_PREF_LIST_GENRES)) {
            Preference listPrefGenres = findPreference(key);
            listPrefGenres.setSummary(sharedPref.getString(key, ""));
        }

    }
}
