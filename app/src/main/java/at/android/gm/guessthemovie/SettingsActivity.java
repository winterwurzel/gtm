package at.android.gm.guessthemovie;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by georg on 15-Nov-15.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // open a fragment to settings
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
