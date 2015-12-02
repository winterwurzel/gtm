package at.android.gm.guessthemovie;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GuessActivity extends AppCompatActivity {

    private ImageView imageView;
    //private Bitmap bitmap;
    private ProgressBar progressBar;
    private DownloadImageTask task;
    private GridView gridview;
    private TextView textView;
    private TextView guessTV;
    private Button nextButton;

//    private Typeface typefaceDefaultText = Typeface.createFromAsset(this.getAssets(),"fonts/RobotoCondensed-LightItalic.ttf");
//    private Typeface typefaceTitleText = Typeface.createFromAsset(this.getAssets(),"fonts/RobotoCondensed-Bold.ttf");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (DataHandler.getInstance().getMovieArraySize() == 0) {
            Intent intent = new Intent(this,GameOverActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            textView = (TextView) findViewById(R.id.guessTextView);

            //Change font
            Typeface typefaceDefaultText = Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed-LightItalic.ttf");
            textView.setTypeface(typefaceDefaultText);

            gridview = (GridView) findViewById(R.id.buttonLayout);
            gridview.setVisibility(View.INVISIBLE);

            nextButton = (Button) findViewById(R.id.button2);
            nextButton.setEnabled(false);

            updateHearts(this);

            imageView = (ImageView) findViewById(R.id.imageView);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            showImage();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    public void resetButtonClicked(View view) {
        guessTV = (TextView) findViewById(R.id.guessTextView);
        guessTV.setText(R.string.defaultText);

        //Change font
        Typeface typefaceDefaultText = Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed-LightItalic.ttf");
        guessTV.setTypeface(typefaceDefaultText);

        initButtons();
    }

    private void initButtons() {
        Movie movie = DataHandler.getInstance().getCurrentMovie();
        String rndmTitle = shuffle(movie.getTitle());

        gridview.setAdapter(new ButtonAdapter(this, rndmTitle));
    }

    public void nextButtonClicked(View view) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean conf = sharedPref.getBoolean("confirmation", false);
        if (!conf) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?")
                    .setMessage("You will lose one live!")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateGame();
                        }
                    })
                    .setNegativeButton("NO", null)                        //Do nothing on no
                    .show();
        }
        else
            updateGame();
    }

    private void loadNext(boolean correctAnswer, boolean gameOver) {
        Movie movie = DataHandler.getInstance().getCurrentMovie();
        Intent i = new Intent(this, MovieInfoActivity.class);
        //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.putExtra("movie", movie);
        i.putExtra("correct", correctAnswer);
        i.putExtra("gameOver", gameOver);
        DataHandler.getInstance().removeCurrentMovie();
        //showImage();
        imageView.setVisibility(View.INVISIBLE);
        ((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
        startActivity(i);
        finish();
    }

    public void showImage() {
        initButtons();
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
            Bitmap bitmap = null;
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
            gridview.setVisibility(View.VISIBLE);
            textView.setText(getString(R.string.defaultText));
            nextButton.setEnabled(true);
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


    class ButtonAdapter extends BaseAdapter {
        private Context mContext;
        private String rndmTitle;

        public ButtonAdapter(Context c, String rndmTitle) {
            mContext = c;
            this.rndmTitle = rndmTitle;
        }

        @Override
        public int getCount() {
            return rndmTitle.length();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Button myButton;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                myButton = new Button(mContext);
                myButton.setLayoutParams(new GridView.LayoutParams(75, 75));
                myButton.setTransformationMethod(null);
                myButton.setPadding(8, 8, 8, 8);
            } else {
                myButton = (Button) convertView;
            }

            myButton.setText(rndmTitle.substring(position, position+1));
            myButton.setId(position);

            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Change font
                    Typeface typefaceTitleText = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Medium.ttf");
                    textView.setTypeface(typefaceTitleText);

                    if (textView.getText().equals(getString(R.string.defaultText)))
                        textView.setText(myButton.getText());
                    else
                        textView.setText((String) textView.getText() + myButton.getText());

                    myButton.setVisibility(View.INVISIBLE);

                    if (DataHandler.getInstance().checkGuessedMovie((String) textView.getText())) {
                        //textView.setText("nice guess!");
                        gridview.setVisibility(View.INVISIBLE);
                        loadNext(true, false);
                    } else if (textView.getText().length() >= rndmTitle.length()) {
                        //textView.setText("nope, maybe next time!");
                        updateGame();
                    }
                }
            });

            return myButton;
        }
    }

    private void updateHearts(Context mContext) {
        switch(DataHandler.getInstance().getLives()) {
            case 0:
                ImageView iv1 = (ImageView) findViewById(R.id.imageView1);
                iv1.setVisibility(View.INVISIBLE);
            case 1:
                ImageView iv2 = (ImageView) findViewById(R.id.imageView2);
                iv2.setVisibility(View.INVISIBLE);
            case 2:
                ImageView iv3 = (ImageView) findViewById(R.id.imageView3);
                iv3.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    private void updateGame() {
        gridview.setVisibility(View.INVISIBLE);
        DataHandler.getInstance().reduceLives();
        if (DataHandler.getInstance().getLives() > 0) {
            loadNext(false, false);
        } else {
            loadNext(false, true);
        }
    }

    public void toastLives(View view) {
        Toast.makeText(this, "You have " + DataHandler.getInstance().getLives() + " lives left!", Toast.LENGTH_SHORT).show();
    }
}
