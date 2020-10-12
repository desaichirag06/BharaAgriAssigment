package com.chirag.bharaagriassigment.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.chirag.bharaagriassigment.utilities.TimeStampUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity
public class MovieDetail {

    @SerializedName("popularity")
    @Expose
    public Double popularity;
    @SerializedName("vote_count")
    @Expose
    public Integer voteCount;
    @SerializedName("video")
    @Expose
    public Boolean video;
    @SerializedName("poster_path")
    @Expose
    public String posterPath;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("adult")
    @Expose
    public Boolean adult;
    @SerializedName("backdrop_path")
    @Expose
    public String backdropPath;
    @SerializedName("original_language")
    @Expose
    public String originalLanguage;
    @SerializedName("original_title")
    @Expose
    public String originalTitle;
    @SerializedName("genre_ids")
    @Expose
    public ArrayList<Integer> genreIds = null;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("vote_average")
    @Expose
    public Double voteAverage;
    @SerializedName("overview")
    @Expose
    public String overview;
    @SerializedName("release_date")
    @Expose
    public String releaseDate;

    @PrimaryKey(autoGenerate = true)
    public int uuid;

    public MovieDetail(String posterPath, String originalTitle, String overview, String releaseDate, Double voteAverage) {
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    public String getDetailedVoteAverage() {
        return voteAverage + "/10";
    }

    public String getReleaseDate() {
        return TimeStampUtils.getPrettyTime(TimeStampUtils.dateToSeconds(releaseDate));
    }

    @Override
    public String toString() {
        return "MovieDetail{" +
                "popularity=" + popularity +
                ", voteCount=" + voteCount +
                ", video=" + video +
                ", posterPath='" + posterPath + '\'' +
                ", id=" + id +
                ", adult=" + adult +
                ", backdropPath='" + backdropPath + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", genreIds=" + genreIds +
                ", title='" + title + '\'' +
                ", voteAverage=" + voteAverage +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
