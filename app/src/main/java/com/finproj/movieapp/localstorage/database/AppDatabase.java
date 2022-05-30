package com.finproj.movieapp.localstorage.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.finproj.movieapp.localstorage.dao.FavoriteMovieDao;
import com.finproj.movieapp.localstorage.dao.FavoriteTvShowDao;
import com.finproj.movieapp.localstorage.entity.FavoriteMovie;
import com.finproj.movieapp.localstorage.entity.FavoriteTvShow;

@Database(entities = {FavoriteMovie.class, FavoriteTvShow.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoriteMovieDao favoriteMovieDao();
    public abstract FavoriteTvShowDao favoriteTvShowDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}

