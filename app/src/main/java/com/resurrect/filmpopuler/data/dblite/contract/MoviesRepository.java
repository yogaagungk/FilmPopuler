package com.resurrect.filmpopuler.data.dblite.contract;

import com.resurrect.filmpopuler.data.model.Movies;

import java.util.List;

/**
 * Created by root on 26/10/17.
 */

public interface MoviesRepository {
    List<Movies> getFavoriteMovie();

    void addFavoriteMovie(Movies movieData);

    boolean isMovieFavored(String movieId);

    void updateFavoriteMovie(Movies movieData);

    void removeFavoriteMovie(String movieId);
}
