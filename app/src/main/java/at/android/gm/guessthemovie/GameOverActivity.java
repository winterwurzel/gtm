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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity implements OnFetchDataCompleted{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        TextView gameOverText = (TextView) findViewById(R.id.gameOverText);
        int count = DataHandler.getInstance().getCount();
        gameOverText.setText("Your Score: " + (count - 4 + DataHandler.getInstance().getLives()) + "/" + count);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    public void menuButtonClicked(View view) {
        onBackPressed();
    }

    public void newGameButtonClicked(View view) {
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

        DataHandler dh = DataHandler.getInstance();
        ProgressDialog dialog = new ProgressDialog(this);
        dh.setDialog(dialog);
        dh.reInitMovieArray();
        dh.getData(this, options);
    }

    @Override
    public void OnFetchDataCompleted() {
        Intent i = new Intent(this, GuessActivity.class);
        startActivity(i);
    }
}
