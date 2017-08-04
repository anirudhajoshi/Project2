package com.example.aniru.popmovies1;

import android.media.Image;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by aniru on 7/21/2017.
 */

public class ViewHolder_MovieTrailers extends ViewHolder implements View.OnClickListener {

    final private MovieDetailsAdapter.ListItemClickListener mOnClickListener_Trailers;

    private ImageView iv_movietrailers;

    public ImageView getIv_movietrailers() {
        return iv_movietrailers;
    }

    public void setIv_movietrailers(ImageView iv_movietrailers) {
        this.iv_movietrailers = iv_movietrailers;
    }

    private MovieDetailsAdapter mMovieDetailsAdapter;

    public ViewHolder_MovieTrailers(View itemView, MovieDetailsAdapter.ListItemClickListener mOnClickListener_trailers,
                                    MovieDetailsAdapter movieDetailsAdapter) {
        super(itemView);
        mOnClickListener_Trailers = mOnClickListener_trailers;

        mMovieDetailsAdapter = movieDetailsAdapter;
        iv_movietrailers = (ImageView) itemView.findViewById(R.id.movietrailer_list_item);
        iv_movietrailers.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int movietrailerheadercount = 1, moviereviewheadercount = 1;

        // Get the correct offset for the trailer item that was clicked
        int clickedPosition = getAdapterPosition() - movietrailerheadercount - moviereviewheadercount;

        // Handle the click in the MovieDetailsActivity
        mOnClickListener_Trailers.onListItemClick(clickedPosition);

    }
}
