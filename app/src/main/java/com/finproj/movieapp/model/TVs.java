package com.finproj.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TVs {
    @SerializedName("results")
    public List<Result> results;

    public static class Result {
        // TV id
        @SerializedName("id")
        public String tv_id;

        // TV image thumbnail
        @SerializedName("poster_path")
        public String tv_image;

        // TV original title
        @SerializedName("original_name")
        public String tv_og_title;
    }
}

