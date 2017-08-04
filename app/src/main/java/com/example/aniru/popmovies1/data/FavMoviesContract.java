package com.example.aniru.popmovies1.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by aniru on 7/26/2017.
 */

public class FavMoviesContract {

    // Favorite movies database
    public static final String DATABASE_NAME = "moviesDB.db";

    // Our database version
    public static final int DATABASE_VERSION = 8;


    // Content authority for MyFavMovies provider.
    public static final String CONTENT_AUTHORITY = "com.example.aniru.MyFavMovies";

    // URI for this MyFavMovies provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // This is the path for the movies
    public static final String PATH_FAVMOVIES = "favMovies";

    // This is the link used to get the favorite movies
    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVMOVIES).build();

    // his is a String type that denotes a Uri references a list or directory.
    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVMOVIES;

    // This is a String type that denotes a Uri references a single item.
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVMOVIES;


    public static Uri buildMovieUriWithId(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class MovieEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVMOVIES).build();

        // Favorite movies table
        public static final String FAVMOVIES_TABLE = "favmovies_entries";

        // Column name for MovieID
        public static final String COLUMN_MOVIEID = "movieID";

        // Column name for MovieTitle
        public static final String COLUMN_MOVIETITLE = "movieTitle";

        // Column name for MovieUserRating
        public static final String COLUMN_MOVIEUSERATING = "movieUserRating";

        // Column name for MovieReleaseDate
        public static final String COLUMN_MOVIERELEASEDATE = "movieReleaseDate";

        // Column name for MovieSynopsis
        public static final String COLUMN_MOVIESYNOPSIS = "movieSynopsis";

        // Column name for MoviePoster
        public static final String COLUMN_MOVIEPOSTER = "moviePoster";
    }
}
