package com.finproj.movieapp.home.favorite;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.finproj.movieapp.R;
import com.finproj.movieapp.helper.DataHolder;
import com.finproj.movieapp.home.favorite.adapter.FavoriteMovieAdapter;
import com.finproj.movieapp.localstorage.database.AppDatabase;
import com.finproj.movieapp.localstorage.entity.FavoriteMovie;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;
import java.util.Objects;

public class FavoriteMovieActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private FavoriteMovieAdapter favoriteMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movie);

        Button backButton = findViewById(R.id.button_back);

        // back button
        backButton.setOnClickListener(v -> onBackPressed());

        getMovieList();

        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getMovieList();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMovieList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataHolder.getInstance().setData(null);
    }

    private void getMovieList() {
        TextView textView = findViewById(R.id.empty_text);
        RecyclerView rv = findViewById(R.id.rv_favmovie);

        favoriteMovieAdapter = new FavoriteMovieAdapter(this);
        // read data from database
        AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        List<FavoriteMovie> favoriteMovieList = db.favoriteMovieDao().getFavoriteMovie(Objects.requireNonNull(account).getId());

        if (favoriteMovieList.size() < 1) {
            textView.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }
        else {
            textView.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            loadData();
            initRecyclerView();
        }
    }

    private void initRecyclerView() {
        RecyclerView rv = findViewById(R.id.rv_favmovie);
        rv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(dividerItemDecoration);
        rv.setAdapter(favoriteMovieAdapter);
    }

    private void loadData() {
        // read data from database
        AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        List<FavoriteMovie> favoriteMovieList = db.favoriteMovieDao().getFavoriteMovie(Objects.requireNonNull(account).getId());
        favoriteMovieAdapter.setFavoriteMovieList(favoriteMovieList);
    }
}