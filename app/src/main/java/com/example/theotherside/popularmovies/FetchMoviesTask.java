package com.example.theotherside.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by theotherside on 5/27/16.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        ArrayList<Movie> movies = null;


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
            movies =getMovieDataFromJson(movieListStr);

            Log.v(LOG_TAG, movieListStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
        } catch (JSONException e) {
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

        return movies;
    }

    private ArrayList<Movie> getMovieDataFromJson(String moviesJsonStr) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();

        //name of the JSON objects that needs to be extracted
        final String RESULTS="results";
        final String TITLE = "original_title";
        final String IMAGE ="backdrop_path";
        final String OVERVIEW ="overview";
        final String RATING = "vote_average";
        final String RELEASE_DATE ="release_date";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray movieArray = moviesJson.getJSONArray(RESULTS);

        for (int i =0;i <movieArray.length();i++){


            JSONObject movie = movieArray.getJSONObject(i);

            Movie movieData = new Movie();

            movieData.setTitle(movie.getString(TITLE));
            movieData.setImage(movie.getString(IMAGE));
            movieData.setOverview(movie.getString(OVERVIEW));
            movieData.setRating(movie.getString(RATING));
            movieData.setReleaseDate(movie.getString(RELEASE_DATE));


            Log.v(LOG_TAG,movieData.getTitle());

        }
        return movies;
    }
}
