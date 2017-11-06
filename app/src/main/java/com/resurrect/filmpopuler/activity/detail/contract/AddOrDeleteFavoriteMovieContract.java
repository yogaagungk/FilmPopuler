package com.resurrect.filmpopuler.activity.detail.contract;

/**
 * Created by yogaagungk on 31/10/17.
 */

public class AddOrDeleteFavoriteMovieContract {

    public static final String ACTION_FAVORITE_MOVIE_DATA_CHANGED= "com.resurrect.filmpopuler.activity.detail.contract.ACTION_FAVORITE_MOVIE_DATA_CHANGED";

    public static final String ACTION_ADD_FAVORITE = "com.resurrect.filmpopuler.activity.detail.contract.ACTION_ADD_FAVORITE";
    public static final String ACTION_DELETE_FAVORITE = "com.resurrect.filmpopuler.activity.detail.contract.ACTION_DELETE_FAVORITE";


    public static final String EXTRA_MOVIE_DATA = "EXTRA_MOVIE_DATA";
    public static final String EXTRA_MOVIE_ID= "EXTRA_MOVIE_ID";

    private AddOrDeleteFavoriteMovieContract(){}
}
