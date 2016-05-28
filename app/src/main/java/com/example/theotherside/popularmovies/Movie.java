package com.example.theotherside.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by theotherside on 5/27/16.
 */
public class Movie implements Parcelable {

    private String title,image,overview,rating,releaseDate;

    public Movie(){

    }

    public Movie(String title, String image, String overview,String rating, String releaseDate){
        this.title = title;
        this.image = image;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public Movie(Parcel in){
        title = in.readString();
        image = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        rating = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(rating);

    }
}
