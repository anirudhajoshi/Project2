package com.example.aniru.popmovies1;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aniru.popmovies1.data.FavMoviesContract;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.getIntent;
import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by aniru on 7/21/2017.
 */

public class ViewHolder_MovieHeader extends ViewHolder {

    private MovieDetailsAdapter mMovieDetailsAdapter;

    // @BindView(R.id.tv_Title) TextView tv_movietitle;
    TextView tv_movietitle;

    public TextView getTv_movietitle() {
        return tv_movietitle;
    }

    // @BindView(R.id.iv_Poster) ImageView iv_Poster;
    ImageView iv_Poster;

    public ImageView getIv_Poster() {
        return iv_Poster;
    }

    public void setIv_Poster(ImageView iv_Poster) {
        this.iv_Poster = iv_Poster;
    }

    // @BindView(R.id.tv_ReleaseDate) TextView tv_ReleaseDate;
    TextView tv_ReleaseDate;

    public TextView getTv_releasedate() {
        return tv_ReleaseDate;
    }

    // @BindView(R.id.tv_VoteAverage) TextView tv_VoteAverage;
    TextView tv_VoteAverage;

    public TextView getTv_VoteAverage() {
        return tv_VoteAverage;
    }

    // @BindView(R.id.btn_SaveToFav)
    Button btn_SaveToFav;

    public Button getBtn_SaveToFav() {
        return btn_SaveToFav;
    }

    // @BindView(R.id.tv_synopsis) TextView tv_Synopsis;
    TextView tv_Synopsis;

    public TextView getTv_Synopsis() {
        return tv_Synopsis;
    }

    private ContentValues mContentValues = new ContentValues();
    private ContentResolver mContentResolver;
    private Uri mUrisinglequery;

    public ViewHolder_MovieHeader(View itemView, MovieDetailsAdapter movieDetailsAdapter) {
        super(itemView);

        // ButterKnife.bind(itemView);

        tv_movietitle = (TextView) itemView.findViewById(R.id.tv_Title);
        iv_Poster = (ImageView) itemView.findViewById(R.id.iv_Poster);
        tv_ReleaseDate = (TextView) itemView.findViewById(R.id.tv_ReleaseDate);
        tv_VoteAverage = (TextView) itemView.findViewById(R.id.tv_VoteAverage);
        tv_VoteAverage = (TextView) itemView.findViewById(R.id.tv_VoteAverage);
        btn_SaveToFav = (Button) itemView.findViewById(R.id.btn_SaveToFav);
        btn_SaveToFav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnButtonClick_SaveToFav();
            }
        });
        tv_Synopsis = (TextView) itemView.findViewById(R.id.tv_synopsis);

        mMovieDetailsAdapter = movieDetailsAdapter;

        tv_movietitle.setText(mMovieDetailsAdapter.mMovieDetails.getTitle());

        int movieID = mMovieDetailsAdapter.mMovieDetails.getMovieID();

        if( GetFavMovieData(movieID)!=null ){
            btn_SaveToFav.setText(mMovieDetailsAdapter.mContext.getString(R.string.btntextAddedToFavs));
        }
    }

    private void setupContentProvider(){

        // Get the movie ID & store it in the ContentValues
        mContentValues.put(FavMoviesContract.MovieEntry.COLUMN_MOVIEID,
                mMovieDetailsAdapter.mMovieDetails.getMovieID());

        // Get the movie title & store it in the ContentValues
        mContentValues.put(FavMoviesContract.MovieEntry.COLUMN_MOVIETITLE,
                mMovieDetailsAdapter.mMovieDetails.getTitle());

        // Get the movie user rating & store it in the Content Values
        mContentValues.put(FavMoviesContract.MovieEntry.COLUMN_MOVIEUSERATING,
                mMovieDetailsAdapter.mMovieDetails.getVote_average());

        // Get the movie release date & store it in the Content Values
        mContentValues.put(FavMoviesContract.MovieEntry.COLUMN_MOVIERELEASEDATE,
                mMovieDetailsAdapter.mMovieDetails.getReleaseDate());

        // Get the movie synopsis & store it in the content values
        mContentValues.put(FavMoviesContract.MovieEntry.COLUMN_MOVIESYNOPSIS,
                mMovieDetailsAdapter.mMovieDetails.getPlot_synopsis());
    }

    private Cursor GetFavMovieData(int movieID) {

        final String movieIDString = "" + movieID;

        try {
            // Check to see if the user has added this movie to his favorites
            String[] projection =
                    {
                            FavMoviesContract.MovieEntry.COLUMN_MOVIEID    // Contract class constant for the movieID column name
                    };

            // Defines a string to contain the selection clause
            String selectionClause = "movieID=?";

            // Initializes an array to contain selection arguments
            String[] selectionArgs = new String[]{String.valueOf(movieID)};

            // Initializes an array to contain sort order arguments
            String sortorderArgs = null;

            mUrisinglequery = FavMoviesContract.MovieEntry.CONTENT_URI.buildUpon().appendEncodedPath(movieIDString).build();

            // Insert the content values via a ContentResolver
            mContentResolver = mMovieDetailsAdapter.mContext.getContentResolver();

            Cursor cursor = mContentResolver.query(mUrisinglequery, projection,
                    selectionClause, selectionArgs, sortorderArgs);
            return cursor;
        } catch (Exception e) {
            Log.d("DBHelper", e.toString());
            return null;
        }
    }

    private void OnButtonClick_SaveToFav() {

        setupContentProvider();

        int movieID = mMovieDetailsAdapter.mMovieDetails.getMovieID();
        final String movieIDString = "" + movieID;

        try {
            Cursor cursor = GetFavMovieData(movieID);
            if (cursor != null) {
                // Tell user the movie is already in his favorites list and ask if it should be deleted
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked - the user wants to delete the movie from the favorites list
                                int moviedeleted = mContentResolver.delete(mUrisinglequery, movieIDString, null);
                                String msg = "";
                                if (moviedeleted > 0) {
                                    msg = mMovieDetailsAdapter.mMovieDetails.getTitle() +
                                            mMovieDetailsAdapter.mContext.getString(R.string.movieadeletedmsg);

                                    // Also need to delete it from internal storage
                                    String path = mMovieDetailsAdapter.mContext.getFilesDir().getPath();
                                    String filename = mMovieDetailsAdapter.mMovieDetails.getTitle() + ".jpg";
                                    String fullfilename = path + "/" + filename;

                                    mMovieDetailsAdapter.notifyDataSetChanged();

                                    try {
                                        File file = mMovieDetailsAdapter.mContext.getFileStreamPath(filename);
                                        if (file.exists()) {
                                            file.delete();
                                        }
                                    } catch (Exception e) {
                                        Log.d("ViewHolder", e.toString());
                                    }
                                } else {
                                    msg = mMovieDetailsAdapter.mMovieDetails.getTitle() +
                                            mMovieDetailsAdapter.mContext.getString(R.string.moviecouldnoteletemsg);
                                }

                                getBtn_SaveToFav().setText(mMovieDetailsAdapter.mContext.getString(R.string.savebuttontext));

                                Toast.makeText(mMovieDetailsAdapter.mContext, msg, Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked - do nothing here since the user wants to keep the movie in the favorites list
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(mMovieDetailsAdapter.mContext);
                String msgYes = mMovieDetailsAdapter.mContext.getString(R.string.alert_yestext);
                String msgNo = mMovieDetailsAdapter.mContext.getString(R.string.alert_notext);
                String msg = mMovieDetailsAdapter.mMovieDetails.getTitle() +
                        mMovieDetailsAdapter.mContext.getString(R.string.alert_favmovieyesno);
                builder.setMessage(msg)
                        .setPositiveButton(msgYes, dialogClickListener)
                        .setNegativeButton(msgNo, dialogClickListener).show();
            } else {
                try {
                    // If he has not, add it
                    Uri uri = mContentResolver.insert(FavMoviesContract.MovieEntry.CONTENT_URI, mContentValues);

                    // Save movie poster as a bitmap file to internal storage
                    String path = mMovieDetailsAdapter.mContext.getFilesDir().getPath();
                    String filename = mMovieDetailsAdapter.mMovieDetails.getTitle() + ".jpg";
                    FileOutputStream fos = new FileOutputStream(new File(path, filename));

                    // Write bitmap to file
                    mMovieDetailsAdapter.imageView.buildDrawingCache();
                    // Bitmap bmap = mMovieDetailsAdapter.imageView.getDrawingCache();
                    // bmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    BitmapDrawable drawable = (BitmapDrawable) mMovieDetailsAdapter.imageView.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();

                    getBtn_SaveToFav().setText(mMovieDetailsAdapter.mContext.getString(R.string.btntextAddedToFavs));

                    String msg = mMovieDetailsAdapter.mMovieDetails.getTitle() + mMovieDetailsAdapter.mContext.getString(R.string.movieaddedmsg);
                    Toast.makeText(mMovieDetailsAdapter.mContext, msg, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.d("ViewHolder", e.toString());
                }
            }
        } catch (Exception e) {
            Log.d("DBHelper", e.toString());
        }
    }
}
