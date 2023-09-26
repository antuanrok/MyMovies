package com.example.mymovies.utils;

import android.util.Log;

import com.example.mymovies.data.Movie;
import com.example.mymovies.data.Review;
import com.example.mymovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {
   /* private int id;
    private int vote_count;
    private String title;
    private String originalTitle;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private double rating;
    private String releaseDate;*/

    private static final String KEY_RESULTS = "docs";

    // Для отзыва
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_REVIEW = "review";

    // Для видео
    private static final String KEY_VIDEOS = "videos";
    private static final String KEY_TRAILERS = "trailers";
    private static final String KEY_TEASERS = "teasers";
    private static final String KEY_VIDEO_URL = "url";
    private static final String KEY_NAME = "name";

    // Вся информация
    private static final String KEY_ID = "id";
    private static final String KEY_ARR_RATING = "rating";
    private static final String KEY_RATING = "imdb";

    private static final String KEY_TITLE = "name";
    private static final String KEY_ORIGINAL_TITLE = "alternativeName";
    private static final String KEY_OVERVIEW = "description";
    private static final String KEY_ARR_POSTERPATH = "poster";
    private static final String KEY_POSTERPATH = "url";
    private static final String KEY_BACK_DROP_PATH = "previewUrl";
    private static final String KEY_ARR_VOTES = "votes";
    private static final String KEY_VOTES = "imdb";
    private static final String KEY_RELEASE_DATE = "year";

    public static ArrayList<Trailer> getVideosFromJSON(JSONObject jsonObject) {
        ArrayList<Trailer> result = new ArrayList<>();
        if (jsonObject == null) {
            result = null;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            JSONObject objectMovie = jsonArray.getJSONObject(0);
            JSONObject objectVideo = objectMovie.getJSONObject(KEY_VIDEOS);

            JSONArray jsonArray_trailers = objectVideo.getJSONArray(KEY_TRAILERS);
            for (int i = 0; i < jsonArray_trailers.length(); i++) {
                JSONObject objecttrailer = jsonArray_trailers.getJSONObject(i);

                String url = objecttrailer.getString(KEY_VIDEO_URL);
                String name = objecttrailer.getString(KEY_NAME);


                Trailer movie = new Trailer(url, name);
                result.add(movie);
            }

            JSONArray jsonArray_teasers = objectVideo.getJSONArray(KEY_TEASERS);
            for (int i = 0; i < jsonArray_teasers.length(); i++) {
                JSONObject objectteasers = jsonArray_teasers.getJSONObject(i);

                String url = objectteasers.getString(KEY_VIDEO_URL);
                String name = objectteasers.getString(KEY_NAME);


                Trailer movie = new Trailer(url, name);
                result.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Review> getReviewsFromJSON(JSONObject jsonObject) {
        ArrayList<Review> result = new ArrayList<>();
        if (jsonObject == null) {
            result = null;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectMovie = jsonArray.getJSONObject(i);

                String author = objectMovie.getString(KEY_AUTHOR);
                String content = objectMovie.getString(KEY_REVIEW);


                Review movie = new Review(author, content);
                result.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Movie> getMoviesFromJSON(JSONObject jsonObject) {
        ArrayList<Movie> result = new ArrayList<>();
        if (jsonObject == null) {
            result = null;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectMovie = jsonArray.getJSONObject(i);
                int id;
                try {
                    id = objectMovie.getInt(KEY_ID);
                } catch (Exception e) {
                    id = 0;
                }
                int releaseDate;
                try {
                    releaseDate = objectMovie.getInt(KEY_RELEASE_DATE);
                } catch (Exception e) {
                    releaseDate = 0;
                }


                String title = objectMovie.getString(KEY_TITLE);
                String ori_title = objectMovie.getString(KEY_ORIGINAL_TITLE);
                String overview = objectMovie.getString(KEY_OVERVIEW);

                JSONObject objectRaiting = objectMovie.getJSONObject(KEY_ARR_RATING);
                double raiting;
                try {
                    raiting = objectRaiting.getDouble(KEY_RATING);
                } catch (Exception e) {
                    raiting = 0.0;
                }
                JSONObject objectVotes = objectMovie.getJSONObject(KEY_ARR_VOTES);
                int votes;
                try {
                    votes = objectVotes.getInt(KEY_VOTES);
                } catch (Exception e) {
                    votes = 0;
                }

                JSONObject objectPosterPath = objectMovie.getJSONObject(KEY_ARR_POSTERPATH);
                String posterPath = objectPosterPath.getString(KEY_POSTERPATH);
                String backdropPath = objectPosterPath.getString(KEY_BACK_DROP_PATH);
                Movie movie = new Movie(id, votes, title, ori_title, overview, posterPath, backdropPath, raiting, releaseDate);
                result.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


}
