package com.example.theotherside.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by theotherside on 5/28/16.
 */
public class MovieDetailFragment extends Fragment {

    private Movie mMovie;
    @BindView(R.id.movie_title) TextView titleTextview;
    @BindView(R.id.movie_overview) TextView overviewTextview;
    @BindView(R.id.movie_rating) TextView ratingTextview;
    @BindView(R.id.movie_release) TextView releaseTextview;
    @BindView(R.id.movie_image) ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail,container,false);

        ButterKnife.bind(this,rootView);

        Intent detailIntent = getActivity().getIntent();
        mMovie = detailIntent.getParcelableExtra(MovieListFragment.EXTRA_MOVIES);


        titleTextview.setText(mMovie.getTitle());
        overviewTextview.setText(mMovie.getOverview());
        ratingTextview.setText(mMovie.getRating()+ " /10");

        //get the year only from release date
        releaseTextview.setText(mMovie.getReleaseDate().split("-")[0]);

        Glide.with(getActivity()).load("http://image.tmdb.org/t/p/w500/"+mMovie.getImage()).into(imageView);

        return rootView;
    }
}
