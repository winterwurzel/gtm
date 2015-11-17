package at.android.gm.guessthemovie;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GuessActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap bitmap;
    private ProgressBar progressBar;
    private DownloadImageTask task;
    private GridView gridview;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = (TextView) findViewById(R.id.textView2);
        gridview = (GridView) findViewById(R.id.buttonLayout);

        imageView = (ImageView)findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        showImage();


    }

    private void initButtons() {
        Movie movie = DataHandler.getInstance().getCurrentMovie();
        String rndmTitle = shuffle(movie.getTitle());

        Log.e("test", rndmTitle + ", " + rndmTitle.length() + ", " + movie.getTitle());

        gridview.setAdapter(new ButtonAdapter(this, rndmTitle, textView));

    }

    public void nextButtonClicked(View view) {
        bitmap.recycle();
        showImage();
    }

    public void showImage() {
        initButtons();
        // create a new AsyncTask object
        task = new DownloadImageTask();
        // start asynctask
        String url = DataHandler.getInstance().getNextBackdropUrl();
        DataHandler.getInstance().removeCurrentMovie();
        if(url != null)
            task.execute(url);
    }

    private class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {

        // this is done in UI thread, nothing this time
        @Override
        protected void onPreExecute() {
            // show loading progress bar
            progressBar.setVisibility(View.VISIBLE);
        }

        // this is background thread, load image and pass it to onPostExecute
        @Override
        protected Bitmap doInBackground(String... urls) {
            URL imageUrl;
            bitmap = null;
            try {
                imageUrl = new URL(urls[0]);
                InputStream in = imageUrl.openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("<<LOADIMAGE>>", e.getMessage());
            }
            return bitmap;
        }

        // this is done in UI thread
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            // hide loading progress bar
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private String shuffle(String input){
        List<Character> characters = new ArrayList<Character>();

        for(char c:input.toCharArray()){
            characters.add(c);
        }

        StringBuilder output = new StringBuilder(input.length());

        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }

        return output.toString();
    }



}
