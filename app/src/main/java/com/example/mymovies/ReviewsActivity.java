package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mymovies.adapters.ReviewAdapter;
import com.example.mymovies.data.MainViewModel;
import com.example.mymovies.data.Movie;
import com.example.mymovies.data.Review;
import com.example.mymovies.utils.JSONUtils;
import com.example.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {

    private TextView textViewReviewName;

    private ReviewAdapter reviewAdapter;


    private RecyclerView recyclerViewReview;

    private Movie movie;

    private int id_movie;

    private MainViewModel viewModel;


    @Override
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        textViewReviewName = findViewById(R.id.textViewReviewTittleName);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id_movie = intent.getIntExtra("id", -1);

        } else {
            finish();
        }

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        movie = viewModel.getMovieById(id_movie);

        textViewReviewName.setText(movie.getTitle());
        recyclerViewReview = findViewById(R.id.recyclerviewReviews);
        reviewAdapter = new ReviewAdapter();
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReview.setAdapter(reviewAdapter);
        JSONObject jsonObjectReview = NetworkUtils.getJSONForReviews(1,movie.getId());
        ArrayList<Review> reviews = JSONUtils.getReviewsFromJSON(jsonObjectReview);
        reviewAdapter.setReviews(reviews);
    }
}