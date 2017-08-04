package com.example.aniru.popmovies1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by aniru on 7/26/2017.
 */

public class FavMoviesDBHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = FavMoviesContract.DATABASE_NAME;

    // If you change the database schema, you must increment the database version
    private static final int VERSION = FavMoviesContract.DATABASE_VERSION;

    private static final String DATABASE_ALTER_MOVIES_9 = "ALTER TABLE "
            + FavMoviesContract.MovieEntry.FAVMOVIES_TABLE;

    // Constructor
    FavMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the movies database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create movies table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + FavMoviesContract.MovieEntry.FAVMOVIES_TABLE + " (" +
                FavMoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                FavMoviesContract.MovieEntry.COLUMN_MOVIEID + " INTEGER, " +
                FavMoviesContract.MovieEntry.COLUMN_MOVIETITLE + " TEXT NOT NULL, " +
                FavMoviesContract.MovieEntry.COLUMN_MOVIEUSERATING + " TEXT, " +
                FavMoviesContract.MovieEntry.COLUMN_MOVIERELEASEDATE + " TEXT NOT NULL, " +
                FavMoviesContract.MovieEntry.COLUMN_MOVIESYNOPSIS + " TEXT NOT NULL);";
        try {
            db.execSQL(CREATE_TABLE);
        }catch (Exception e){
            Log.d("DBHelper",e.toString());
        }
    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
/*    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavMoviesContract.MovieEntry.FAVMOVIES_TABLE);
        onCreate(db);
    }*/

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= 8) {
            db.execSQL("DROP TABLE IF EXISTS " + FavMoviesContract.MovieEntry.FAVMOVIES_TABLE);
            onCreate(db);
        }
        else if(oldVersion > 8) {       // We use this if we make any changes to the database schema going forward
            db.execSQL(DATABASE_ALTER_MOVIES_9);
        }
    }
}

