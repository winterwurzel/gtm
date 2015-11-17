package at.android.gm.guessthemovie;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by georg on 15-Nov-15.
 */
public class DataHandler implements OnFetchDataCompleted{
    @Override
    public void OnFetchDataCompleted() {
        nextPageReady = true;
    }

    private static DataHandler ourInstance = new DataHandler();

    private JSONArray results;
    private List<Movie> movieArray;
    private ProgressDialog dialog;
    private String dataUrl = "http://api.themoviedb.org/3/discover/movie?api_key=d395777e95507dd42bcaab7bb4f94266";
    private String backdropBaseUrl = "https://image.tmdb.org/t/p/original";
    private int page = 1;
    private boolean nextPageReady = true;
    private int lives = 3;

    public DataHandler() {
        movieArray = new ArrayList();
    }

    public static DataHandler getInstance() {
        return ourInstance;
    }

    class FetchDataTask extends AsyncTask<String, Void, JSONObject> {
        private OnFetchDataCompleted listener;

        public FetchDataTask(OnFetchDataCompleted listener) {
            this.listener = listener;
        }

        protected void onPreExecute() {
            DataHandler.getInstance().setDialogMessage("Fetching Data");
            DataHandler.getInstance().showDialog();
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            JSONObject json = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                json = new JSONObject(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
            return json;
        }

        protected void onPostExecute(JSONObject json) {
            DataHandler.getInstance().dismissDialog();
            try {
                results = json.getJSONArray("results");
                for (int i=0; i < results.length(); i++) {
                    JSONObject mv = results.getJSONObject(i);
                    if (mv.getString("backdrop_path") != null) {
                        List<Integer> ids = new ArrayList<Integer>();
                        String gids[] = mv.getString("genre_ids").substring(1, mv.getString("genre_ids").length()-1).split(",");
                        for (int j = 0; j < gids.length; j++) {
                            ids.add(Integer.parseInt(gids[j]));
                            Log.e("id", mv.getString("title") + ", " + ids.get(j));
                        }
                        movieArray.add(new Movie(mv.getBoolean("adult"), mv.getString("backdrop_path"), ids, mv.getInt("id"), mv.getString("original_language"),
                                mv.getString("original_title"), mv.getString("overview"), mv.getString("release_date"), mv.getString("poster_path"), mv.getDouble("popularity"),
                                mv.getString("title"), mv.getDouble("vote_average")));
                    }
                }
                long seed = System.nanoTime();
                Collections.shuffle(movieArray, new Random(seed));
            } catch (JSONException e) {
                Log.e("JSON", "Error getting data.");
            }
            listener.OnFetchDataCompleted();
        }
    }

    public String getNextBackdropUrl() {
        if (nextPageReady) {
            Movie nextMovie = (Movie) this.movieArray.get(0);
            return backdropBaseUrl + nextMovie.getBackdrop_path();
        } else
            return null;
    }

    public void removeCurrentMovie() {
        movieArray.remove(0);
        if (movieArray.isEmpty() == true) {
            page++;
            getData(this, null);
            nextPageReady = false;
        }
    }

    public boolean checkGuessedMovie(String textViewString) {
        String movieTitle = this.getCurrentMovie().getTitle();
        if (movieTitle.equals(textViewString))
            return true;
        else
            return false;
    }

    public Movie getCurrentMovie() {
        return (Movie) movieArray.get(0);
    }

    public void setDialogMessage(String msg) {
        this.dialog.setMessage(msg);
    }

    public void showDialog() {
        this.dialog.show();
    }

    public void dismissDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    public void setDialog(ProgressDialog dialog) {
        this.dialog = dialog;
    }

    public void reInitMovieArray() {
        this.movieArray = new ArrayList();
        page = 1;
        nextPageReady = true;
        lives = 3;
    }

    public int getLives() {
        return lives;
    }

    public void reduceLives() {
        lives--;
    }

    public void getData(OnFetchDataCompleted ofdc, String options){
        new FetchDataTask(ofdc).execute(dataUrl + "&page=" + page + options);
    }

}
