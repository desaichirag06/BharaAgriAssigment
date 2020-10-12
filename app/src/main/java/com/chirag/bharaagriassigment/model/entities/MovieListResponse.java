package com.chirag.bharaagriassigment.model.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieListResponse {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private ArrayList<MovieDetail> results = null;

    public MovieListResponse() {
    }

    public ArrayList<MovieDetail> getResults() {
        return results;
    }

}
