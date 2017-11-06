package com.resurrect.filmpopuler.activity.main.store;

import com.resurrect.filmpopuler.data.model.Movies;

/**
 * Created by root on 22/10/17.
 */

public interface MoviesItem extends MainItem{

    String getMovieId();

    String getMovieTitle();

    String getPosterPath();

    Movies getMovieData();
}
