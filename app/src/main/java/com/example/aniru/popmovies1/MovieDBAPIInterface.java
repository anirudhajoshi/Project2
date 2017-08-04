package com.example.aniru.popmovies1;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by aniru on 7/15/2017.
 */

// Interface to get the movies based on the what the user has selected
interface MovieDBAPIInterface {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter
    // Get movies by user picked order - popular or top rated
    @GET("{movies_order}")
    Call<Movies> getMovies(@Path("movies_order") String movies_order, @Query("api_key") String apikey);

    // Get the selected movies' trailers
    @GET("{movie_id}/videos")
    Call<MovieTrailers> getMovieTrailers(@Path("movie_id") String movie_id, @Query("api_key") String apikey);

    // Get the selected movies' reviews
    @GET("{movie_id}/reviews")
    Call<MovieReviews> getMovieReviews(@Path("movie_id") String movie_id, @Query("api_key") String apikey);


}
