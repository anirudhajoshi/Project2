package com.example.aniru.popmovies1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;

/**
 * Created by aniru on 7/15/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieImageHolder> {

    private String posterPath = "";
    private ArrayList<MovieDBAPI_ResponseResults> msortedMovies = new ArrayList<MovieDBAPI_ResponseResults>();
    private Context mContext;

    // Create a final private ListItemClickListener called mOnClickListener
    private ListItemClickListener mOnClickListener;

    // Toggle for favorite movies
    private boolean mbFavMovies = false;


    // Add an interface called ListItemClickListener
    // Within that interface, define a void method called onListItemClick that takes an int as a parameter
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    // Add a ListItemClickListener as a parameter to the constructor and store it in mOnClickListener
    public MovieAdapter(boolean bfavMovies, ArrayList<MovieDBAPI_ResponseResults> sortedmovies, Context context, ListItemClickListener listener) {

        // Initialize member variables that are passed into the constructor
        mbFavMovies = bfavMovies;

        msortedMovies.addAll(sortedmovies);
        mContext = context;
        mOnClickListener = listener;
    }

    public MovieAdapter()  {

        Log.d("MovieAdapter","MovieAdapter()");
    }

    public void setMovieData(boolean bFavMovies, ArrayList<MovieDBAPI_ResponseResults> sortedmovies, Context context, ListItemClickListener listener){

        mbFavMovies = bFavMovies;

        mContext = context;

        mOnClickListener = listener;

        msortedMovies.clear();

        msortedMovies.addAll(sortedmovies);

       notifyDataSetChanged();
    }

    @Override
    public MovieImageHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MovieImageHolder viewHolder = new MovieImageHolder(view);

        // Set the path that will be used to load image into the imageView
        //  Image size = "w92", "w154", "w185", "w342", "w500", "w780", or "original".
        // posterPath = mContext.getString(R.string.baseImageURL) + mContext.getString(R.string.IMAGE_SIZE_w185);
        posterPath = mContext.getString(R.string.baseImageURL) + mContext.getString(R.string.IMAGE_SIZE_w780);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieImageHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {

        // Return the number of movies in our list
        return msortedMovies.size();
    }

    class MovieImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView listItemMovieView;

        public MovieImageHolder(View itemView) {
            super(itemView);

            // Find the imageview and attach a listener to it
            listItemMovieView = (ImageView) itemView.findViewById(R.id.movie_list_item);

            //Call setOnClickListener on the View passed into the constructor (use 'this' as the
            // OnClickListener)
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {

            // Bind & load each image to the imageview with Glide. An image will be loaded for each list item
            MovieDBAPI_ResponseResults movie = msortedMovies.get(listIndex);

            if (mbFavMovies == false) {
                String MoviePosterPath = posterPath + movie.getPosterPath();

                // The scroll position is not maintained when using Glide. The ImageView is collapsing on rotation since
                // the height is set to wrap_content so the position is lost when this happen.
                // Setting a fixed height (to 300dp) for the ImageView in the list item layout makes it function
                // more smoothly but the UI does not show properly. This probelm does not happen with Picasso, so a
                // mix of Picasso and Glide is used here
                Picasso
                        .with(mContext)
                        .load(MoviePosterPath)
                        .into(listItemMovieView);

/*                Glide
                        .with(mContext)
                        .load(MoviePosterPath)
                        .into(listItemMovieView);*/
            } else {

                String path = mContext.getFilesDir().getPath();
                String filename = msortedMovies.get(listIndex).getTitle() + ".jpg";
                String fullfilename = path + "/" + filename;
                try {

                    Glide
                        .with(mContext)
                        .asBitmap()
                        .load(fullfilename)
                        .into(listItemMovieView);

                } catch (Exception e) {
                    Log.d("ViewHolder", e.toString());
                }
            }

            /* Not sure why this does not work -
            Glide
                    .with(mContext)
                    .load(MoviePosterPath)
                    .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                    .error(R.mipmap.ic_launcher) // will be displayed if the image cannot be loaded
                    .into(listItemMovieView);
                    */
        }

        @Override
        public void onClick(View view) {
            // Let MainActivity handle the click
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
