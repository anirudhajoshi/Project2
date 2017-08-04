package com.example.aniru.popmovies1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aniru on 7/17/2017.
 */

public class MovieDetails implements Parcelable{
    public static final Parcelable.Creator<MovieDetails> CREATOR =
            new Parcelable.Creator<MovieDetails>() {
                public MovieDetails createFromParcel(Parcel source) {
                    return new MovieDetails(source);
                }

                public MovieDetails[] newArray(int size) {
                    return new MovieDetails[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //write
        dest.writeString(String.valueOf(getMovieID()));
        dest.writeString(getTitle());
        dest.writeString(String.valueOf(getVote_average()));
        dest.writeString(getReleaseDate());
        dest.writeString(getPlot_synopsis());
    }

    protected MovieDetails(Parcel in) {
        //retrieve
        String id = in.readString();
        setMovieID( Integer.valueOf(id) );

        String title = in.readString();
        setTitle( title );

        String voteAverage = in.readString();
        setVote_average( Double.valueOf(voteAverage) );

        String releaseDate = in.readString();
        setReleaseDate(releaseDate);

        String getOverview = in.readString();
        setPlot_synopsis( getOverview );
    }

    MovieDetails(){}

    @Override
    public String toString() {
        return getClass().getName() + '@' + "\nTitle: " + getTitle() + "\nPoster: " + getMovie_poster()
                + "\nVote Average: " + String.valueOf(getVote_average()) + "\nSynopsis: " + getPlot_synopsis();
    }

    public String getTitle() {
        return title;
    }

    public String getMovie_poster() {
        return movie_poster;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getPlot_synopsis() {
        return plot_synopsis;
    }

    // title, release date, movie poster, vote average, and plot synopsis
    public void setTitle(String title) {
        this.title = title;
    }

    public void setMovie_poster(String movie_poster) {
        this.movie_poster = movie_poster;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public void setPlot_synopsis(String plot_synopsis) {
        this.plot_synopsis = plot_synopsis;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    private int movieID;

    private String title;

    private String movie_poster;

    private double vote_average;

    private String plot_synopsis;

    private String releaseDate;
}
