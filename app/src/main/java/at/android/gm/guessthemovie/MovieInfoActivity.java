package at.android.gm.guessthemovie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;

public class MovieInfoActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap bitmap;
    private ProgressBar progressBar;
    private DownloadImageTask task;
    private TextView textView;
    private TextView storyTV;
    private TextView ratingTV;
    private TextView releaseTV;
    private TextView correctTV;
    private Movie movie;
    private boolean gameOver;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface tempTypeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Medium.ttf");

        correctTV = (TextView) findViewById(R.id.correctTV);
        correctTV.setTypeface(tempTypeface);

        imageView = (ImageView)findViewById(R.id.imageView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        textView = (TextView) findViewById(R.id.correctTitle);
        tempTypeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
        textView.setTypeface(tempTypeface);

        storyTV = (TextView) findViewById(R.id.storyTV);
        tempTypeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-LightItalic.ttf");
        storyTV.setTypeface(tempTypeface);

        ratingTV = (TextView) findViewById(R.id.rating);
        tempTypeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");
        ratingTV.setTypeface(tempTypeface);

        releaseTV = (TextView) findViewById(R.id.release);
        releaseTV.setTypeface(tempTypeface);

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setEnabled(false);

        Intent i = getIntent();
        movie = (Movie) i.getSerializableExtra("movie");
        boolean correctAnswer = i.getBooleanExtra("correct", false);
        gameOver = i.getBooleanExtra("gameOver", false);
        TextView correctTV = (TextView) findViewById(R.id.correctTV);

        ratingTV.setText("rating: " + movie.getVote_average() + "/10");
        releaseTV.setText("(" + movie.getRelease_Date().split("-")[0] + ")");

        if (correctAnswer)
            correctTV.setText("Correct!");
        else
            correctTV.setText("Wrong! - You lost 1 live!");

        textView.setText(movie.getTitle());
        storyTV.setText(movie.getOverview());

        showImage();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void nextButtonClicked(View view) {
        imageView.setVisibility(View.INVISIBLE);
        ((BitmapDrawable) imageView.getDrawable()).getBitmap().recycle();
        if (gameOver) {
            Intent intent = new Intent(this,GameOverActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent i = new Intent(this, GuessActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void infoButtonClicked(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www.themoviedb.org/movie/" + movie.getId()));
        startActivity(i);
    }

    public void showImage() {
        // create a new AsyncTask object
        task = new DownloadImageTask();
        // start asynctask
        String url = "https://image.tmdb.org/t/p/w396" + movie.getPoster_path();
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
            try {
                imageUrl = new URL(urls[0]);
                InputStream in = imageUrl.openStream();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }
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
            nextButton.setEnabled(true);
        }
    }

}
