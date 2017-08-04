package com.example.aniru.popmovies1;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsAdapter.ListItemClickListener {

    private final Gson gson = new GsonBuilder()
            .create();

    private Retrofit retrofit;

    ArrayList<MovieTrailers_Results> mMovietrailer_results;

    ArrayList<MovieReviews_Results> mMoviereviews_results;

    private MovieDetailsAdapter mMovieDetailsAdapter;

    @BindView(R.id.rv_movies)
    RecyclerView mMovieList;

    private String movieTrailerUrl = "";

    private boolean mbfavMoviesSelected = false;

    LinearLayoutManager linearLayoutManager_MovieList;

    MovieTrailers mMovieTrailers = new MovieTrailers();

    MovieReviews mMovieReviews = new MovieReviews();

    MovieDetails mMovieDetails = new MovieDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ActionBar actionBar = this.getActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        // Get the movie details that were passed from the main activity
        ParcelableMovieDetails parcelableMovieDetails = intent
                .getParcelableExtra(getString(R.string.MovieDetail));

        // Safety check
        if (parcelableMovieDetails == null) {
            Toast.makeText(this, "There was an error getting the movie's details", Toast.LENGTH_SHORT).show();
            return;
        }

        mMovieDetails = parcelableMovieDetails.getMovieDetails();

        mbfavMoviesSelected = intent.getBooleanExtra("ShowFavs", false);

        // Populate the child activity's view elements with the correct data
        // Movie title
        ButterKnife.bind(this);

        movieTrailerUrl = getString(R.string.movieTrailerUrl);

        mMovieDetailsAdapter = new MovieDetailsAdapter();

        if (savedInstanceState != null) {

            mbfavMoviesSelected = savedInstanceState.getBoolean("isFavMovie");

            mMovieDetails = ((Bundle) savedInstanceState).getParcelable("MOVIEDETAILS");

            mMovieTrailers = ((Bundle) savedInstanceState).getParcelable("TRAILERS");

            mMovieReviews = ((Bundle) savedInstanceState).getParcelable("REVIEWS");

            mMovieDetailsAdapter.updateVideos(mMovieTrailers.getResults());

            mMovieDetailsAdapter.updateReviews(mMovieReviews.getResults());

            mMovieDetailsAdapter.setMovieDetailsData(mbfavMoviesSelected, mMovieDetails, this, this);
        } else {

            mMovieDetailsAdapter.setMovieDetailsData(mbfavMoviesSelected, mMovieDetails, this, this);

            if (mbfavMoviesSelected == false) {
                // Create the retrofit client
                retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.baseURL))
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                MovieDBAPIInterface apiService =
                        retrofit.create(MovieDBAPIInterface.class);

                // Get the movie's trailers
                GetMovieTrailers(mMovieDetails.getMovieID(), apiService);

                // Get the movie's reviews
                GetMovieReviews(mMovieDetails.getMovieID(), apiService);
            }
        }

        linearLayoutManager_MovieList = new LinearLayoutManager(getApplicationContext());

        // Set the layout manager to the linearlayoutmanager
        mMovieList.setLayoutManager(linearLayoutManager_MovieList);

        // Make sure recyclerview size does not change when items are added or removed from it
        mMovieList.setHasFixedSize(true);

        // Set the adapter to the recyclerview
        mMovieList.setAdapter(mMovieDetailsAdapter);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        Bundle bundle = new Bundle();
        outState.putParcelable("MOVIEDETAILS", mMovieDetails);

        outState.putParcelable("TRAILERS", mMovieTrailers);

        outState.putParcelable("REVIEWS", mMovieReviews);

        outState.putBoolean("isFavMovie", mbfavMoviesSelected);
    }

    private void GetMovieTrailers(int movieId, MovieDBAPIInterface apiService) {
        // API to pull movie trailers
        // http://api.themoviedb.org/3/movie/119450/videos?api_key=13c952bf74890cf680bba865b9c2076b
        // Call the MoviesDBAPI asynchronously on a seperate thread so as not to bog down the UI thread

        Call<MovieTrailers> callAPIForTrailers = apiService
                .getMovieTrailers(String.valueOf(movieId), getString(R.string.MyAPIKey));
        callAPIForTrailers.enqueue(new Callback<MovieTrailers>() {
            @Override
            public void onResponse(Call<MovieTrailers> call, Response<MovieTrailers> response) {
                Log.d("MovieDetailsActivity", response.toString());

                MovieTrailers movie_trailers = response.body();

                if (movie_trailers != null) {
                    // mMovietrailer_results = movie_trailers.getResults();
                    mMovieTrailers.setResults(movie_trailers.getResults());

                    // mMovieDetailsAdapter.updateVideos(mMovietrailer_results);
                    mMovieDetailsAdapter.updateVideos(mMovieTrailers.getResults());

                    Log.d("MovieDetailActivity", "Check mMovie_items");
                }
            }

            @Override
            public void onFailure(Call<MovieTrailers> call, Throwable t) {

                // Log error here since request failed
                Log.d(getString(R.string.MainActivity), "ERROR - Request failed");
            }
        });
    }

    private void GetMovieReviews(int movieID, MovieDBAPIInterface apiService) {
        // API to pull movie reviews
        // http://api.themoviedb.org/3/movie/119450/reviews?api_key=13c952bf74890cf680bba865b9c2076b
        // Call the MoviesDBAPI asynchronously on a seperate thread so as not to bog down the UI thread
        Call<MovieReviews> callAPIForReviews = apiService
                .getMovieReviews(String.valueOf(movieID), getString(R.string.MyAPIKey));
        callAPIForReviews.enqueue(new Callback<MovieReviews>() {
            @Override
            public void onResponse(Call<MovieReviews> call, Response<MovieReviews> response) {
                Log.d("MovieDetailsActivity", response.toString());

                MovieReviews movie_reviews = response.body();

                mMovieReviews.setResults(movie_reviews.getResults());

                mMovieDetailsAdapter.updateReviews(mMovieReviews.getResults());
            }

            @Override
            public void onFailure(Call<MovieReviews> call, Throwable t) {

                // Log error here since request failed
                Log.d(getString(R.string.MainActivity), "ERROR - Request failed");
            }
        });
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {

        // Movie trsiler pattern: https://www.youtube.com/watch?v=SUXWAEX2jlg
        // String movieTrailerUrl = "https://www.youtube.com/watch?v=";
        movieTrailerUrl += mMovieDetailsAdapter.mMovieTrailers_Results.get(clickedItemIndex).getKey();
        Log.d("Movie trailer", movieTrailerUrl);

        Uri movieTrailer = Uri.parse(movieTrailerUrl);

        Intent intentPlayTrailer = new Intent(Intent.ACTION_VIEW, movieTrailer);

        if (intentPlayTrailer.resolveActivity(getPackageManager()) != null) {
            startActivity(intentPlayTrailer);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.sharetrailer, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item_share) {
            if (mMovieDetailsAdapter.mMovieTrailers_Results.size() > 0) {
                String mimeType = "@string/mimeType";
                movieTrailerUrl += mMovieDetailsAdapter.mMovieTrailers_Results.get(0).getKey();
                String movietitle = getString(R.string.choosertitle) +
                        mMovieDetailsAdapter.mMovieDetails.getTitle() + getString(R.string.questionmark);

                ShareCompat.IntentBuilder
                        .from(this)
                        .setType(mimeType)
                        .setChooserTitle(movietitle)
                        .setText(movieTrailerUrl)
                        .startChooser();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
