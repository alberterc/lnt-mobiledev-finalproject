package com.finproj.movieapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.finproj.movieapp.R;
import com.finproj.movieapp.databinding.FragmentHomeBinding;
import com.finproj.movieapp.home.UserFavoriteActivity;
import com.finproj.movieapp.localstorage.database.AppDatabase;
import com.finproj.movieapp.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private GoogleSignInClient gsc;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.finproj.movieapp.databinding.FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // make list view for all that acts similar to buttons
        ListView listView = view.findViewById(R.id.list_user_button);
        String[] buttonList = {"Favorites", "Logout", "Remove Account", "Exit"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_home, R.id.home_button, buttonList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Log.d("HOMELIST", "CLICKED row number: " + position);

            switch (position) {
                case 0:
                    startActivity(new Intent(getContext(), UserFavoriteActivity.class));
                    break;

                case 1:
                    logOut();
                    break;

                case 2:
                    removeAccount();
                    logOut();
                    break;

                case 3:
                    Objects.requireNonNull(getActivity()).moveTaskToBack(true);
                    getActivity().finish();
                    break;

                default:
                    break;
            }
        });

        ////////////////////////////////////////////////////////////////////////
        /////////////////////////// LOGIN WIH GOOGLE ///////////////////////////

        // get logged in account
        createLoginRequest();

        // get account info
        TextView userName = view.findViewById(R.id.user_name);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(Objects.requireNonNull(getActivity()));
        if (account != null) {
            String accName = account.getDisplayName();

            String str = "Welcome,\n" + accName;

            userName.setText(str);
        }

        /////////////////////////// LOGIN WIH GOOGLE ///////////////////////////
        ////////////////////////////////////////////////////////////////////////
    }

    private void removeAccount() {
        // delete data in database
        AppDatabase db = AppDatabase.getInstance(this.getContext());
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(Objects.requireNonNull(getActivity()));
        if (account != null) {
            db.favoriteMovieDao().deleteAllFavoriteMovieFromAccount(account.getId());
            db.favoriteTvShowDao().deleteAllFavoriteTvShowFromAccount(account.getId());
        }
    }

    private void logOut() {
        gsc.signOut().addOnCompleteListener(task -> {
            startActivity(new Intent(getContext(), LoginActivity.class));
            Objects.requireNonNull(getActivity()).finish();
        });
    }

    private void createLoginRequest() {
        //Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso
        gsc = GoogleSignIn.getClient(Objects.requireNonNull(getActivity()), gso);
    }
}