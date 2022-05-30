package com.finproj.movieapp.home;

import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.finproj.movieapp.R;
import com.finproj.movieapp.home.favorite.FavoriteMovieActivity;
import com.finproj.movieapp.home.favorite.FavoriteTvShowActivity;

public class UserFavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_favorite);

        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> onBackPressed());

        ConstraintLayout favMovieButton = findViewById(R.id.fav_movie_button);
        ConstraintLayout favTvShowButton = findViewById(R.id.fav_tvshow_button);

        favMovieButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FavoriteMovieActivity.class)));

        favTvShowButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FavoriteTvShowActivity.class)));
    }
}