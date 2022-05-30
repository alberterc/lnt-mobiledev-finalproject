package com.finproj.movieapp.localstorage.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteMovie {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "uid")
    public String uid;

    @ColumnInfo(name = "fav_movie_id")
    public String favMovieId;
}
