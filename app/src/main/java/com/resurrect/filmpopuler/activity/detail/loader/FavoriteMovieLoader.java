package com.resurrect.filmpopuler.activity.detail.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.resurrect.filmpopuler.data.dblite.DatabaseHelper;
import com.resurrect.filmpopuler.data.dblite.contract.MoviesRepository;

/**
 * Created by root on 26/10/17.
 */

public class FavoriteMovieLoader extends AsyncTaskLoader<Boolean> {

    public static final int FAVORITE_LOADER_ID = 111;
    private static final String TAG = "FavoriteMovieLoader";
    private MoviesRepository movieRepository;
    private Boolean favorite = null;
    private String movieID;

    public FavoriteMovieLoader(Context context, String movieID) {
        super(context);
        movieRepository = DatabaseHelper.getInstance(context);
        this.movieID = movieID;
    }

    @Override
    protected void onStartLoading(){
        super.onStartLoading();
        if (favorite==null){
            forceLoad();
        }else {
            deliverResult(favorite);
        }
    }
    @Override
    public Boolean loadInBackground() {
        try{
            favorite = movieRepository.isMovieFavored(movieID);
        }catch (Exception e){
            Log.e(TAG, "loadInBackground: ", e);
        }
        return favorite;
    }
}