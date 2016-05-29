package com.example.theotherside.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by theotherside on 5/27/16.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<Movie> movies = new ArrayList<>();

    public ImageAdapter(Context c, ArrayList<Movie> movies) {
        mContext = c;
        this.movies = movies;
    }

    public int getCount() {
        return movies.size();
    }

    public Object getItem(int position) {
        return movies.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(720, 900));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            imageView = (ImageView) convertView;
        }

        //Get image links from passed arraylist
        String imageLink = movies.get(position).getImage();

        Glide.with(mContext).load("http://image.tmdb.org/t/p/w500/"+imageLink).into(imageView);

        return imageView;
    }

}
