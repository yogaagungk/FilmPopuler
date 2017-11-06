package com.resurrect.filmpopuler.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.resurrect.filmpopuler.R;
import com.resurrect.filmpopuler.activity.detail.MoviesDetailActivity;
import com.resurrect.filmpopuler.activity.main.adapter.MoviesAdapter;
import com.resurrect.filmpopuler.activity.detail.contract.AddOrDeleteFavoriteMovieContract;
import com.resurrect.filmpopuler.activity.main.loader.FavoriteMovieListLoader;
import com.resurrect.filmpopuler.activity.main.store.MainItem;
import com.resurrect.filmpopuler.data.api.ApiService;
import com.resurrect.filmpopuler.core.contract.MainListItemClickListener;
import com.resurrect.filmpopuler.data.model.Movies;
import com.resurrect.filmpopuler.data.model.MoviesList;
import com.resurrect.filmpopuler.activity.main.store.MoviesItem;
import com.resurrect.filmpopuler.core.util.StoreToMoviesItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final int STATE_POPULAR_MOVIE = 1;
    public static final int STATE_FAVORITE_MOVIE = 2;
    public static final int STATE_HIGHEST_MOVIE = 3;
    public static final String CURRENT_STATE = "CURRENT_STATE";

    int currentState = STATE_POPULAR_MOVIE;

    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<MoviesItem> moviesItemList;
    private GridLayoutManager layoutManager;
    private MainListItemClickListener mainListItemClickListener;
    private ImageView imgBroken;

    private FavoriteBroadcastReceiver favoriteBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null){
            currentState = savedInstanceState.getInt(CURRENT_STATE, STATE_POPULAR_MOVIE);
        }

        moviesItemList = new ArrayList<>();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerMovie);
        imgBroken = (ImageView) findViewById(R.id.img_broken);

        setUpMainListView();

        if (currentState == STATE_POPULAR_MOVIE){
            fetchPopularMovie();
        }else if(currentState == STATE_FAVORITE_MOVIE){
            fetchFavoriteMovie();
        }else if (currentState == STATE_HIGHEST_MOVIE){
            fetchActionMovie();
        }

        favoriteBroadcastReceiver = new FavoriteBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AddOrDeleteFavoriteMovieContract.ACTION_ADD_FAVORITE);
        intentFilter.addAction(AddOrDeleteFavoriteMovieContract.ACTION_DELETE_FAVORITE);
        LocalBroadcastManager.getInstance(this).registerReceiver(favoriteBroadcastReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(favoriteBroadcastReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_STATE,currentState);
    }

    private void setUpMainListView() {
        mainListItemClickListener = new MainListItemClickListener() {
            @Override
            public void onMovieItemClick(MoviesItem movieItem) {
                MoviesDetailActivity.showMovieDetailPage(
                        MainActivity.this,
                        movieItem.getMovieId(),
                        movieItem.getMovieTitle(),
                        movieItem.getPosterPath(),
                        movieItem.getMovieData()
                );
            }
        };
        adapter = new MoviesAdapter(mainListItemClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position != RecyclerView.NO_POSITION ? adapter.getItemSize(position) : 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final int menuID = item.getItemId();
                if (menuID == R.id.action_popular){
                    currentState = STATE_POPULAR_MOVIE;
                    fetchPopularMovie();
                }else if (menuID == R.id.action_favorite){
                    currentState = STATE_FAVORITE_MOVIE;
                    fetchFavoriteMovie();
                }else if (menuID == R.id.action_highest){
                    currentState = STATE_HIGHEST_MOVIE;
                    fetchActionMovie();
                }
                return true;
            }
        });
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });
    }

    private void showMovieList(List<MainItem> mainItemList) {
        adapter.setMainItemList(mainItemList);
        recyclerView.setVisibility(View.VISIBLE);
        imgBroken.setVisibility(View.GONE);
    }

    private void hideMovieList() {
        recyclerView.setVisibility(View.GONE);
        imgBroken.setVisibility(View.VISIBLE);
    }
    private void emptyList(){
        adapter.setMainItemList(Collections.<MainItem>emptyList());
        hideMovieList();
    }

    private void fetchPopularMovie(){
        hideMovieList();
        Call<MoviesList> call = ApiService.open().getMostPopularMovies(1);
        call.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                MoviesList data = response.body();
                if(data!=null){
                    List<Movies> movies = data.getResults();
                    if(movies!=null && !movies.isEmpty()) {
                        showMovieList(StoreToMoviesItem.getMainItemList("Popular Movie", movies));
                    }else {
                        emptyList();
                    }
                    Log.d("success","Ambil data dari API untuk moviePopular");
                }
            }
            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Log.d("error","Gagal ambil data dari API untuk moviePopular");
            }
        });
    }

    private void fetchFavoriteMovie() {
        hideMovieList();
        getSupportLoaderManager().restartLoader(
                FavoriteMovieListLoader.FAVORITE_MOVIE_LIST_ID,
                null,
                favoriteLoaderCallback
        );
    }

    private void fetchActionMovie(){
        hideMovieList();
        Call<MoviesList> call = ApiService.open().getActionThisYear(28,2017);
        call.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                MoviesList data = response.body();
                if(data!=null){
                    List<Movies> movies = data.getResults();
                    if(movies!=null && !movies.isEmpty()) {
                        showMovieList(StoreToMoviesItem.getMainItemList("2017 Action Movie", movies));
                    }else {
                        emptyList();
                    }
                    Log.d("success","Ambil data dari API untuk actionMovie");
                }
            }
            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Log.d("error","Gagal ambil data dari API untuk actionMovie");
            }
        });
    }

    LoaderManager.LoaderCallbacks<List<Movies>> favoriteLoaderCallback = new LoaderManager.LoaderCallbacks<List<Movies>>() {
       @Override
        public Loader onCreateLoader(int id, Bundle args) {
            return new FavoriteMovieListLoader(MainActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<List<Movies>> loader, List<Movies> data) {
            if (data!=null && !data.isEmpty()){
                showMovieList(StoreToMoviesItem.getMainItemList("Favorite Movies", data));
            }else {
                emptyList();
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Movies>> loader) {

        }
    };

    class FavoriteBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!=null){
                final String action = intent.getAction();
                if (action.equals(AddOrDeleteFavoriteMovieContract.ACTION_ADD_FAVORITE) || action.equals(AddOrDeleteFavoriteMovieContract.ACTION_DELETE_FAVORITE)){
                    if (currentState == STATE_FAVORITE_MOVIE){
                        getSupportLoaderManager().restartLoader(
                                FavoriteMovieListLoader.FAVORITE_MOVIE_LIST_ID,
                                null,
                                favoriteLoaderCallback
                        );
                    }
                }
            }
        }
    }
}