package com.resurrect.filmpopuler.activity.main.store;

import com.resurrect.filmpopuler.R;
import com.resurrect.filmpopuler.data.model.Movies;

/**
 * Created by root on 25/10/17.
 */

public class StandardMovieItem implements MoviesItem {
    private final String movieId;
    private final String movieTitle;
    private final String posterPath;
    private final Movies movieData;

    public StandardMovieItem(String movieId, String movieTitle, String posterPath, Movies movieData) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.posterPath = posterPath;
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

    @Override
    public int getType() {
        return R.layout.movie_content;
    }

    @Override
    public int getItemSize() {
        return 1;
    }

    @Override
    public String toString() {
        return "StandardMovieItem{" +
                "movieId='" + movieId + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", movieData=" + movieData +
                '}';
    }
}
