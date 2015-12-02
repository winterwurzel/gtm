package at.android.gm.guessthemovie;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.Dictionary;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnFetchDataCompleted{

    private final int SHOW_PREFERENCES = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //material design fonts
        Typeface typefaceRobotoCondensedRegular = Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed-Regular.ttf");

        TextView appTitle = (TextView) findViewById(R.id.textView);
        appTitle.setTypeface(typefaceRobotoCondensedRegular);
        appTitle.setTextColor(Color.parseColor("#FFFFFF"));


        PreferenceManager.setDefaultValues(this, R.xml.application_preferences, false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DataHandler.getInstance().getLives() <= 0) {
            Button resumeButton = (Button) findViewById(R.id.buttonResume);
            resumeButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, SHOW_PREFERENCES);
                return true;
            case R.id.action_about:
                Intent i2 = new Intent(this, AboutActivity.class);
                startActivity(i2);
                return true;
        }
        return false;
    }

    public void startButtonClicked(View view) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String order = sharedPref.getString("list_preference_order", "");

        String options = "";
        if (order.equals("Popularity"))
            options = "&sort_by=popularity.desc";
        else if (order.equals("Rating"))
            options = "&sort_by=vote_average.desc&vote_count.gte=1000";

        String genre = sharedPref.getString("list_preference_genres", "");
        if (!genre.equals("Default"))
            options += "&with_genres=" + GenreMap.getInstance().getCONSTANT_MAP().get(genre);

        Log.e("wut", options);

        DataHandler dh = DataHandler.getInstance();
        ProgressDialog dialog = new ProgressDialog(this);
        dh.setDialog(dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dh.setAlertDialog(builder);

        dh.reInitMovieArray();
        dh.getData(this, options);
    }

    public void resumeButtonClicked(View view) {
        Intent i = new Intent(this, GuessActivity.class);
        startActivity(i);
    }

    @Override
    public void OnFetchDataCompleted() {
        Intent i = new Intent(this, GuessActivity.class);
        startActivity(i);
        Button resumeButton = (Button) findViewById(R.id.buttonResume);
        resumeButton.setVisibility(View.VISIBLE);
    }
}
