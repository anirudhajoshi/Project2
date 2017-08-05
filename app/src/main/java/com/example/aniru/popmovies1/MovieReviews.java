package com.example.aniru.popmovies1;

/**
 * Created by aniru on 7/21/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieReviews implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    public ArrayList<MovieReviews_Results> results = null;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public ArrayList<MovieReviews_Results> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieReviews_Results> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public MovieReviews() {}

    protected MovieReviews(Parcel in) {
        //retrieve
        ArrayList trailers = new ArrayList<MovieReviews>();
        trailers = in.readArrayList(MovieReviews.class.getClassLoader());
    }

    public static final Parcelable.Creator<MovieReviews> CREATOR =
            new Parcelable.Creator<MovieReviews>() {
                public MovieReviews createFromParcel(Parcel source) {
                    return new MovieReviews(source);
                }

                public MovieReviews[] newArray(int size) {
                    return new MovieReviews[size];
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
