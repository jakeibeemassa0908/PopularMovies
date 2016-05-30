package com.example.theotherside.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
public class MovieListFragment extends Fragment {

    private final String LOG_TAG = MovieListFragment.class.getSimpleName();
    public  static final String EXTRA_MOVIES = "movies";

    ImageAdapter mImageAdapter;
    GridView moviesGrid;

    public MovieListFragment(){
     setHasOptionsMenu(true);

    }

    @Override
    public void onStart() {
        super.onStart();

        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected)
            updateMovies();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movielist,container,false);
        moviesGrid = (GridView) rootView.findViewById(R.id.movies_grid);
        moviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //retrieve adapter
                ImageAdapter adapter =(ImageAdapter) parent.getAdapter();

                Movie movie=(Movie)adapter.getItem(position);

                //Start detail activity
                //Toast.makeText(getActivity(),movie.getReleaseDate(),Toast.LENGTH_SHORT).show();
                Intent movieDetailIntent = new Intent(getActivity(),MovieDetailActivity.class);
                movieDetailIntent.putExtra(EXTRA_MOVIES,movie);
                startActivity(movieDetailIntent);

            }
        });
        return rootView;

    }

    private void updateMovies(){
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        //get shared preference
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String location = prefs.getString(getString(R.string.pref_movies_key),getString(R.string.pref_movies_rating));

        moviesTask.execute(location);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            ArrayList<Movie> movies = null;


            String listType = params[0];

            /**
             * TODO Insert your own API KEY
             */
            String apiKey = "";

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

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            if(arrayList!=null) {
                mImageAdapter = new ImageAdapter(getActivity(), arrayList);
                moviesGrid.setAdapter(mImageAdapter);
            }
        }

        private ArrayList<Movie> getMovieDataFromJson(String moviesJsonStr) throws JSONException {
            ArrayList<Movie> movies = new ArrayList<>();

            //name of the JSON objects that needs to be extracted
            final String RESULTS="results";
            final String TITLE = "title";
            final String IMAGE ="poster_path";
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

                movies.add(movieData);

            }
            return movies;
        }
    }


}
