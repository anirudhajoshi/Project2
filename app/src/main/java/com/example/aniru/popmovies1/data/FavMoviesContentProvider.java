package com.example.aniru.popmovies1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by aniru on 7/26/2017.
 */

import static com.example.aniru.popmovies1.data.FavMoviesContract.MovieEntry.FAVMOVIES_TABLE;

public class FavMoviesContentProvider extends ContentProvider {

    // Define final integer constants for the directory of FavMovies and a single movie item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int MOVIES = 100;   // use rounded to 100th numbers for tables
    public static final int MOVIE_WITH_ID = 101;    // use related int to refer to individual rows in that table

    // Declare a static variable for the Uri matcher
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     Initialize a new matcher object without any matches,
     then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri to access, add the corresponding match with addURI.
          The two calls below add matches for the FavMovies directory and a single item by ID.
         */
        uriMatcher.addURI(FavMoviesContract.CONTENT_AUTHORITY, FavMoviesContract.PATH_FAVMOVIES, MOVIES);
        uriMatcher.addURI(FavMoviesContract.CONTENT_AUTHORITY, FavMoviesContract.PATH_FAVMOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    // Member variable for a FavMoviesDbHelper that's initialized in the onCreate() method
    private FavMoviesDBHelper mMoviesDbHelper;

    // Initialize mMoviesDbHelper to gain access to a SQLite database
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMoviesDbHelper = new FavMoviesDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // (1) Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mMoviesDbHelper.getReadableDatabase();

        // (2) Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor = null;

        String groupBy=null, having=null;   // We do not group on anything here

        // (3) Query for the movies directory and write a default case
        switch (match) {
            // Query for the movies directory
            case MOVIES:
                retCursor =  db.query(FAVMOVIES_TABLE,
                        projection,
                        selection,
                        selectionArgs,
                        groupBy,
                        having,
                        sortOrder);
                break;
            case MOVIE_WITH_ID:
                try {
                    String movieID = uri.getPathSegments().get(1);

                    int iCount = (int) DatabaseUtils.queryNumEntries(db, FAVMOVIES_TABLE,
                            "movieID=?", new String[]{movieID});

                    if (iCount == 0)
                        retCursor = null;
                    else {
                        retCursor = db.query(FAVMOVIES_TABLE,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);

                        // 4) Set a notification URI on the Cursor and return that Cursor
                        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
                    }
                }
                catch(Exception e){
                    Log.d("ContentProvider",e.toString());
                }
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // (1) Get access to the favmovie database to write new data to
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        // (2) Write URI matching code to identify the match for the movies directory
        int match = sUriMatcher.match(uri);
        Uri returnUri = null; // URI to be returned
        String message="";
        long id = -1;

        switch (match) {
            case MOVIES:
                // (3) Insert new values into the moviesDB database,
                // Inserting values into movies table
                try {
                    id = db.insertOrThrow(FAVMOVIES_TABLE, message, values);
                }
                catch(Exception e) {
                    Log.d("ContentProvider",e.toString());
                }

                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(FavMoviesContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // (4) Default case throws an UnsupportedOperationException for unknown URI's
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // (5) Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        // (2) Write URI matching code to identify the match for the movies directory
        int match = sUriMatcher.match(uri);
        Uri returnUri = null; // URI to be returned
        String message="";
        int movieDeleted = 0;

        switch (match) {
            case MOVIE_WITH_ID:
                try {
                    // Get the movie ID from the URI path
                    String movieID = uri.getPathSegments().get(1);

                    // Use selections/selectionArgs to filter for this ID
                    movieDeleted = db.delete(FAVMOVIES_TABLE,
                            "movieID=?", new String[]{movieID});
                }
                catch(Exception e){
                    Log.d("ContentProvider",e.toString());
                }
                break;
            // Default case throws an UnsupportedOperationException for unknown URI's
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // (5) Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return movieDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
