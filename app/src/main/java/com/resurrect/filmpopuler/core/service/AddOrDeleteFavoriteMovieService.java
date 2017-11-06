package com.resurrect.filmpopuler.core.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.resurrect.filmpopuler.data.dblite.DatabaseHelper;
import com.resurrect.filmpopuler.data.dblite.contract.MoviesRepository;
import com.resurrect.filmpopuler.data.model.Movies;

import static com.resurrect.filmpopuler.activity.detail.contract.AddOrDeleteFavoriteMovieContract.ACTION_ADD_FAVORITE;
import static com.resurrect.filmpopuler.activity.detail.contract.AddOrDeleteFavoriteMovieContract.ACTION_DELETE_FAVORITE;
import static com.resurrect.filmpopuler.activity.detail.contract.AddOrDeleteFavoriteMovieContract.EXTRA_MOVIE_DATA;
import static com.resurrect.filmpopuler.activity.detail.contract.AddOrDeleteFavoriteMovieContract.EXTRA_MOVIE_ID;

/**
 * Created by yogaagungk on 31/10/17.
 */

public class AddOrDeleteFavoriteMovieService extends IntentService {

    private static final String TAG = "AddOrDeleteFavoriteMovi";
    private MoviesRepository moviesRepository;
    
    public AddOrDeleteFavoriteMovieService(){super(TAG);}
    public AddOrDeleteFavoriteMovieService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        moviesRepository = DatabaseHelper.getInstance(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.d(TAG, "onHandleIntent: " + action);
            if (ACTION_ADD_FAVORITE.equals(action)) {
                Movies movies = intent.getParcelableExtra(EXTRA_MOVIE_DATA);
                if (movies != null) {
                    moviesRepository.addFavoriteMovie(movies);
                }
            } else if (ACTION_DELETE_FAVORITE.equals(action)) {
                String movieID = intent.getStringExtra(EXTRA_MOVIE_ID);
                if (!TextUtils.isEmpty(movieID)) {
                    moviesRepository.removeFavoriteMovie(movieID);
                }
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(action));
        }
    }
}
