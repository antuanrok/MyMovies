package com.example.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie extends Movie{
    public FavouriteMovie(int uniqId,int id, int vote_count, String title, String originalTitle, String overwiev, String posterPath, String backdropPath, double rating, int releaseDate) {
        super(uniqId,id, vote_count, title, originalTitle, overwiev, posterPath, backdropPath, rating, releaseDate);
    }

    @Ignore
    public FavouriteMovie(Movie movie) {
        super(movie.getUniqId(),movie.getId(), movie.getVote_count(), movie.getTitle(), movie.getOriginalTitle(), movie.getOverwiev(), movie.getPosterPath(), movie.getBackdropPath(), movie.getRating(), movie.getReleaseDate());
    }
}
