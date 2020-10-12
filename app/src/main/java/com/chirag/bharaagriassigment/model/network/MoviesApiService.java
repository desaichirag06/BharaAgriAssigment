package com.chirag.bharaagriassigment.model.network;

import com.chirag.bharaagriassigment.model.entities.GenreListResponse;
import com.chirag.bharaagriassigment.model.entities.MovieListResponse;
import com.chirag.bharaagriassigment.utilities.Constants;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesApiService {

    private MoviesApi api;

    public MoviesApiService() {

        api = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MoviesApi.class);
    }

    public Single<MovieListResponse> getMovies(String type, String api_key) {
        return api.getMoviesList(type, api_key);
    }

    public Single<GenreListResponse> getGenres(String api_key) {
        return api.getGenresList(api_key);
    }
}
