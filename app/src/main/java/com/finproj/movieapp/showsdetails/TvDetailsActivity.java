package com.finproj.movieapp.showsdetails;

import android.util.Log;
import android.view.Gravity;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.finproj.movieapp.helper.DataHolder;
import com.finproj.movieapp.R;
import com.finproj.movieapp.localstorage.database.AppDatabase;
import com.finproj.movieapp.localstorage.entity.FavoriteTvShow;
import com.finproj.movieapp.model.TvItem;
import com.finproj.movieapp.network.TmdbApiClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.Objects;

public class TvDetailsActivity extends AppCompatActivity {

    ImageView tvImage;
    Button backButton;
    Button favButton;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvOriginalTitle;
    TextView tvTitle;
    TextView tvDetails;
    TextView tvOverview;
    ScrollView pageScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_details);

        tvImage = findViewById(R.id.tv_image);
        backButton = findViewById(R.id.button_back);
        favButton = findViewById(R.id.button_add_favorite);
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        tvOriginalTitle = findViewById(R.id.tv_og_title);
        tvTitle = findViewById(R.id.tv_title);
        tvDetails = findViewById(R.id.tv_details);
        tvOverview = findViewById(R.id.tv_overview);
        pageScroller = findViewById(R.id.page_scroller);

        // check if movie is favorited
        int isFav; // 0 : false, 1 : true
        // read data from database
        AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        String tvshow_id = DataHolder.getInstance().getData();
        String curr_uid = Objects.requireNonNull(account).getId();

        List<FavoriteTvShow> checkFavTvShow = db.favoriteTvShowDao().checkFavoritedTvShow(tvshow_id, curr_uid);
        isFav = checkFavTvShow.size();
        Log.d("DATABASE", "list size: " + isFav);
        // favorite button
        if (isFav == 0) {
            favButton.setText(R.string.add_favorite_button_text);
            favButton.setOnClickListener(v -> {
                addFavorite();
                favButton.setText(R.string.added_favorite_button_text);
                favButton.setClickable(false);
            });
        }
        else {
            favButton.setText(R.string.remove_favorite_button_text);
            favButton.setOnClickListener(v -> {
                removeFavorite();
                favButton.setText(R.string.removed_favorite_button_text);
                favButton.setClickable(false);
            });
        }

        // back button
        backButton.setOnClickListener(view -> onBackPressed());

        // get data from tmdb api
        getTvDetails();

        // swipe refresh
        pageScroller.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = pageScroller.getScrollY(); // get vertical height of scroll view
            swipeRefreshLayout.setEnabled(scrollY == 0);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // get data from tmdb api
            getTvDetails();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void removeFavorite() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        String tvshow_id = DataHolder.getInstance().getData();
        String user_id = Objects.requireNonNull(account).getId();

        AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
        Integer getId = db.favoriteTvShowDao().getPrimaryKeyId(tvshow_id, user_id);

        FavoriteTvShow model = new FavoriteTvShow();
        model.id = getId;
        model.uid = user_id;
        model.favTvShowId = tvshow_id;

        db.favoriteTvShowDao().removeFavTvShow(model);
    }

    private void addFavorite() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        String tvshow_id = DataHolder.getInstance().getData();
        String user_id = Objects.requireNonNull(account).getId();

        AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
        FavoriteTvShow model = new FavoriteTvShow();
        model.uid = user_id;
        model.favTvShowId = tvshow_id;
        db.favoriteTvShowDao().addFavTvShow(model);
    }

    public void getTvDetails() {
        // get data from tmdb api
        TmdbApiClient api = new TmdbApiClient();
        api.apiInterface().getTvDetails().enqueue(new Callback<TvItem>() {

            @Override
            public void onResponse(@NonNull Call<TvItem> call, @NonNull Response<TvItem> response) {
                TvItem itemTv = response.body();
                if (itemTv != null) {
                    StringBuilder str = new StringBuilder();

                    Picasso.with(getApplicationContext())
                            .load("https://image.tmdb.org/t/p/w500" + itemTv.tv_image)
                            .into(tvImage);

                    if (itemTv.tv_title.equals(itemTv.tv_og_title)) {
                        tvOriginalTitle.setText(itemTv.tv_og_title);
                        tvTitle.setText("");
                    }
                    else {
                        tvOriginalTitle.setText(itemTv.tv_og_title);
                        str.append("(").append(itemTv.tv_title).append(")");
                        tvTitle.setText(str);
                    }

                    str.setLength(0);
                    str.append("Popularity: ").append(itemTv.tv_popularity).append("\n")
                            .append("Release Date: ").append(itemTv.tv_first_air_date).append("\n")
                            .append("Language: ").append(itemTv.tv_language.toUpperCase()).append("\n")
                            .append("Episodes/Seasons: ").append(itemTv.tv_num_eps).append("/").append(itemTv.tv_num_seasons).append("\n");
                    tvDetails.setText(str);

                    if (itemTv.tv_overview.equals("")) {
                        tvOverview.setText(R.string.item_no_overview_found_text);
                    }
                    else {
                        tvOverview.setText(itemTv.tv_overview);
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Failed to retrieve from server.\nSwipe down to refresh.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvItem> call, @NonNull Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "Failed to retrieve from server.\nSwipe down to refresh.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataHolder.getInstance().setData(null);
    }
}