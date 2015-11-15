package at.android.gm.guessthemovie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.net.URL;

public class GuessActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap bitmap;
    private ProgressBar progressBar;
    private DownloadImageTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView)findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        showImage();
    }

    public void nextButtonClicked(View view) {
        bitmap.recycle();
        showImage();
    }

    public void showImage() {
        // create a new AsyncTask object
        task = new DownloadImageTask();
        // start asynctask
        String url = DataHandler.getInstance().getNextBackdropUrl();
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

}
