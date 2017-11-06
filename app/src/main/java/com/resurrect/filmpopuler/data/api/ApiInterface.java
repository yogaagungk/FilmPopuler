package com.resurrect.filmpopuler.data.api;

import com.resurrect.filmpopuler.data.model.MoviesDetail;
import com.resurrect.filmpopuler.data.model.MoviesList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by root on 22/10/17.
 */

public interface ApiInterface {
    @GET("movie/popular")
    Call<MoviesList> getMostPopularMovies(@Query("page") int page);

    @GET("movie/{movie_id}")
    Call<MoviesDetail> getMovieDetail(@Path("movie_id") String movieId);

    @GET("discover/movie")
    Call<MoviesList> getActionThisYear(@Query("with_genres") int genre, @Query("primary_release_year") int year);
}
