package com.example.aniru.popmovies1;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aniru on 7/17/2017.
 */

public class ParcelableMovieDetails implements Parcelable {

    // The MovieDetails class that we want to put in the parcel
    private final MovieDetails movieDetails;

    private ArrayList<MovieTrailers_Results> trailers;
    private ArrayList<MovieReviews_Results> reviews;

    public MovieDetails getMovieDetails() {
        return movieDetails;
    }

    public ParcelableMovieDetails(MovieDetails movieDetails) {
        super();
        this.movieDetails = movieDetails;
    }

    private ParcelableMovieDetails(Parcel in) {

        // Save movie details in parcel
        movieDetails = new MovieDetails();
        movieDetails.setMovieID(in.readInt());
        movieDetails.setTitle(in.readString());
        movieDetails.setMovie_poster(in.readString());
        movieDetails.setVote_average(in.readDouble());
        movieDetails.setPlot_synopsis(in.readString());
        movieDetails.setReleaseDate(in.readString());

        trailers = new ArrayList<MovieTrailers_Results>();
        in.readList(trailers,null);

        reviews = new ArrayList<MovieReviews_Results>();
        in.readList(reviews,null);
    }

    public static final Creator<ParcelableMovieDetails> CREATOR = new Creator<ParcelableMovieDetails>() {
        @Override
        public ParcelableMovieDetails createFromParcel(Parcel in) {
            return new ParcelableMovieDetails(in);
        }

        @Override
        public ParcelableMovieDetails[] newArray(int size) {
            return new ParcelableMovieDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        // Get movie details from parcel
        parcel.writeInt(movieDetails.getMovieID());
        parcel.writeString(movieDetails.getTitle());
        parcel.writeString(movieDetails.getMovie_poster());
        parcel.writeDouble(movieDetails.getVote_average());
        parcel.writeString(movieDetails.getPlot_synopsis());
        parcel.writeString(movieDetails.getReleaseDate());

        parcel.writeList(this.trailers);
        parcel.writeList(this.reviews);
    }
}
