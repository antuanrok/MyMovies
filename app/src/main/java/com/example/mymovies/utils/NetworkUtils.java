package com.example.mymovies.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.mymovies.MainActivity;
import com.example.mymovies.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {

    private static final String BASE_URL = "https://api.kinopoisk.dev/v1.3/movie";
    private static final String REVIEW_URL = "https://api.kinopoisk.dev/v1/review";


    private static final String PARAMS_NOT_NULL_ID = "!null";
    private static final String PARAMS_NOT_NULL_TITLE = "name";
    private static final String PARAMS_NOT_NULL_ORIG_TITLE = "alternativeName";

    private static final String PARAMS_NOT_NULL_OVERVIEW = "description";
    private static final String PARAMS_NOT_NULL_POSTER_PATH = "url";
    private static final String PARAMS_NOT_NULL_BACKDROP_PATH = "previewUrl";
    private static final String PARAMS_NOT_NULL_RATING = "!null";
    private static final String PARAMS_NOT_NULL_RELEASE_DATE = "!null";

    private static final String PARAMS_API_KEY = "X-API-KEY";
    private static final String PARAMS_ACCEPT = "accept";
    private static final String PARAMS_PAGE = "page";
    private static final String PARAMS_LIMIT = "limit";
    private static final String PARAMS_SORT_FIELD = "sortField";
    private static final String PARAMS_SORT_TYPE = "sortType";
    private static final String PARAMS_TOP_250 = "top250";
    private static final String PARAMS_TYPE = "type";

    private static final String PARAMS_ID = "id";

    private static final String API_KEY = "5W90EA0-55VMD01-QCTCXS3-4RE59N7";
    private static final String ACCEPT = "application/json";
    private static final String LIMIT = "20";
    private static final String PAGE = "1";
    private static final String SORT_BY_POP = "rating.imdb";
    private static final String TYPE_SORT_BY_POP = "-1";

    private static final String SORT_BY_VOTES = "votes.imdb";
    private static final String TYPE_SORT_BY_VOTES = "-1";


    private static final String NOT_NULL_ID = "%21null";
    private static final String NOT_NULL_TITLE = "!null";
    private static final String NOT_NULL_ORIG_TITLE = "!null";

    private static final String NOT_NULL_OVERVIEW = "!null";
    private static final String NOT_NULL_POSTER_PATH = "!null";
    private static final String NOT_NULL_BACKDROP_PATH = "!null";
    private static final String NOT_NULL_TOP_250 = "!null";
    private static final String NOT_NULL_RATING = "!null";
    private static final String NOT_NULL_RELEASE_DATE = "!null";

    private static final String TYPE_1 = "movie";
    private static final String TYPE_2 = "cartoon";
    private static final String TYPE_3 = "anime";


    public static final int TOP_POP = 0;
    public static final int TOP_VOTES = 1;

    private static final String PARAMS_SELECT_FIELDS = "selectFields";
    private static final String SELECT_FIELDS = "videos";

    private static final String PARAMS_MOVIE_ID = "movieID";


    Context thisC;

    public static URL buildURL_Rev(int page, int id) {
        URL result = null;
        Uri uri = Uri.parse(REVIEW_URL).buildUpon()
                .appendQueryParameter(PARAMS_PAGE, String.valueOf(page))
                .appendQueryParameter(PARAMS_LIMIT, LIMIT)
                .appendQueryParameter(PARAMS_MOVIE_ID, String.valueOf(id))
                .build();

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static URL buildURL_Mov(int page, int id) {
        URL result = null;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_SELECT_FIELDS, SELECT_FIELDS)
                .appendQueryParameter(PARAMS_PAGE, String.valueOf(page))
                .appendQueryParameter(PARAMS_LIMIT, LIMIT)
                .appendQueryParameter(PARAMS_ID, String.valueOf(id))
                .build();

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static URL buildURL(int sort_by, int page) {
        String methodOfSort;
        String typeOfSort;
        URL result = null;
        if (sort_by == TOP_POP) {
            methodOfSort = SORT_BY_POP;
            typeOfSort = TYPE_SORT_BY_POP;
        } else {
            methodOfSort = SORT_BY_VOTES;
            typeOfSort = TYPE_SORT_BY_VOTES;
        }
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_PAGE, String.valueOf(page))
                .appendQueryParameter(PARAMS_LIMIT, LIMIT)
                .appendQueryParameter(PARAMS_SORT_FIELD, methodOfSort)
                .appendQueryParameter(PARAMS_SORT_TYPE, typeOfSort)
                .appendQueryParameter(PARAMS_NOT_NULL_TITLE, NOT_NULL_TITLE)
                .appendQueryParameter(PARAMS_NOT_NULL_ORIG_TITLE, NOT_NULL_ORIG_TITLE)
                .appendQueryParameter(PARAMS_NOT_NULL_OVERVIEW, NOT_NULL_OVERVIEW)
                .appendQueryParameter(PARAMS_NOT_NULL_POSTER_PATH, NOT_NULL_POSTER_PATH)
                .appendQueryParameter(PARAMS_NOT_NULL_BACKDROP_PATH, NOT_NULL_BACKDROP_PATH)
                .appendQueryParameter(PARAMS_TOP_250, NOT_NULL_TOP_250)
                .appendQueryParameter(PARAMS_TYPE, TYPE_1)
                .appendQueryParameter(PARAMS_TYPE, TYPE_2)
                .appendQueryParameter(PARAMS_TYPE, TYPE_3)
                .build();

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONForReviews(int page, int id) {
        URL url = buildURL_Rev(page, id);
        JSONObject result = null;
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONForVideos(int page, int id) {
        URL url = buildURL_Mov(page, id);
        JSONObject result = null;
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONFromNetwork(int sortBy, int page) {
        URL url = buildURL(sortBy, page);
        JSONObject result = null;
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject> {
        private Bundle bundle;

        private OnStartLoadingListener onStartLoadingListener;

       public interface OnStartLoadingListener {
           void onStartLoading();
       }

        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (onStartLoadingListener!=null) {
                onStartLoadingListener.onStartLoading();
            }
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            JSONObject result = null;
            if (bundle == null) {
                return result;
            }
            String urlAsString = bundle.getString("url");
            URL url = null;
            try {
                url = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (url == null) {
                return result;
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty(PARAMS_ACCEPT, ACCEPT);
                urlConnection.setRequestProperty(PARAMS_API_KEY, API_KEY);

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                StringBuilder stringBuilder = new StringBuilder();
                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
                result = new JSONObject(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }


            return result;
        }
    }

    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject result = null;
            if (urls == null || urls.length == 0) {
                return result;
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) urls[0].openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty(PARAMS_ACCEPT, ACCEPT);
                urlConnection.setRequestProperty(PARAMS_API_KEY, API_KEY);

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                StringBuilder stringBuilder = new StringBuilder();
                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
                result = new JSONObject(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }


            return result;

        }
    }

}
