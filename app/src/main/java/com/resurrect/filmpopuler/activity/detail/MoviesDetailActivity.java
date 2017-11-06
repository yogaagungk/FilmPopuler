package com.resurrect.filmpopuler.activity.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.resurrect.filmpopuler.R;
import com.resurrect.filmpopuler.activity.detail.contract.AddOrDeleteFavoriteMovieContract;
import com.resurrect.filmpopuler.core.service.AddOrDeleteFavoriteMovieService;
import com.resurrect.filmpopuler.data.api.ApiService;
import com.resurrect.filmpopuler.data.dblite.DatabaseHelper;
import com.resurrect.filmpopuler.data.dblite.contract.MoviesRepository;
import com.resurrect.filmpopuler.data.model.Movies;
import com.resurrect.filmpopuler.data.model.MoviesDetail;
import com.resurrect.filmpopuler.core.util.AnimationUtil;
import com.resurrect.filmpopuler.activity.detail.loader.FavoriteMovieLoader;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 25/10/17.
 */
public class MoviesDetailActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String KEY_MOVIE_ID = "MOVIE_ID";
    public static final String KEY_MOVIE_TITLE = "MOVIE_TITLE";
    public static final String KEY_POSTER_PATH = "POSTER_PATH";
    public static final String KEY_MOVIE_DATA = "MOVIE_DATA";
    private static final String TAG = "MovieDetailActivity";

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.75f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.75f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private static final float SCRIM_ADJUSTMENT = 0.075f;

    TextView movieTitleField;
    TextView movieRatingField;
    TextView movieReleaseDateField;
    TextView movieDetailTaglineField;
    TextView movieDetailDurationField;
    TextView movieDetailOverviewField;
    TextView toolbarTitleView;

    ImageView moviePosterView;
    ImageView movieBackdropView;

    FloatingActionButton fabFavourite;

    Toolbar toolbar;

    ViewGroup detailWrapperView;

    String movieId;
    String movieTitle;
    String posterPath;

    Movies movieData;
    MoviesDetail movieDetail;

    AppBarLayout appBarLayout;

    SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyy", Locale.getDefault());
    private boolean isTitleVisible = false;
    private boolean isTitleContainerVisible = true;
    private boolean favored = false;

    AddOrDeleteBroadcastReceiver addOrDeleteBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        viewBinding();
        processIntent();

        setUpToolbar();
        setUpDetails();
        setUpMovieDetail();

        addOrDeleteBroadcastReceiver = new AddOrDeleteBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AddOrDeleteFavoriteMovieContract.ACTION_ADD_FAVORITE);
        intentFilter.addAction(AddOrDeleteFavoriteMovieContract.ACTION_DELETE_FAVORITE);
        LocalBroadcastManager.getInstance(this).registerReceiver(addOrDeleteBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(addOrDeleteBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    LoaderManager.LoaderCallbacks<Boolean> favoriteCallback = new LoaderManager.LoaderCallbacks<Boolean>(){
        @Override
        public Loader<Boolean> onCreateLoader(int id, Bundle args) {
            return new FavoriteMovieLoader(MoviesDetailActivity.this, movieId);
        }

        @Override
        public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
            enableFavoriteButton();
            favored = data;
            if (favored) {
                setFavoriteImage();
            } else{
                setNonFavoriteImage();
            }
        }

        @Override
        public void onLoaderReset(Loader<Boolean> loader) {

        }
    };

    public static void showMovieDetailPage(Context context, String movieId,
                                           String movieTitle, String posterPath,
                                           Parcelable parcelableMovieData) {
        Intent detailIntent = new Intent(context, MoviesDetailActivity.class);
        detailIntent.putExtra(KEY_MOVIE_ID, movieId);
        detailIntent.putExtra(KEY_MOVIE_TITLE, movieTitle);
        detailIntent.putExtra(KEY_POSTER_PATH, posterPath);
        detailIntent.putExtra(KEY_MOVIE_DATA, parcelableMovieData);
        context.startActivity(detailIntent);
        Log.d(TAG, "showMovieDetailPage: ");
    }

    private void processIntent() {
        if (getIntent() != null) {
            movieId = getIntent().getStringExtra(KEY_MOVIE_ID);
            movieTitle = getIntent().getStringExtra(KEY_MOVIE_TITLE);
            posterPath = getIntent().getStringExtra(KEY_POSTER_PATH);
            movieData = getIntent().getParcelableExtra(KEY_MOVIE_DATA);
            Log.d(TAG, "processIntent: ");
        }
    }

    private void viewBinding() {
        toolbar = (Toolbar) findViewById(R.id.movie_detail_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.movie_detail_appbar);

        toolbarTitleView = (TextView) findViewById(R.id.movie_detail_toolbar_title);
        movieTitleField = (TextView) findViewById(R.id.movie_detail_title);
        movieRatingField = (TextView) findViewById(R.id.movie_detail_ratings);
        movieReleaseDateField = (TextView) findViewById(R.id.movie_detail_release_date);
        movieDetailTaglineField = (TextView) findViewById(R.id.movie_detail_tagline_field);
        movieDetailDurationField = (TextView) findViewById(R.id.movie_detail_duration_field);
        movieDetailOverviewField = (TextView) findViewById(R.id.movie_detail_overview_field);

        detailWrapperView = (ViewGroup) findViewById(R.id.movie_detail_wrapper);

        moviePosterView = (ImageView) findViewById(R.id.movie_detail_poster_image);
        movieBackdropView = (ImageView) findViewById(R.id.movie_detail_backdrop_image);

        fabFavourite = (FloatingActionButton) findViewById(R.id.movie_detail_favorite_button);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appBarLayout.addOnOffsetChangedListener(this);
        getSupportActionBar().setTitle("");
        toolbarTitleView.setText(movieTitle);
    }

    private void setUpDetails() {
        Log.d(TAG, "setUpDetails: ");
        movieTitleField.setText(movieTitle);
        if (movieData != null) {
            movieDetailOverviewField.setText(movieData.getOverview());
            Date movieReleaseDate;
            try {
                movieReleaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                        .parse(movieData.getReleaseDate());
            } catch (Exception e) {
                movieReleaseDate = Calendar.getInstance().getTime();
            }
            movieReleaseDateField.setText(String.format(
                    "Release date : %s",
                    dateFormat.format(movieReleaseDate))
            );
            movieRatingField.setText(String.format(Locale.getDefault(), "%.1f", movieData.getVoteAverage()));
            Picasso.with(this)
                    .load(IMAGE_BASE_URL + "w342/" + movieData.getPosterPath())
                    .into(moviePosterView);
            Picasso.with(this)
                    .load(IMAGE_BASE_URL + "w780/" + movieData.getBackdropPath())
                    .into(movieBackdropView);
        }
        disableFavoriteButton();
        getSupportLoaderManager().initLoader(
                FavoriteMovieLoader.FAVORITE_LOADER_ID,
                null,
                favoriteCallback
        );
        fabFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent favoriteIntent = new Intent(MoviesDetailActivity.this, AddOrDeleteFavoriteMovieService.class);
              if (favored){
                  favoriteIntent.setAction(AddOrDeleteFavoriteMovieContract.ACTION_DELETE_FAVORITE);
                  favoriteIntent.putExtra(AddOrDeleteFavoriteMovieContract.EXTRA_MOVIE_ID, movieId);
              }else if (favored == false){
                  favoriteIntent.setAction(AddOrDeleteFavoriteMovieContract.ACTION_ADD_FAVORITE);
                  favoriteIntent.putExtra(AddOrDeleteFavoriteMovieContract.EXTRA_MOVIE_DATA, movieData);
              }
              startService(favoriteIntent);
              disableFavoriteButton();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        // code from https://github.com/saulmm/CoordinatorBehaviorExample
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void getMovieDetail() {
        Call<MoviesDetail> call = ApiService.open().getMovieDetail(movieId);
        call.enqueue(new Callback<MoviesDetail>() {
            @Override
            public void onResponse(Call<MoviesDetail> call, Response<MoviesDetail> response) {
                movieDetail = response.body();
                setUpMovieDetail();
            }

            @Override
            public void onFailure(Call<MoviesDetail> call, Throwable throwable) {
                // do nothing
                getMovieDetail();
            }
        });
    }

    private void setUpMovieDetail() {
        if (movieDetail != null) {
            movieDetailDurationField.setText(String.format(
                    Locale.getDefault(),
                    "%d minute(s)",
                    movieDetail.getRuntime())
            );
            movieDetailTaglineField.setText(TextUtils.isEmpty(movieDetail.getTagline())
                    ? "-" : movieDetail.getTagline());
        } else {
            getMovieDetail();
        }
    }

    private void setNonFavoriteImage() {
        fabFavourite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
    }

    private void setFavoriteImage() {
        fabFavourite.setImageResource(R.drawable.ic_favorite_white_24dp);
    }

    // modified code from https://github.com/saulmm/CoordinatorBehaviorExample
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!isTitleVisible) {
                AnimationUtil.startAlphaAnimation(
                        toolbarTitleView,
                        ALPHA_ANIMATIONS_DURATION,
                        View.VISIBLE
                );
                changeToolbarColorAlpha(255);
                isTitleVisible = true;
            }
        } else {
            if (isTitleVisible) {
                AnimationUtil.startAlphaAnimation(
                        toolbarTitleView,
                        ALPHA_ANIMATIONS_DURATION,
                        View.INVISIBLE
                );
                changeToolbarColorAlpha(0);
                isTitleVisible = false;
            }
        }
    }

    private void changeToolbarColorAlpha(int alpha) {
        if (toolbar.getBackground() != null) {
            toolbar.getBackground().setAlpha(alpha);
        }
    }

    // modified code from https://github.com/saulmm/CoordinatorBehaviorExample
    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (isTitleContainerVisible) {
                AnimationUtil.startAlphaAnimation(
                        detailWrapperView,
                        ALPHA_ANIMATIONS_DURATION,
                        View.INVISIBLE
                );
                isTitleContainerVisible = false;
            }
        } else {
            if (!isTitleContainerVisible) {
                AnimationUtil.startAlphaAnimation(
                        detailWrapperView,
                        ALPHA_ANIMATIONS_DURATION,
                        View.VISIBLE
                );
                isTitleContainerVisible = true;
            }
        }
    }

    private void enableFavoriteButton() {
        if (fabFavourite != null) {
            fabFavourite.setEnabled(true);
        }
    }

    private void disableFavoriteButton() {
        if (fabFavourite != null) {
            fabFavourite.setEnabled(false);
        }
    }


    private class AddOrDeleteBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!=null){
                final String action = intent.getAction();
                Log.d(TAG, "onReceive: " + action);
                enableFavoriteButton();
                if (AddOrDeleteFavoriteMovieContract.ACTION_ADD_FAVORITE.equals(action)){
                    favored = true;
                    setFavoriteImage();
                }else if (AddOrDeleteFavoriteMovieContract.ACTION_DELETE_FAVORITE.equals(action)){
                    favored = false;
                    setNonFavoriteImage();
                }
            }else{
                Log.d(TAG, "onFailure: ");
            }
        }
    }

    private class AddOrRemoveFavorite extends AsyncTask<Movies, Void, Boolean> {
        MoviesRepository moviesRepository;

        AddOrRemoveFavorite(Context context){
            moviesRepository = DatabaseHelper.getInstance(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (fabFavourite!=null){
                fabFavourite.setEnabled(false);
            }
        }

        @Override
        protected Boolean doInBackground(Movies... params) {
            boolean result = false;
            Movies data = params[0];
            if (favored){
                moviesRepository.removeFavoriteMovie(String.valueOf(data.getId()));
            }else{
                moviesRepository.addFavoriteMovie(data);
                result = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            favored = aBoolean;
            if(fabFavourite!=null){
                fabFavourite.setEnabled(true);
            }
            if (aBoolean) setFavoriteImage();
            else setNonFavoriteImage();
        }
    }
}