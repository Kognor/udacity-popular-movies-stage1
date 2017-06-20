package com.klogan.popularmoviesstage1.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.klogan.popularmoviesstage1.R;
import com.klogan.popularmoviesstage1.domain.Movie;
import com.klogan.popularmoviesstage1.fragments.MovieDetailFragment;

/**
 * Activity showing details for a specific movie.
 */
public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_TITLE = "movieTitle";
    public static final String MOVIE_POSTER_URL = "moviePosterUrl";
    public static final String MOVIE_RELEASE_DATE = "movieReleaseDate";
    public static final String MOVIE_USER_RATING = "movieUserRating";
    public static final String MOVIE_PLOT_OVERVIEW = "moviePlotOverview";

    public static Intent getIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MOVIE_TITLE, movie.getTitle());
        intent.putExtra(MOVIE_POSTER_URL, movie.getPosterImageUrl());
        intent.putExtra(MOVIE_RELEASE_DATE, movie.getReleaseDate());
        intent.putExtra(MOVIE_USER_RATING, movie.getUserRating());
        intent.putExtra(MOVIE_PLOT_OVERVIEW, movie.getPlotOverview());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new MovieDetailFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = SettingsActivity.getIntent(this);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
