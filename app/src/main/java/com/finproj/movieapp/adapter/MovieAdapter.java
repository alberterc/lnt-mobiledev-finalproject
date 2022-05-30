package com.finproj.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.finproj.movieapp.helper.DataHolder;
import com.finproj.movieapp.showsdetails.MovieDetailsActivity;
import com.finproj.movieapp.R;
import com.finproj.movieapp.databinding.ItemMovieBinding;
import com.finproj.movieapp.model.Movies;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final List<Movies.Result> movies;
    private final Context context;

    public MovieAdapter(List<Movies.Result> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        com.finproj.movieapp.databinding.ItemMovieBinding binding = ItemMovieBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void onBind(Movies.Result result) {
            TextView movieTitle = itemView.findViewById(R.id.item_movie_title);
            ImageView movieImage = itemView.findViewById(R.id.item_movie_image);
            RelativeLayout itemMovie = itemView.findViewById(R.id.item_movie);

            itemMovie.setOnClickListener(view -> {
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                DataHolder.getInstance().setData(result.movie_id);
                context.startActivity(intent);
            });

            Picasso.with(context)
                    .load("https://image.tmdb.org/t/p/w500" + result.movie_image)
                    .into(movieImage);

            movieTitle.setText(result.movie_og_title);
        }
    }
}