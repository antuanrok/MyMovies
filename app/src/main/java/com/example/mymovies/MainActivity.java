package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovies.adapters.MovieAdapter;
import com.example.mymovies.data.MainViewModel;
import com.example.mymovies.data.Movie;
import com.example.mymovies.utils.JSONUtils;
import com.example.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private RecyclerView recyclerViewPosters;
    private MovieAdapter movieAdapter;
    private Switch switchSort;

    private TextView textpop;
    private TextView textrate;
    private MainViewModel viewModel;
    private static int LOADER_ID = 1995;
    private androidx.loader.app.LoaderManager loaderManager;
    private static int page = 1;
    private static boolean isLoading=false;
    private static int methodOfSort;
    private ProgressBar progressBarLoading;
    private String lang;

    private boolean key_create = false;
    private static int prev_methodOfSort;

    private static final String APP_PREF = "my_settings";

    private static final String APP_PREF_METH_OF_SORT = "MethodOfSort";

    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_Main) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.item_Favourite) {
            Intent intent = new Intent(this, FavouriteActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private int getColumnCount(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int)(displayMetrics.widthPixels/ displayMetrics.density);
        return width/185 >3 ? width/185 : 3;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSettings = getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        editor = mSettings.edit();

        lang = Locale.getDefault().getLanguage();
        loaderManager = LoaderManager.getInstance(this);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        recyclerViewPosters = findViewById(R.id.recyclerViewPosters);
        switchSort = findViewById(R.id.switchPopularity);
        movieAdapter = new MovieAdapter();
        recyclerViewPosters.setAdapter(movieAdapter);
        recyclerViewPosters.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
        textpop = findViewById(R.id.textViewPopularity);
        textrate = findViewById(R.id.textViewTopRated);
        progressBarLoading = findViewById(R.id.progressBarLoading);
       /* JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(NetworkUtils.TOP_VOTES, 1);
        ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);

        movieAdapter.setMovies(movies);
        recyclerViewPosters.setAdapter(movieAdapter);*/
        Boolean t_mof = true;
        if(mSettings.contains(APP_PREF_METH_OF_SORT)){
            // Получаем число из настроек
            t_mof = mSettings.getBoolean(APP_PREF_METH_OF_SORT, true);
        }
        switchSort.setChecked(!t_mof);
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                page=1;
                setMethodofSort(b);
            }
        });
        switchSort.setChecked(t_mof);
        movieAdapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie movie = movieAdapter.getMovies().get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", movie.getId());
                startActivity(intent);
                // Toast.makeText(MainActivity.this, "Clicked" + position, Toast.LENGTH_SHORT).show();
            }
        });
        movieAdapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void OnReachEnd() {
                if (!isLoading) {
                    Toast.makeText(MainActivity.this, R.string.text_loading, Toast.LENGTH_SHORT).show();
                    downloadData(methodOfSort,page);
                }
            }
        });
        LiveData<List<Movie>> movieFromLiveData = viewModel.getMovies();//Берём все муви при загрузке ПО, связываем с Ливдата
        movieFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                //               movieAdapter.setMovies(movies);
                //               recyclerViewPosters.setAdapter(movieAdapter);
                if (/*page==1 &&*/ !key_create) {
                    key_create = true;
                    movieAdapter.setMovies(movies);
               }
            }
        });
        /*StringBuilder stringBuilder = new StringBuilder();
        for (Movie movie:
             movies) {
            stringBuilder.append(movie.getTitle()).append("\n");
        }
        Log.i("MyRes",stringBuilder.toString());
        if (stringBuilder.toString().isEmpty()) {
            //  if (true) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } else {
                    Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void onClickSetPop(View view) {
        switchSort.setChecked(false);

    }

    public void onClickSetTopRated(View view) {
        switchSort.setChecked(true);

    }

    private void setMethodofSort(boolean isTopRated) {
        setTextColorOfTextView(isTopRated);
        if (isTopRated) {
            methodOfSort = NetworkUtils.TOP_POP;

        } else {
            methodOfSort = NetworkUtils.TOP_VOTES;
        }
        editor.putBoolean(APP_PREF_METH_OF_SORT, isTopRated);
        downloadData(methodOfSort, page);
    }


    @Override
    protected void onStop() {
        super.onStop();
        editor.apply();
    }

    private void downloadData(int methodOfSort, int page) {
        URL url = NetworkUtils.buildURL(methodOfSort, page);
        Bundle bundle = new Bundle();
        bundle.putString("url", url.toString());
        //Toast.makeText(MainActivity.this, url.toString(), Toast.LENGTH_SHORT).show();
        loaderManager.restartLoader(LOADER_ID, bundle, this);
        /*JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(methodOfSort, page);
        ArrayList<Movie> movies=null;
       if (jsonObject!= null)  movies = JSONUtils.getMoviesFromJSON(jsonObject);

        if (movies != null && !movies.isEmpty()) {
            viewModel.deleteAllMovies();
            for (Movie movie : movies) {
                viewModel.insertMovie(movie);
            }
            // movieAdapter.setMovies(movies);
            // recyclerViewPosters.setAdapter(movieAdapter);
        }*/

    }

    private void setTextColorOfTextView(boolean isTopRated) {
        if (!isTopRated) {
            textpop.setTextColor(getResources().getColor(R.color.rose));
            textrate.setTextColor(getResources().getColor(R.color.white));
        } else {
            textpop.setTextColor(getResources().getColor(R.color.white));
            textrate.setTextColor(getResources().getColor(R.color.rose));
        }

    }


    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, args);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                isLoading = true;
                progressBarLoading.setVisibility(View.VISIBLE);
            }
        });
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        ArrayList<Movie> movies = null;
        if (data != null) movies = JSONUtils.getMoviesFromJSON(data);

        if (movies != null && !movies.isEmpty()) {
           // if (page==1) {
            if (prev_methodOfSort != methodOfSort) {
                movieAdapter.clear();
                viewModel.deleteAllMovies();
            }
            prev_methodOfSort = methodOfSort;
            for (Movie movie:movies){
                viewModel.insertMovie(movie);
            }
            movieAdapter.addMovies(movies);
            //movieAdapter.setMovies(movies);
            page ++;
            // recyclerViewPosters.setAdapter(movieAdapter);
        }
        isLoading = false;
        progressBarLoading.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_ID);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}