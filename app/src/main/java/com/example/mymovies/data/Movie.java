package com.example.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "movies")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int uniqId;

    private int id;
    private int vote_count;
    private String title;
    private String originalTitle;
    private String overwiev;
    private String posterPath;
    private String backdropPath;
    private double rating;
    private  int releaseDate;

    public Movie(int uniqId, int id, int vote_count, String title, String originalTitle, String overwiev, String posterPath, String backdropPath, double rating, int releaseDate) {
        this.id = id;
        this.vote_count = vote_count;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overwiev = overwiev;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.uniqId = uniqId;
    }


    @Ignore
    public Movie( int id, int vote_count, String title, String originalTitle, String overwiev, String posterPath, String backdropPath, double rating, int releaseDate) {
        this.id = id;
        this.vote_count = vote_count;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overwiev = overwiev;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public int getUniqId() {
        return uniqId;
    }

    public void setUniqId(int uniqId) {
        this.uniqId = uniqId;
    }

    public int getId() {
        return id;
    }

    public int getVote_count() {
        return vote_count;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverwiev() {
        return overwiev;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public double getRating() {
        return rating;
    }

    public int getReleaseDate() {
        return releaseDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOverwiev(String overwiev) {
        this.overwiev = overwiev;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setReleaseDate(int releaseDate) {
        this.releaseDate = releaseDate;
    }
}
