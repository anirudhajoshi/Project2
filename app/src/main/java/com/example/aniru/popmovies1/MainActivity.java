package com.example.aniru.popmovies1;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aniru.popmovies1.data.FavMoviesContract;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.ListItemClickListener {

    int index;

    private GetFavoriteMoviesFromDB mgetFavoriteMoviesFromDB;
    private Cursor mFavMoviesCursor;

    @BindView(R.id.rv_movies)
    MoviesRecyclerView mMoviesList;
    // RecyclerView mMoviesList;

    @BindView(R.id.tv_error_message)
    TextView mErrorMessage;

    private MovieAdapter mMovieAdapter;

    // Sorted movies list
    private ArrayList<MovieDBAPI_ResponseResults> msortedMovies = new ArrayList<MovieDBAPI_ResponseResults>();

    String mUserSelection;

    private boolean mbfavmovies = false;
    private ArrayList<MovieDBAPI_ResponseResults> mFavMovies = new ArrayList<MovieDBAPI_ResponseResults>();

    private final Gson gson = new GsonBuilder()
            .create();

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Create the retrofit client
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Get reference to the recyclerview
        // mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);
        ButterKnife.bind(this);

        // Set the gridlayoutmanager with default orientation of vertical and column count of 2
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

        // Set the layout manager to the gridlayoutmanager
        mMoviesList.setLayoutManager(gridLayoutManager);

        // Make sure recyclerview size does not change when items are added or removed from it
        mMoviesList.setHasFixedSize(true);

        // For error message
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);

        // Hide the error message initially
        hideErrorMessage();

        mUserSelection = getString(R.string.sort_popular);

        mMovieAdapter = new MovieAdapter();

        if (savedInstanceState != null) {
            // Get user selection
            mUserSelection = savedInstanceState.getString(getString(R.string.sort_userselection));

            // Get the movies list from our parcel
            msortedMovies = savedInstanceState.getParcelableArrayList("MoviesList");

            if( mUserSelection.equalsIgnoreCase("My favorites") )
                mbfavmovies = true;

            mMovieAdapter.setMovieData(mbfavmovies,msortedMovies, MainActivity.this, MainActivity.this);

            mMoviesList.setAdapter(mMovieAdapter);

            // - This works as well to get the scroll position for a out of the box recycler view -
            // int iScrollPosition = savedInstanceState.getInt("ScrollPosition");
            // ((GridLayoutManager)mMoviesList.getLayoutManager()).scrollToPosition(iScrollPosition);

            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable("RV_STATE");

            mMoviesList.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
        else{
            // Get the movies list since we do not have anything saved in our viewstate
            getMovies(mUserSelection);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if( mbfavmovies ){

            mgetFavoriteMoviesFromDB = new GetFavoriteMoviesFromDB();

            mgetFavoriteMoviesFromDB.execute();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        // Restore value of user selected choice of movies to display from saved state
        mUserSelection = savedInstanceState.getString(getString(R.string.sort_userselection));

        Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable("RV_STATE");

        mMoviesList.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        String saveUserSelection = mUserSelection;

        // Save value of user selected choice of movies to display
        outState.putString(getString(R.string.sort_userselection), saveUserSelection);

        outState.putParcelableArrayList("MoviesList", (ArrayList<MovieDBAPI_ResponseResults>) msortedMovies);

        outState.putParcelable("RV_STATE", mMoviesList.getLayoutManager().onSaveInstanceState());

        // mScrollPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        int iScrollPosition = ((GridLayoutManager)mMoviesList.getLayoutManager()).findFirstVisibleItemPosition();

        outState.putInt("ScrollPosition", iScrollPosition);

    }

    // Hide the error message
    private void hideErrorMessage() {

        mMoviesList.setVisibility(View.VISIBLE);

        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    // Show the error message
    private void showErrorMessage() {
        mMoviesList.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void getMovies(String inOrderSpecified) {
        MovieDBAPIInterface apiService =
                retrofit.create(MovieDBAPIInterface.class);

        /* API Endpoints to use
        http://api.themoviedb.org/3/movie/popular?api_key=13c952bf74890cf680bba865b9c2076b
        http://api.themoviedb.org/3/movie/top_rated?api_key=13c952bf74890cf680bba865b9c2076b
        */
        // Call the MoviesDBAPI asynchronously on a seperate thread so as not to bog down the UI thread
        Call<Movies> call = apiService
                .getMovies(inOrderSpecified, getString(R.string.MyAPIKey));
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {

                // We successfully retreieved data so hide the error message
                hideErrorMessage();

                // Get the JSON
                Movies movies = response.body();

                // Safety check to make sure we really have the movies list
                if (movies != null) {
                    // Get the list of movies
                    // msortedMovies = movies.getResults();
                    msortedMovies.clear();

                    msortedMovies.addAll(movies.getResults());

                    Log.d("MainActivity", msortedMovies.toString());

                    // Create the adpater that the recyclerview will use
                    mbfavmovies = false;

                    // mMovieAdapter = new MovieAdapter(mbfavmovies, msortedMovies, MainActivity.this, MainActivity.this);

                    mMovieAdapter.setMovieData(mbfavmovies, msortedMovies, MainActivity.this, MainActivity.this);
                    // mMovieAdapter.notifyDataSetChanged();

                    // Set the adapter to the recyclerview
                    mMoviesList.setAdapter(mMovieAdapter);
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

                // We could not retrieve data over the network so show the error message
                showErrorMessage();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.moviessortorder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Depending on the use selection show movies that are popular or top rated
        int menuItemThatWasSelected = item.getItemId();
        if (menuItemThatWasSelected == R.id.action_popular) {
            mUserSelection = getString(R.string.sort_popular);

            // Refactor once we have My Favs working
            getMovies(mUserSelection);
        } else if (menuItemThatWasSelected == R.id.action_toprated) {
            mUserSelection = getString(R.string.sort_toprated);

            // Refactor once we have My Favs working
            getMovies(mUserSelection);
        } else if (menuItemThatWasSelected == R.id.action_myfavs) {
            mUserSelection = getString(R.string.action_myfavs);

            mgetFavoriteMoviesFromDB = new GetFavoriteMoviesFromDB();
            mgetFavoriteMoviesFromDB.execute();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        // Get the movie details that the user is interested in
        MovieDetails movieDetails = new MovieDetails();

        // Get the movie id, title, poster path, voter score, plot synopsis & movie's release date
        movieDetails.setMovieID(msortedMovies.get(clickedItemIndex).getId());
        movieDetails.setTitle(msortedMovies.get(clickedItemIndex).getTitle());
        movieDetails.setMovie_poster(msortedMovies.get(clickedItemIndex).getPosterPath());
        movieDetails.setVote_average(msortedMovies.get(clickedItemIndex).getVoteAverage());
        movieDetails.setPlot_synopsis(msortedMovies.get(clickedItemIndex).getOverview());
        movieDetails.setReleaseDate(msortedMovies.get(clickedItemIndex).getReleaseDate());

        // Create our intent to launch the chold activity
        Intent intent = new Intent(getApplicationContext(),
                MovieDetailsActivity.class);

        //Create Parcelable object that we will pass to the child activity
        ParcelableMovieDetails parcelableMovie = new ParcelableMovieDetails(movieDetails);

        //Store Parcelable object in Intent
        intent.putExtra(getString(R.string.MovieDetail), parcelableMovie);
        intent.putExtra("ShowFavs", mbfavmovies);


        //Start the child activity
        startActivity(intent);
    }

    private class GetFavoriteMoviesFromDB extends AsyncTask<Object, Object, Cursor> {

        @Override
        protected Cursor doInBackground(Object... params) {

            try {
                return getContentResolver().query(FavMoviesContract.MovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Cursor result) {
            // Set the cursor to
            mFavMoviesCursor = result;

            if (mFavMoviesCursor != null && mFavMoviesCursor.getCount() > 0) {

                int movieID = mFavMoviesCursor.getColumnIndex(FavMoviesContract.MovieEntry.COLUMN_MOVIEID);
                int movieTitle = mFavMoviesCursor.getColumnIndex(FavMoviesContract.MovieEntry.COLUMN_MOVIETITLE);
                int movieUserRating = mFavMoviesCursor.getColumnIndex(FavMoviesContract.MovieEntry.COLUMN_MOVIEUSERATING);
                int movieReleaseDate = mFavMoviesCursor.getColumnIndex(FavMoviesContract.MovieEntry.COLUMN_MOVIERELEASEDATE);
                int movieSynopsis = mFavMoviesCursor.getColumnIndex(FavMoviesContract.MovieEntry.COLUMN_MOVIESYNOPSIS);

                mFavMoviesCursor.moveToFirst();

                if( msortedMovies!=null )
                    msortedMovies.clear();

                do {
                    MovieDBAPI_ResponseResults favMovieDetails = new MovieDBAPI_ResponseResults();

                    favMovieDetails.setId(mFavMoviesCursor.getInt(movieID));

                    // For favorites we are going to get the movie poster from internal storage. So set the path here to the filename
                    String path = getFilesDir().getPath();
                    String filename = mFavMoviesCursor.getString(movieTitle) + ".jpg";
                    String fullfilename = path + "/" + filename;
                    favMovieDetails.setPosterPath(fullfilename);

                    favMovieDetails.setTitle(mFavMoviesCursor.getString(movieTitle));
                    favMovieDetails.setVoteAverage(mFavMoviesCursor.getDouble(movieUserRating));
                    favMovieDetails.setOverview(mFavMoviesCursor.getString(movieSynopsis));
                    favMovieDetails.setReleaseDate(mFavMoviesCursor.getString(movieReleaseDate));

                    msortedMovies.add(favMovieDetails);

                } while (mFavMoviesCursor.moveToNext());

                mFavMoviesCursor.close();

                // Create the adpater that the recyclerview will use
                mbfavmovies = true;

                if( msortedMovies.size()>0 )
                    mMovieAdapter.setMovieData(mbfavmovies, msortedMovies, MainActivity.this, MainActivity.this);

            } else {
                // Set the user selection to popular so that the fav movie flag is not set to true in onSaveInstan
                mUserSelection = getApplicationContext().getString(R.string.sort_popular);

                mbfavmovies = false;

                getMovies(mUserSelection);

                Toast.makeText(getApplicationContext(), getString(R.string.nofavsselected), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
