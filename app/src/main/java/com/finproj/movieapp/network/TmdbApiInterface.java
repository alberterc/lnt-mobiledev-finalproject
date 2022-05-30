package com.finproj.movieapp.network;

import com.finproj.movieapp.model.MovieItem;
import com.finproj.movieapp.model.Movies;
import com.finproj.movieapp.model.TVs;
import com.finproj.movieapp.model.TvItem;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TmdbApiInterface {

    @GET("movie/now_playing")
    Call<Movies> getMovieNowPlaying();

    @GET("movie/")
    Call<MovieItem> getMovieDetails();

    @GET("tv/on_the_air")
    Call<TVs> getTvOnTheAir();

    @GET("tv/")
    Call<TvItem> getTvDetails();
}


