package com.example.aniru.popmovies1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.view.View.GONE;
import static java.lang.String.valueOf;

/**
 * Created by aniru on 7/21/2017.
 */

public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    // private final int MOVIE_HEADER = 0, MOVIE_TRAILERS = 1, MOVIE_REVIEWS = 2;
    private final int MOVIE_HEADER = 0, MOVIE_TRAILERS_HEADER = 1, MOVIE_TRAILERS = 2,
            MOVIE_REVIEWS_HEADER = 3, MOVIE_REVIEWS = 4;

    String mPosterPath = "";

    ImageView imageView;

    MovieDetails mMovieDetails;
    ArrayList<MovieTrailers_Results> mMovieTrailers_Results = new ArrayList<MovieTrailers_Results>();
    ArrayList<MovieReviews_Results> mMovieReviews_Results = new ArrayList<MovieReviews_Results>();

    boolean mbfavMoviesSelected = false;

    private ListItemClickListener mOnClickListener_Trailers;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public void updateVideos(ArrayList<MovieTrailers_Results> videos) {
        this.mMovieTrailers_Results = videos;
        notifyDataSetChanged();
    }

    public ArrayList<MovieTrailers_Results> getVideos(){
        return this.mMovieTrailers_Results;
    }

    public void updateReviews(ArrayList<MovieReviews_Results> reviews) {
        this.mMovieReviews_Results = reviews;
        notifyDataSetChanged();
    }

    public ArrayList<MovieReviews_Results> getReviews(){
        return this.mMovieReviews_Results;
    }

    public MovieDetailsAdapter(boolean bfavMoviesSelected, MovieDetails movieDetails,
                               Context context, ListItemClickListener listenerTrailers)

    {
        mbfavMoviesSelected = bfavMoviesSelected;
        mMovieDetails = movieDetails;
        mContext = context;
        mPosterPath = mContext.getString(R.string.baseImageURL) + mContext.getString(R.string.IMAGE_SIZE_w185);
        mOnClickListener_Trailers = listenerTrailers;
    }

    public MovieDetailsAdapter() {}

    public void setMovieDetailsData(boolean bfavMoviesSelected, MovieDetails movieDetails,
                               Context context, ListItemClickListener listenerTrailers){
        mbfavMoviesSelected = bfavMoviesSelected;
        mMovieDetails = movieDetails;
        mContext = context;
        mPosterPath = mContext.getString(R.string.baseImageURL) + mContext.getString(R.string.IMAGE_SIZE_w185);
        mOnClickListener_Trailers = listenerTrailers;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case MOVIE_HEADER:
                View view_MovieHeader = inflater.inflate(R.layout.movie_header, parent, false);
                viewHolder = new ViewHolder_MovieHeader(view_MovieHeader, this);
                break;
            case MOVIE_TRAILERS_HEADER:
                View view_MovieTrailerHeader = inflater.inflate(R.layout.movietrailers_header, parent, false);
                viewHolder = new ViewHolder_MovieTrailersHeader(view_MovieTrailerHeader);
                break;
            case MOVIE_TRAILERS:
                View view_MovieTrailer = inflater.inflate(R.layout.movietrailer_list_item, parent, false);
                viewHolder = new ViewHolder_MovieTrailers(view_MovieTrailer, mOnClickListener_Trailers, this);
                break;
            case MOVIE_REVIEWS_HEADER:
                View view_MovieReviewsHeader = inflater.inflate(R.layout.moviereviews_header, parent, false);
                viewHolder = new ViewHolder_MovieReviewsHeader(view_MovieReviewsHeader);
                break;
            case MOVIE_REVIEWS:
                View view_MovieReviews = inflater.inflate(R.layout.movie_review_list_item, parent, false);
                viewHolder = new ViewHolder_MovieReviews(view_MovieReviews);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case MOVIE_HEADER:
                ViewHolder_MovieHeader vh_MovieHeader = (ViewHolder_MovieHeader) holder;
                configureViewHolder_MovieHeader(vh_MovieHeader, position);
                break;
            case MOVIE_TRAILERS_HEADER:
                ViewHolder_MovieTrailersHeader vh_MovieTrailersHeader = (ViewHolder_MovieTrailersHeader) holder;
                configureViewHolder_MovieTrailersHeader(vh_MovieTrailersHeader, position);
                break;
            case MOVIE_TRAILERS:
                ViewHolder_MovieTrailers vh_MovieTrailers = (ViewHolder_MovieTrailers) holder;
                configureViewHolder_MovieTrailer(vh_MovieTrailers, position);
                break;
            case MOVIE_REVIEWS_HEADER:
                ViewHolder_MovieReviewsHeader vh_MovieReviewsHeader = (ViewHolder_MovieReviewsHeader) holder;
                configureViewHolder_MovieReviewsHeader(vh_MovieReviewsHeader);
                break;
            case MOVIE_REVIEWS:
                ViewHolder_MovieReviews vh_MovieReviews = (ViewHolder_MovieReviews) holder;
                configureViewHolder_MovieReviews(vh_MovieReviews, position);
                break;
        }
    }

    private void configureViewHolder_MovieReviewsHeader(ViewHolder_MovieReviewsHeader vh_movieReviewsHeader) {

        if (mbfavMoviesSelected) {
            vh_movieReviewsHeader.getVw_horizontaldivider().setVisibility(View.INVISIBLE);
            vh_movieReviewsHeader.getTv_ReviewssHeading().setVisibility(View.INVISIBLE);
            return;
        }

        if (mMovieReviews_Results.size() == 0) {
            vh_movieReviewsHeader.getVw_horizontaldivider().setVisibility(View.INVISIBLE);
            vh_movieReviewsHeader.getTv_ReviewssHeading().setVisibility(View.INVISIBLE);
        } else {
            vh_movieReviewsHeader.getVw_horizontaldivider().setVisibility(View.VISIBLE);
            vh_movieReviewsHeader.getTv_ReviewssHeading().setVisibility(View.VISIBLE);
        }
    }

    private void configureViewHolder_MovieReviews(ViewHolder_MovieReviews vh_movieReviews, int position) {

        if (mMovieReviews_Results != null) {
            if (mMovieReviews_Results.size() > 0) {

                int moviedetailscount = 1, movietrailerheadercount = 1, moviereviewheadercount = 1;

                // Get the offset: -1 - 1 to account for movie trailers header & movie reviews header
                int moviereviewresults_position = position - mMovieTrailers_Results.size() - moviedetailscount
                        - movietrailerheadercount - moviereviewheadercount;
                String author = mMovieReviews_Results.get(moviereviewresults_position).getAuthor();
                String review = mMovieReviews_Results.get(moviereviewresults_position).getContent();
                vh_movieReviews.getTv_moviereview().setText(author + ": "
                        + review + "\n\n\n");
            } else {
                vh_movieReviews.getTv_moviereview().setVisibility(GONE);
                vh_movieReviews.getTv_moviereview().setText("No reviews to display");
            }
        } else {
            vh_movieReviews.getTv_moviereview().setVisibility(GONE);
            vh_movieReviews.getTv_moviereview().setText("No reviews to display");
        }
    }

    private void configureViewHolder_MovieTrailersHeader(ViewHolder_MovieTrailersHeader vh_MovieTrailersHeader, int position) {

        if (mbfavMoviesSelected) {
            vh_MovieTrailersHeader.getVw_horizontaldivider().setVisibility(View.GONE);
            vh_MovieTrailersHeader.getTv_TrailersHeading().setVisibility(View.GONE);
            return;
        }

        if (mMovieTrailers_Results != null) {
            if (mMovieTrailers_Results.size() > 0) {
                vh_MovieTrailersHeader.getVw_horizontaldivider().setVisibility(View.VISIBLE);
                vh_MovieTrailersHeader.getTv_TrailersHeading().setVisibility(View.VISIBLE);
            } else {
                vh_MovieTrailersHeader.getVw_horizontaldivider().setVisibility(View.INVISIBLE);
                vh_MovieTrailersHeader.getTv_TrailersHeading().setVisibility(View.INVISIBLE);
            }
        } else {
            vh_MovieTrailersHeader.getVw_horizontaldivider().setVisibility(View.INVISIBLE);
            vh_MovieTrailersHeader.getTv_TrailersHeading().setVisibility(View.INVISIBLE);
        }
    }

    private void configureViewHolder_MovieTrailer(ViewHolder_MovieTrailers vh_movieTrailers, int position) {

        if (mMovieTrailers_Results != null) {
            if (mMovieTrailers_Results.size() > 0) {

                String posterPath = mContext.getString(R.string.baseImageURL) + mContext.getString(R.string.IMAGE_SIZE_w185);
                posterPath += mMovieDetails.getMovie_poster();
                // ImageView imageView = vh_movieTrailers.getIv_movietrailers();
                imageView = vh_movieTrailers.getIv_movietrailers();
                Glide
                        .with(mContext)
                        .load(posterPath)
                        .into(imageView);
            } else {
                ImageView imageView = vh_movieTrailers.getIv_movietrailers();
                imageView.setVisibility(GONE);
                Log.d("MovieDetailsAdapter", "No trailers to show");
            }
        } else {
            ImageView imageView = vh_movieTrailers.getIv_movietrailers();
            imageView.setVisibility(GONE);
            Log.d("MovieDetailsAdapter", "No trailers to show");
        }
    }

    private void configureViewHolder_MovieHeader(ViewHolder_MovieHeader vh_movieHeader, int position) {

        ImageView imageView = vh_movieHeader.getIv_Poster();

        if (mbfavMoviesSelected) {
            imageView.getLayoutParams().width = 342;
            imageView.getLayoutParams().height = 342;

            String path = mContext.getFilesDir().getPath();
            String filename = mMovieDetails.getTitle() + ".jpg";
            String fullfilename = path + "/" + filename;
            try {
                Glide
                        .with(mContext)
                        .asBitmap()
                        .load(fullfilename)
                        .into(imageView);

            } catch (Exception e) {
                Log.d("ViewHolder", e.toString());
            }
        } else {
            vh_movieHeader.getTv_movietitle().setText(mMovieDetails.getTitle());

            String posterPath = mContext.getString(R.string.baseImageURL) + mContext.getString(R.string.IMAGE_SIZE_w342);
            posterPath += mMovieDetails.getMovie_poster();


            Glide
                    .with(mContext)
                    .load(posterPath)
                    .into(imageView);
        }

        String release_date = mMovieDetails.getReleaseDate().length() < 4 ?
                mMovieDetails.getReleaseDate() : mMovieDetails.getReleaseDate().substring(0, 4);
        vh_movieHeader.getTv_releasedate().setText(release_date);

        String voter_ratings = String.valueOf(mMovieDetails.getVote_average()) +
                mContext.getString(R.string.votermaxscore);
        vh_movieHeader.getTv_VoteAverage().setText(voter_ratings);

        vh_movieHeader.getTv_Synopsis().setText(mMovieDetails.getPlot_synopsis());
    }

    // Return the number of items in our data set = (moviedetails will count as 1 item) + number of movie trailers + number of movie reviews
    @Override
    public int getItemCount() {

        int moviedetailscount = 1,
                movietrailerheadercount = 1, videoCount = 0,
                moviereviewheadercount = 1, reviewCount = 0;
        int itemcounts;

        if (mMovieTrailers_Results != null)
            videoCount = mMovieTrailers_Results.size();

        if (mMovieReviews_Results != null)
            reviewCount = mMovieReviews_Results.size();

        itemcounts = moviedetailscount +
                movietrailerheadercount + videoCount +
                moviereviewheadercount + reviewCount;

        return itemcounts;
    }

    //Returns the view type of the item at position for the purposes of view recycling
    @Override
    public int getItemViewType(int position) {

        int moviedetailscount = 1,
                movietrailersheadercount = 1, movietrailerscount = 0,
                moviereviewsheadercount = 1, moviereviewscount = 0;

        if (mMovieTrailers_Results != null)
            movietrailerscount = mMovieTrailers_Results.size();

        if (mMovieReviews_Results != null)
            moviereviewscount = mMovieReviews_Results.size();

        int totalitemscount = moviedetailscount +
                movietrailersheadercount + movietrailerscount +
                moviereviewsheadercount + moviereviewscount;

        if (position == 0) {
            return MOVIE_HEADER;
        } else if (position == 1) {
            return MOVIE_TRAILERS_HEADER;
        } else if (position > 1 && position <= movietrailerscount + 1) { // movietrailerscount+1 to account for movietrailersheader item
            return MOVIE_TRAILERS;
        } else if (position > movietrailerscount + 1 && position <= (totalitemscount - moviereviewscount) - 1) {
            return MOVIE_REVIEWS_HEADER;
        } else if (position > movietrailerscount + 1 && position <= totalitemscount) { // movietrailerscount+1 to account for movietrailersheader item
            return MOVIE_REVIEWS;
        }

        return -1;
    }
}
