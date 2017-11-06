package com.resurrect.filmpopuler.data.api;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 22/10/17.
 */

public class ApiService {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static final String API_KEY = "7ce003b209958b8de9fb0acfb251b199";

    private static Gson gson;
    private static OkHttpClient client;

    private ApiService(){

    }

    private static Gson getGson(){
        if(gson==null){
            gson = new Gson();
        }
        return gson;
    }

    @NonNull
    private static OkHttpClient getHttpClient(){
        if(client==null){
            client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            HttpUrl url = request.url().newBuilder()
                                    .addQueryParameter("api_key",API_KEY)
                                    .build();
                            request = request.newBuilder().url(url).build();

                            return chain.proceed(request);
                        }
                    }).build();
        }
        return client;
    }

    public static ApiInterface open(){
        OkHttpClient client = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build();

        return retrofit.create(ApiInterface.class);
    }
}
