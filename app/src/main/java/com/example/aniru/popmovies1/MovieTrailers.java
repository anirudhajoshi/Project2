/**
 * Created by aniru on 7/21/2017.
 */
package com.example.aniru.popmovies1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieTrailers implements Parcelable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private ArrayList<MovieTrailers_Results> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<MovieTrailers_Results> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieTrailers_Results> results) {
        this.results = results;
    }

    public MovieTrailers() {}

    protected MovieTrailers(Parcel in) {
        //retrieve
        ArrayList trailers = new ArrayList<MovieTrailers>();
        trailers = in.readArrayList(MovieTrailers.class.getClassLoader());
    }

    public static final Parcelable.Creator<MovieTrailers> CREATOR =
            new Parcelable.Creator<MovieTrailers>() {
                public MovieTrailers createFromParcel(Parcel source) {
                    return new MovieTrailers(source);
                }

                public MovieTrailers[] newArray(int size) {
                    return new MovieTrailers[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(results);
    }
}