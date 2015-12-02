package at.android.gm.guessthemovie;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tempTextView;
        Typeface tempTypeface;


        tempTextView = (TextView) findViewById(R.id.about_title);
        tempTypeface = Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed-Regular.ttf");

        tempTextView.setTypeface(tempTypeface);

        tempTextView = (TextView) findViewById(R.id.about_version);
        tempTypeface = Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed-Light.ttf");

        tempTextView.setTypeface(tempTypeface);

        tempTextView = (TextView) findViewById(R.id.about_authors);
        tempTypeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Medium.ttf");

        tempTextView.setTypeface(tempTypeface);

        tempTextView = (TextView) findViewById(R.id.about_author1);
        tempTypeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");

        tempTextView.setTypeface(tempTypeface);

        tempTextView = (TextView) findViewById(R.id.about_author2);
        tempTypeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");

        tempTextView.setTypeface(tempTypeface);

        tempTextView = (TextView) findViewById(R.id.about_thx);
        tempTypeface = Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed-LightItalic.ttf");

        tempTextView.setTypeface(tempTypeface);
    }

}
