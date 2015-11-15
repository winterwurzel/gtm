package at.android.gm.guessthemovie;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by georg on 15-Nov-15.
 */
public class DataHandler {
    private static DataHandler ourInstance = new DataHandler();

    private JSONObject data;
    private ProgressDialog dialog;

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
            DataHandler.getInstance().setData(json);
            listener.OnFetchDataCompleted();
        }
    }

    public void setData(JSONObject data) {
        this.data = data;
        Log.e("data", data.toString());
    }

    public JSONObject getData() {
        return data;
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

    public void getData(String url, OnFetchDataCompleted ofdc){
        new FetchDataTask(ofdc).execute(url);
    }

}
