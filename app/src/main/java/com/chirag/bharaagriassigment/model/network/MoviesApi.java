package com.chirag.bharaagriassigment.model.network;

import com.chirag.bharaagriassigment.model.entities.GenreListResponse;
import com.chirag.bharaagriassigment.model.entities.MovieListResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {

    //Get Movies
    @GET("movie/{type}")
    Single<MovieListResponse> getMoviesList(@Path("type") String type, @Query("api_key") String api_key);

    //Get Genres
    @GET("genre/movie/list")
    Single<GenreListResponse> getGenresList(@Query("api_key") String api_key);
}
