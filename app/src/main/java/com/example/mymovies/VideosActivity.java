package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovies.adapters.ReviewAdapter;
import com.example.mymovies.adapters.TrailerAdapter;
import com.example.mymovies.data.MainViewModel;
import com.example.mymovies.data.Movie;
import com.example.mymovies.data.Review;
import com.example.mymovies.data.Trailer;
import com.example.mymovies.utils.JSONUtils;
import com.example.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;

public class VideosActivity extends AppCompatActivity {


    private TextView textViewVideoName;

    private Movie movie;

    private int id_movie;

     private TrailerAdapter trailerAdapter;

    private RecyclerView recyclerViewTrailer;
    private MainViewModel viewModel;


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rev_vid_menu, menu);
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
        }else if (id == R.id.item_Detail) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public String changeURL(String URL) {
        String result = URL.replace("embed/","watch?v=");
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        textViewVideoName= findViewById(R.id.textViewVideoName);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id_movie = intent.getIntExtra("id", -1);

        } else {
            finish();
        }
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        movie = viewModel.getMovieById(id_movie);

        textViewVideoName.setText(movie.getTitle());

        recyclerViewTrailer = findViewById(R.id.recyclerviewTrailers);
        trailerAdapter = new TrailerAdapter();
        trailerAdapter.setOnTrailerClickListener(new TrailerAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailrerClick(String url) {
                Toast.makeText(VideosActivity.this,changeURL(url),Toast.LENGTH_SHORT).show();
                Intent intentToTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(changeURL(url)));
                startActivity(intentToTrailer);
            }
        });
        recyclerViewTrailer.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailer.setAdapter(trailerAdapter);
        JSONObject jsonObjectTrailer = NetworkUtils.getJSONForVideos(1,movie.getId());
        ArrayList<Trailer> trailers = JSONUtils.getVideosFromJSON(jsonObjectTrailer);
        trailerAdapter.setTrailers(trailers);
    }
}