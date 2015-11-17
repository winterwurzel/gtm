package at.android.gm.guessthemovie;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        gridview.setVisibility(View.INVISIBLE);

        updateHearts(this);

        imageView = (ImageView)findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        showImage();


    }

    private void initButtons() {
        Movie movie = DataHandler.getInstance().getCurrentMovie();
        String rndmTitle = shuffle(movie.getTitle());

        gridview.setAdapter(new ButtonAdapter(this, rndmTitle));
    }

    public void nextButtonClicked(View view) {
        updateGame(this);
    }

    private void loadNext() {
        DataHandler.getInstance().removeCurrentMovie();
        bitmap.recycle();
        showImage();
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
            gridview.setVisibility(View.VISIBLE);
            textView.setText(getString(R.string.defaultText));
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
                myButton.setLayoutParams(new GridView.LayoutParams(150, 150));
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
                    if (textView.getText().equals(getString(R.string.defaultText)))
                        textView.setText(myButton.getText());
                    else
                        textView.setText((String) textView.getText() + myButton.getText());

                    if (DataHandler.getInstance().checkGuessedMovie((String) textView.getText())) {
                        textView.setText("nice guess!");
                        gridview.setVisibility(View.INVISIBLE);
                        loadNext();
                    } else if (textView.getText().length() > rndmTitle.length()) {
                        textView.setText("nope, maybe next time!");
                        updateGame(mContext);
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

    private void updateGame(final Context mContext) {
        gridview.setVisibility(View.INVISIBLE);
        if (DataHandler.getInstance().getLives() > 1) {
            DataHandler.getInstance().reduceLives();
            loadNext();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
            alertDialog.setTitle("Game Over!");
            alertDialog.setMessage("You lost all of your lives and therefor the game!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i = new Intent(mContext, MainActivity.class);
                            startActivity(i);
                        }
                    });
            alertDialog.show();
        }
        updateHearts(mContext);
    }
}
