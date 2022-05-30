package com.finproj.movieapp.model;

import com.google.gson.annotations.SerializedName;

public class TvItem {
    // tv id
    @SerializedName("id")
    public String tv_id;

    // tv popularity
    @SerializedName("popularity")
    public String tv_popularity;

    // tv image thumbnail
    @SerializedName("poster_path")
    public String tv_image;

    // tv original title
    @SerializedName("original_name")
    public String tv_og_title;

    // tv title
    @SerializedName("name")
    public String tv_title;

    // tv first air date
    @SerializedName("first_air_date")
    public String tv_first_air_date;

    // tv language
    @SerializedName("original_language")
    public String tv_language;

    // tv overview
    @SerializedName("overview")
    public String tv_overview;

    // tv tagline
    @SerializedName("tagline")
    public String tv_tagline;

    // tv number of episodes
    @SerializedName("number_of_episodes")
    public String tv_num_eps;

    // tv number of seasons
    @SerializedName("number_of_seasons")
    public String tv_num_seasons;
}
