package com.finproj.movieapp.showsdetails;

import android.view.Gravity;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.finproj.movieapp.helper.DataHolder;
import com.finproj.movieapp.R;
import com.finproj.movieapp.localstorage.database.AppDatabase;
import com.finproj.movieapp.localstorage.entity.FavoriteMovie;
import com.finproj.movieapp.model.MovieItem;
import com.finproj.movieapp.network.TmdbApiClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.Objects;

public class MovieDetailsActivity extends AppCompatActivity {

    ImageView movieImage;
    Button backButton;
    Button favButton;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView movieOriginalTitle;
    TextView movieTitle;
    TextView movieDetails;
    TextView movieOverview;
    ScrollView pageScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieImage = findViewById(R.id.movie_image);
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        backButton = findViewById(R.id.button_back);
        favButton = findViewById(R.id.button_add_favorite);
        movieOriginalTitle = findViewById(R.id.movie_og_title);
        movieTitle = findViewById(R.id.movie_title);
        movieDetails = findViewById(R.id.movie_details);
        movieOverview = findViewById(R.id.movie_overview);
        pageScroller = findViewById(R.id.page_scroller);

        // check if movie is favorited
        int isFav; // 0 : false, 1 : true
        // read data from database
        AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        String movie_id = DataHolder.getInstance().getData();
        String curr_uid = Objects.requireNonNull(account).getId();

        List<FavoriteMovie> checkFavMovie = db.favoriteMovieDao().checkFavoritedMovie(movie_id, curr_uid);
        isFav = checkFavMovie.size();

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
        getMovieDetails();

        // swipe refresh
        pageScroller.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = pageScroller.getScrollY(); // get vertical height of text scroll view
            swipeRefreshLayout.setEnabled(scrollY == 0);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // get data from tmdb api
            getMovieDetails();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void removeFavorite() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        String movie_id = DataHolder.getInstance().getData();
        String user_id = Objects.requireNonNull(account).getId();

        AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
        Integer getId = db.favoriteMovieDao().getPrimaryKeyId(movie_id, user_id);

        FavoriteMovie model = new FavoriteMovie();
        model.id = getId;
        model.uid = user_id;
        model.favMovieId = movie_id;

        db.favoriteMovieDao().removeFavMovie(model);
    }

    private void addFavorite() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        String movie_id = DataHolder.getInstance().getData();
        String user_id = Objects.requireNonNull(account).getId();

        AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
        FavoriteMovie model = new FavoriteMovie();
        model.uid = user_id;
        model.favMovieId = movie_id;
        db.favoriteMovieDao().addFavMovie(model);
    }

    public void getMovieDetails() {
        // get data from tmdb api
        TmdbApiClient api = new TmdbApiClient();
        api.apiInterface().getMovieDetails().enqueue(new Callback<MovieItem>() {

            @Override
            public void onResponse(@NonNull Call<MovieItem> call, @NonNull Response<MovieItem> response) {
                MovieItem itemMovie = response.body();
                if (itemMovie != null) {
                    StringBuilder str = new StringBuilder();

                    Picasso.with(getApplicationContext())
                            .load("https://image.tmdb.org/t/p/w500" + itemMovie.movie_image)
                            .into(movieImage);

                    if (itemMovie.movie_title.equals(itemMovie.movie_og_title)) {
                        movieOriginalTitle.setText(itemMovie.movie_og_title);
                        movieTitle.setText("");
                    }
                    else {
                        movieOriginalTitle.setText(itemMovie.movie_title);
                        str.append("(").append(itemMovie.movie_title).append(")");
                        movieTitle.setText(str);
                    }

                    str.setLength(0);
                    str.append("Popularity: ").append(itemMovie.movie_popularity).append("\n")
                            .append("Release Date: ").append(itemMovie.movie_rel_date).append("\n")
                            .append("Language: ").append(itemMovie.movie_language.toUpperCase()).append("\n")
                            .append("\n").append(itemMovie.movie_tagline);
                    movieDetails.setText(str);

                    if (itemMovie.movie_overview.equals("")) {
                        movieOverview.setText(R.string.item_no_overview_found_text);
                    }
                    else {
                        movieOverview.setText(itemMovie.movie_overview);
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Failed to retrieve from server.\nSwipe down to refresh.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieItem> call, @NonNull Throwable t) {
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