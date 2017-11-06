package com.resurrect.filmpopuler.activity.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resurrect.filmpopuler.R;
import com.resurrect.filmpopuler.core.contract.MainListItemClickListener;
import com.resurrect.filmpopuler.core.contract.MovieItemClickListener;
import com.resurrect.filmpopuler.activity.main.store.MainItem;
import com.resurrect.filmpopuler.activity.main.store.MoviesItem;
import com.resurrect.filmpopuler.activity.main.view.BaseViewHolder;
import com.resurrect.filmpopuler.activity.main.view.BigMovieViewHolder;
import com.resurrect.filmpopuler.activity.main.view.HeaderViewHolder;
import com.resurrect.filmpopuler.activity.main.view.MoviesViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 22/10/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<MainItem> moviesItemList;
    private MainListItemClickListener mainListItemClickListener;
    private MovieItemClickListener movieItemClickListener;

    public MoviesAdapter(MainListItemClickListener mainListItemClickListener) {
        this.mainListItemClickListener = mainListItemClickListener;
        this.moviesItemList = new ArrayList<>();
        setupMovieItemClickListener();
    }

    private void setupMovieItemClickListener(){
        movieItemClickListener = new MovieItemClickListener() {
            @Override
            public void onMoviesItemClicked(int position) {
                MainItem item = moviesItemList.get(position);
                if (mainListItemClickListener!=null){
                    mainListItemClickListener.onMovieItemClick((MoviesItem) item);
                }
            }
        };
    }

    public int getItemViewType(int pos){
        return moviesItemList.get(pos).getType();
    }

    public void setMainItemList(List<MainItem> moviesItemList) {
        this.moviesItemList = moviesItemList;
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(viewType, parent, false);
        if (viewType == R.layout.movie_content) {
            return new MoviesViewHolder(view, movieItemClickListener);
        } else if (viewType == R.layout.main_big_movie_item) {
            return new BigMovieViewHolder(view, movieItemClickListener);
        } else if (viewType == R.layout.main_header_item) {
            return new HeaderViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindView(moviesItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return moviesItemList.size();
    }

    public int getItemSize(int position) {
        if (position >= moviesItemList.size() || position < 0) {
            return 0;
        } else {
            return moviesItemList.get(position).getItemSize();
        }
    }
}
