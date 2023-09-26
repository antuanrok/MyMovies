package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovies.adapters.ReviewAdapter;
import com.example.mymovies.adapters.TrailerAdapter;
import com.example.mymovies.data.FavouriteMovie;
import com.example.mymovies.data.MainViewModel;
import com.example.mymovies.data.Movie;
import com.example.mymovies.data.Review;
import com.example.mymovies.data.Trailer;
import com.example.mymovies.utils.JSONUtils;
import com.example.mymovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private ImageView imageViewBigPoster;
    private ImageView imageViewAddToFavourite;
    private TextView textViewTittle;
    private TextView textViewOriginalTitle;
    private TextView textViewReleaseDate;
    private TextView textViewRating;

    private TextView textViewVotes;
    private TextView textViewOverview;

    private int id_movie;

    private Movie movie;

    FavouriteMovie favouriteMovie;

    private MainViewModel viewModel;
    private ScrollView scrollViewinfo;

   // private TrailerAdapter trailerAdapter;
//    private ReviewAdapter reviewAdapter;

//    private RecyclerView recyclerViewTrailer;
 //   private RecyclerView recyclerViewReview;

    //private int id_Movie;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
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
        } else if (id == R.id.item_Reviews) {
            Intent intent = new Intent(this, ReviewsActivity.class);
            intent.putExtra("id", id_movie);
            startActivity(intent);
        }else if (id == R.id.item_Videos) {
            Intent intent = new Intent(this, VideosActivity.class);
            intent.putExtra("id", id_movie);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

  /*  public String changeURL(String URL) {
       String result = URL.replace("embed/","watch?v=");
       return result;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        imageViewAddToFavourite = findViewById(R.id.imageViewAddToFavourite);
        //imageViewAddToFavourite.setBackgroundResource(R.color.back_lite_disabled);
        textViewTittle = findViewById(R.id.textViewTitle);
        textViewReleaseDate = findViewById(R.id.textViewDate);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textViewVotes = findViewById(R.id.textViewVotes);
        textViewOverview = findViewById(R.id.textViewOverview);
        scrollViewinfo = findViewById(R.id.scrollView_info);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id_movie = intent.getIntExtra("id", -1);

        } else {
            finish();
        }
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        movie = viewModel.getMovieById(id_movie);

        Picasso.get().load(movie.getPosterPath()).placeholder(R.drawable.placeholder).into(imageViewBigPoster);
        textViewTittle.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle());
        textViewReleaseDate.setText(Integer.toString(movie.getReleaseDate()));
        textViewRating.setText(Double.toString(movie.getRating()));
        textViewVotes.setText(Integer.toString(movie.getVote_count()));
        textViewOverview.setText(movie.getOverwiev());
        setFavourite();


       /*recyclerViewTrailer = findViewById(R.id.recyclerviewTrailers);
        recyclerViewReview = findViewById(R.id.recyclerviewReviews);
        trailerAdapter = new TrailerAdapter();
        trailerAdapter.setOnTrailerClickListener(new TrailerAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailrerClick(String url) {
                Toast.makeText(DetailActivity.this,changeURL(url),Toast.LENGTH_SHORT).show();
                Intent intentToTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(changeURL(url)));
                startActivity(intentToTrailer);
            }
        });
        reviewAdapter = new ReviewAdapter();
        recyclerViewTrailer.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailer.setAdapter(trailerAdapter);
        recyclerViewReview.setAdapter(reviewAdapter);
        JSONObject jsonObjectTrailer = NetworkUtils.getJSONForVideos(1,movie.getId());
        JSONObject jsonObjectReview = NetworkUtils.getJSONForReviews(1,movie.getId());
        ArrayList<Trailer> trailers = JSONUtils.getVideosFromJSON(jsonObjectTrailer);
        ArrayList<Review> reviews = JSONUtils.getReviewsFromJSON(jsonObjectReview);
        reviewAdapter.setReviews(reviews);
        trailerAdapter.setTrailers(trailers);*/
        scrollViewinfo.smoothScrollTo(0,0);
    }

    public void onClickChangeFavourite(View view) {
        if (favouriteMovie == null) {
            viewModel.insertFavouriteMovie(new FavouriteMovie(movie));
            Toast.makeText(this, R.string.add_to_favourite, Toast.LENGTH_SHORT).show();
        } else {
            viewModel.deleteFavouriteMovie(favouriteMovie);
            Toast.makeText(this, R.string.del_from_favourite, Toast.LENGTH_SHORT).show();
        }
        setFavourite();
    }

    private void setFavourite() {
        favouriteMovie = viewModel.getFavouriteMovieById(id_movie);
        if (favouriteMovie == null) {
            imageViewAddToFavourite.setImageResource(R.drawable.btn_star_big_off);
        } else {
            imageViewAddToFavourite.setImageResource(R.drawable.btn_star_big_on);
        }
    }
}