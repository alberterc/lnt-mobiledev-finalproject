package com.finproj.movieapp.localstorage.dao;

import androidx.room.*;
import com.finproj.movieapp.localstorage.entity.FavoriteMovie;

import java.util.List;

@Dao
public interface FavoriteMovieDao {
    @Query("SELECT * FROM favoritemovie WHERE uid = :curr_uid")
    List<FavoriteMovie> getFavoriteMovie(String curr_uid);

    @Query("SELECT * FROM favoritemovie WHERE fav_movie_id = :movie_id AND uid = :curr_uid")
    List<FavoriteMovie> checkFavoritedMovie(String movie_id, String curr_uid);

    @Query("DELETE FROM favoritemovie WHERE uid = :curr_uid")
    void deleteAllFavoriteMovieFromAccount(String curr_uid);

    @Query("SELECT id FROM favoritemovie WHERE fav_movie_id = :movie_id AND uid = :curr_uid")
    Integer getPrimaryKeyId(String movie_id, String curr_uid);

    @Insert
    void addFavMovie(FavoriteMovie... userFav);

    @Delete
    void removeFavMovie(FavoriteMovie userFav);
}
