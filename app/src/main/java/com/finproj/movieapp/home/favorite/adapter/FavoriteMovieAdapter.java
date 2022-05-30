package com.finproj.movieapp.home.favorite.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.finproj.movieapp.R;
import com.finproj.movieapp.helper.DataHolder;
import com.finproj.movieapp.localstorage.entity.FavoriteMovie;
import com.finproj.movieapp.model.MovieItem;
import com.finproj.movieapp.network.TmdbApiClient;
import com.finproj.movieapp.showsdetails.MovieDetailsActivity;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.ViewHolder> {

    private final Context context;
    private List<FavoriteMovie> favoriteMovieList;

    public FavoriteMovieAdapter(Context context) {
        this.context = context;
    }

    public void setFavoriteMovieList(List<FavoriteMovie> favoriteMovieList) {
        this.favoriteMovieList = favoriteMovieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fav, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // read movie id from database and pass it to DataHolder for api
        String movie_id = this.favoriteMovieList.get(position).favMovieId;
        DataHolder.getInstance().setData(movie_id);

        // get data from tmdb api
        TmdbApiClient api = new TmdbApiClient();
        api.apiInterface().getMovieDetails().enqueue(new Callback<MovieItem>() {

            @Override
            public void onResponse(@NonNull Call<MovieItem> call, @NonNull Response<MovieItem> response) {
                MovieItem itemMovie = response.body();
                if (itemMovie != null) {
                    Picasso.with(context)
                            .load("https://image.tmdb.org/t/p/w500" + itemMovie.movie_image)
                            .into(holder.image);

                    holder.title.setText(itemMovie.movie_og_title);
                }
                else {
                    Toast toast = Toast.makeText(context, "Failed to retrieve from server.\nSwipe down to refresh.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieItem> call, @NonNull Throwable t) {
                Toast toast = Toast.makeText(context, "Failed to retrieve from server.\nSwipe down to refresh.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        // open movie details for each movie item
        holder.itemFav.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            DataHolder.getInstance().setData(movie_id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.favoriteMovieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        RelativeLayout itemFav;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            itemFav = itemView.findViewById(R.id.item_fav);
        }
    }
}
