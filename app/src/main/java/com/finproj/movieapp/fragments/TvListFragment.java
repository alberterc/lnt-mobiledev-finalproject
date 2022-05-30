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
import com.finproj.movieapp.adapter.TvAdapter;
import com.finproj.movieapp.databinding.FragmentTvListBinding;
import com.finproj.movieapp.model.TVs;
import com.finproj.movieapp.network.TmdbApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Collections;
import java.util.Random;

public class TvListFragment extends Fragment {

    private FragmentTvListBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTvListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getTvs();

        swipeRefreshLayout = view.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getTvs();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void getTvs() {
        TmdbApiClient api = new TmdbApiClient();
        api.apiInterface().getTvOnTheAir().enqueue(new Callback<TVs>() {
            @Override
            public void onResponse(@NonNull Call<TVs> call, @NonNull Response<TVs> response) {

                binding.recyclerTv.setLayoutManager(new GridLayoutManager(getContext(), 2));
                if (response.body() != null) {
                    // randomize list to show that list refreshed
                    Collections.shuffle(response.body().results, new Random(System.currentTimeMillis()));
                    binding.recyclerTv.setAdapter(new TvAdapter(response.body().results, getContext()));
                }
                else {
                    Toast toast = Toast.makeText(getContext(), "Failed to retrieve from server.\nSwipe down to refresh.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<TVs> call, @NonNull Throwable t) {
                Toast toast = Toast.makeText(getContext(), "Failed to retrieve from server.\nSwipe down to refresh.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }
}