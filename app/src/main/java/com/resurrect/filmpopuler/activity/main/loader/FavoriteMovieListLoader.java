package com.resurrect.filmpopuler.activity.main.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.resurrect.filmpopuler.data.dblite.DatabaseHelper;
import com.resurrect.filmpopuler.data.dblite.contract.MoviesRepository;
import com.resurrect.filmpopuler.data.model.Movies;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yogaagungk on 02/11/17.
 */

public class FavoriteMovieListLoader extends AsyncTaskLoader<List<Movies>> {
    public static final int FAVORITE_MOVIE_LIST_ID = 511;
    private static final String TAG = "FavoriteMovieListLoader";
    MoviesRepository moviesRepository;
    List<Movies> movies;

    public FavoriteMovieListLoader(Context context){
        super(context);
        moviesRepository = DatabaseHelper.getInstance(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (movies!=null){
            deliverResult(movies);
        }else {
            forceLoad();
        }
    }

    @Override
    public List<Movies> loadInBackground() {
        List<Movies> moviesList = new ArrayList<>();
        try {
            moviesList.addAll(moviesRepository.getFavoriteMovie());
        }catch (Exception e){
            Log.e(TAG, "loadInBackground: ",e);
        }
        return moviesList;
    }

    @Override
    public void deliverResult(List<Movies> data) {
        movies = data;
        super.deliverResult(data);
    }
}
