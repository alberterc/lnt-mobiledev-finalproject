package com.finproj.movieapp.localstorage.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.finproj.movieapp.localstorage.entity.FavoriteTvShow;

import java.util.List;

@Dao
public interface FavoriteTvShowDao {
    @Query("SELECT * FROM favoritetvshow WHERE uid = :curr_uid")
    List<FavoriteTvShow> getFavoriteTvShow(String curr_uid);

    @Query("SELECT * FROM favoritetvshow WHERE fav_tvshow_id = :tvShow_id AND uid = :curr_uid")
    List<FavoriteTvShow> checkFavoritedTvShow(String tvShow_id, String curr_uid);

    @Query("DELETE FROM favoritetvshow WHERE uid = :curr_uid")
    void deleteAllFavoriteTvShowFromAccount(String curr_uid);

    @Query("SELECT id FROM favoritetvshow WHERE fav_tvshow_id = :tvShow_id AND uid = :curr_uid")
    Integer getPrimaryKeyId(String tvShow_id, String curr_uid);

    @Insert
    void addFavTvShow(FavoriteTvShow... userFav);

    @Delete
    void removeFavTvShow(FavoriteTvShow userFav);
}
