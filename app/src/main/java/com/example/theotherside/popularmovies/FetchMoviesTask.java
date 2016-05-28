package com.example.theotherside.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by theotherside on 5/27/16.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        String listType = params[0];
        String apiKey = "093bbf5811fc52b39f44f1c219d78f73";

        try {
            final String BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String QUERY = listType;
            final String KEY_PARAM = "api_key";

            String movieListStr = null;

            Uri uri = Uri.parse(BASE_URL + QUERY).buildUpon()
                    .appendQueryParameter(KEY_PARAM, apiKey)
                    .build();

            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            movieListStr = buffer.toString();

            Log.v(LOG_TAG, movieListStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error", e);
                }
            }
        }

        return null;
    }
}
