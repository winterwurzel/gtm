package at.android.gm.guessthemovie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements OnFetchDataCompleted{

    private final int SHOW_PREFERENCES = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (DataHandler.getInstance().getLives() == 0) {
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
        }
        return false;
    }

    public void startButtonClicked(View view) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String order = sharedPref.getString("list_preference", "");
        String options = null;
        if (order.equals("popularity"))
            options = "&sort_by=popularity.desc";
        else if (order.equals("rating"))
            options = "&sort_by=vote_average.desc&vote_count.gte=100";

        DataHandler dh = DataHandler.getInstance();
        ProgressDialog dialog = new ProgressDialog(this);
        dh.setDialog(dialog);
        dh.reInitMovieArray();
        dh.getData(this, options);
        Button resumeButton = (Button) findViewById(R.id.buttonResume);
        resumeButton.setVisibility(View.VISIBLE);
    }

    public void resumeButtonClicked(View view) {
        Intent i = new Intent(this, GuessActivity.class);
        startActivity(i);
    }

    @Override
    public void OnFetchDataCompleted() {
        Log.e("test", "FERTIG!");
        Intent i = new Intent(this, GuessActivity.class);
        startActivity(i);
    }
}
