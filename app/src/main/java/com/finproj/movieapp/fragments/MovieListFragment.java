package com.finproj.movieapp.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.finproj.movieapp.R;
import com.finproj.movieapp.adapter.MovieAdapter;
import com.finproj.movieapp.databinding.FragmentMovieListBinding;
import com.finproj.movieapp.model.Movies;
import com.finproj.movieapp.network.TmdbApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Collections;
import java.util.Random;

public class MovieListFragment extends Fragment {

    private FragmentMovieListBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMovieListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMovies();

        swipeRefreshLayout = view.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getMovies();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void getMovies() {
        TmdbApiClient api = new TmdbApiClient();
        api.apiInterface().getMovieNowPlaying().enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {

                binding.recyclerMovie.setLayoutManager(new GridLayoutManager(getContext(), 2));
                if (response.body() != null) {
                    // randomize list to show that list refreshed
                    Collections.shuffle(response.body().results, new Random(System.currentTimeMillis()));
                    binding.recyclerMovie.setAdapter(new MovieAdapter(response.body().results, getContext()));
                }
                else {
                    Toast toast = Toast.makeText(getContext(), "Failed to retrieve from server.\nSwipe down to refresh.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
                Toast toast = Toast.makeText(getContext(), "Failed to retrieve from server.\nSwipe down to refresh.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }
}