package com.resurrect.filmpopuler.activity.main.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.resurrect.filmpopuler.R;
import com.resurrect.filmpopuler.core.contract.MovieItemClickListener;
import com.resurrect.filmpopuler.activity.main.store.MainItem;
import com.resurrect.filmpopuler.activity.main.store.StandardMovieItem;
import com.squareup.picasso.Picasso;

/**
 * Created by root on 24/10/17.
 */

public class MoviesViewHolder extends BaseViewHolder {

    private static final String SMALL_POSTER_SIZE = "w342/";
    private static final String MEDIUM_POSTER_SIZE = "w500/";

    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    private ImageView imageView;
    private MovieItemClickListener listener;
    private String imageSize;

    public MoviesViewHolder(View itemView, MovieItemClickListener listener) {
        super(itemView);
        this.listener = listener;

        setupViews(itemView);
    }

    private void setupViews(View itemView){
        imageView = (ImageView) itemView.findViewById(R.id.img_photo);
        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final int pos = getAdapterPosition();
                if (listener != null & pos != RecyclerView.NO_POSITION){
                    listener.onMoviesItemClicked(pos);
                }
            }
        });
    }

    private void setImageSize(int position) {
        imageSize = positionDivisibleByFive(position) ? MEDIUM_POSTER_SIZE : SMALL_POSTER_SIZE;
    }

    private boolean positionDivisibleByFive(int position) {
        return position != RecyclerView.NO_POSITION && (position % 5 == 0);
    }

    @Override
    public void bindView(MainItem item) {
        StandardMovieItem movieItem = (StandardMovieItem) item;
        setImageSize(getAdapterPosition());
        Picasso.with(itemView.getContext())
                .load(IMAGE_BASE_URL + imageSize + movieItem.getPosterPath())
                .into(imageView);
    }
}