package com.finproj.movieapp.model;

import com.google.gson.annotations.SerializedName;

public class MovieItem {
    // movie id
    @SerializedName("id")
    public String movie_id;

    // movie popularity
    @SerializedName("popularity")
    public String movie_popularity;

    // movie image thumbnail
    @SerializedName("poster_path")
    public String movie_image;

    // movie original title
    @SerializedName("original_title")
    public String movie_og_title;

    // movie title
    @SerializedName("title")
    public String movie_title;

    // movie release date
    @SerializedName("release_date")
    public String movie_rel_date;

    // movie language
    @SerializedName("original_language")
    public String movie_language;

    // movie overview
    @SerializedName("overview")
    public String movie_overview;

    // movie tagline
    @SerializedName("tagline")
    public String movie_tagline;
}
