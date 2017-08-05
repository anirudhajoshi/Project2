package com.example.aniru.popmovies1;

/**
 * Created by aniru on 7/21/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieReviews_Results implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("url")
    @Expose
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    protected MovieReviews_Results(Parcel in) {
        //retrieve
        setId( in.readString() );
        setAuthor(in.readString());
        setContent(in.readString());
        setUrl(in.readString());
    }

    public MovieReviews_Results() {}

    public static final Parcelable.Creator<MovieReviews_Results> CREATOR =
            new Parcelable.Creator<MovieReviews_Results>() {
                public MovieReviews_Results createFromParcel(Parcel source) {
                    return new MovieReviews_Results(source);
                }

                public MovieReviews_Results[] newArray(int size) {
                    return new MovieReviews_Results[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getAuthor());
        dest.writeString(getContent());
        dest.writeString(getUrl());
    }
}