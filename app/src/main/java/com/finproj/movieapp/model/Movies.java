package com.finproj.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movies {
    @SerializedName("results")
    public List<Result> results;

    public static class Result {
        // movie id
        @SerializedName("id")
        public String movie_id;

        // movie image thumbnail
        @SerializedName("poster_path")
        public String movie_image;

        // movie original title
        @SerializedName("original_title")
        public String movie_og_title;
    }
}
