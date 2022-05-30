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
import com.finproj.movieapp.home.favorite.adapter.FavoriteTvShowAdapter;
import com.finproj.movieapp.localstorage.database.AppDatabase;
import com.finproj.movieapp.localstorage.entity.FavoriteTvShow;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;
import java.util.Objects;

public class FavoriteTvShowActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private FavoriteTvShowAdapter favoriteTvShowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_tv_show);

        Button backButton = findViewById(R.id.button_back);

        // back button
        backButton.setOnClickListener(v -> onBackPressed());

        getTvShowList();

        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getTvShowList();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTvShowList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataHolder.getInstance().setData(null);
    }

    private void getTvShowList() {
        TextView textView = findViewById(R.id.empty_text);
        RecyclerView rv = findViewById(R.id.rv_favtvshow);

        favoriteTvShowAdapter = new FavoriteTvShowAdapter(this);
        // read data from database
        AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        List<FavoriteTvShow> favoriteTvShowList = db.favoriteTvShowDao().getFavoriteTvShow(Objects.requireNonNull(account).getId());

        if (favoriteTvShowList.size() < 1) {
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
        RecyclerView rv = findViewById(R.id.rv_favtvshow);
        rv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(dividerItemDecoration);
        rv.setAdapter(favoriteTvShowAdapter);
    }

    private void loadData() {
        // read data from database
        AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        List<FavoriteTvShow> favoriteTvShowList = db.favoriteTvShowDao().getFavoriteTvShow(Objects.requireNonNull(account).getId());
        favoriteTvShowAdapter.setFavoriteTvShowList(favoriteTvShowList);
    }
}