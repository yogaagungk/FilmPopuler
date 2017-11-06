package com.resurrect.filmpopuler.activity.main.store;

import com.resurrect.filmpopuler.R;
import com.resurrect.filmpopuler.data.model.Movies;

/**
 * Created by root on 25/10/17.
 */

public class BigMovieItem implements MoviesItem {
    private final String movieId;
    private final String movieTitle;
    private final String posterPath;
    private final String overview;
    private final Movies movieData;

    public BigMovieItem(String movieId, String movieTitle, String posterPath, String overview, Movies movieData) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.posterPath = posterPath;
        this.overview = overview;
        this.movieData = movieData;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    @Override
    public Movies getMovieData() {
        return movieData;
    }

    public String getOverview() {
        return overview;
    }

    @Override
    public int getType() {
        return R.layout.main_big_movie_item;
    }

    @Override
    public int getItemSize() {
        return 2;
    }
}
