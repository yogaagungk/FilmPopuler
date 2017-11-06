package com.resurrect.filmpopuler.core.util;

import android.support.annotation.NonNull;

import com.resurrect.filmpopuler.activity.main.store.BigMovieItem;
import com.resurrect.filmpopuler.activity.main.store.HeaderItem;
import com.resurrect.filmpopuler.activity.main.store.MainItem;
import com.resurrect.filmpopuler.data.model.Movies;
import com.resurrect.filmpopuler.activity.main.store.StandardMovieItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 22/10/17.
 */

public class StoreToMoviesItem {

    public static List<MainItem> getMainItemList(String headerTitle, @NonNull List<Movies> moviesDataList){
        List<MainItem> results = new ArrayList<>();
        int pos = 0;
        results.add(new HeaderItem(headerTitle));
        for (Movies movies : moviesDataList){
            results.add(itemCreator(movies,pos));
            pos++;
        }
        return results;
    }

    static MainItem itemCreator(Movies movieData, int position) {
        if (position % 7 == 0) {
            return new BigMovieItem(
                    String.valueOf(movieData.getId()),
                    movieData.getTitle(),
                    movieData.getPosterPath(),
                    movieData.getOverview(),
                    movieData);
        } else {
            return new StandardMovieItem(
                    String.valueOf(movieData.getId()),
                    movieData.getTitle(),
                    movieData.getPosterPath(),
                    movieData);
        }
    }
}
