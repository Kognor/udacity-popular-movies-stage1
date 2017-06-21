package com.klogan.popularmoviesstage1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.klogan.popularmoviesstage1.R;
import com.klogan.popularmoviesstage1.activities.MovieDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.Locale;

/**
 * A Fragment containing the details for a specific movie.
 */
public class MovieDetailFragment extends Fragment {

    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Note: an enhancement would be to use something like Parcelable and AutoValue to handle
        // storing and retrieving the entire Movie object. But for simplicity, I just stored what
        // was needed for stage 1.
        Intent activityIntent = getActivity().getIntent();
        String movieTitle = activityIntent.getStringExtra(MovieDetailActivity.MOVIE_TITLE);
        String posterUrl = activityIntent.getStringExtra(MovieDetailActivity.MOVIE_POSTER_URL);
        String releaseDateString = activityIntent.getStringExtra(MovieDetailActivity.MOVIE_RELEASE_DATE);
        Double userRating = activityIntent.getDoubleExtra(MovieDetailActivity.MOVIE_USER_RATING, 0);
        String plotOverview = activityIntent.getStringExtra(MovieDetailActivity.MOVIE_PLOT_OVERVIEW);

        // Note: an enhancement here would be to use a library that does this automatically.
        TextView movieTitleTextView = (TextView)rootView.findViewById(R.id.movie_detail_title);
        movieTitleTextView.setText(movieTitle);

        ImageView imageView = (ImageView)rootView.findViewById(R.id.movie_detail_poster_image);
        Picasso.with(getActivity()).load(posterUrl).into(imageView);

        String releaseYearString = releaseDateString.substring(0, 4);
        TextView movieReleaseDateTextView = (TextView)rootView.findViewById(R.id.movie_detail_release_date);
        movieReleaseDateTextView.setText(releaseYearString);

        TextView userRatingTextView = (TextView)rootView.findViewById(R.id.movie_detail_user_rating);
        String userRatingString = String.format(Locale.getDefault(), "%.1f/10", userRating);
        userRatingTextView.setText(userRatingString);

        TextView plotOverviewTextView = (TextView)rootView.findViewById(R.id.movie_detail_plot);
        plotOverviewTextView.setText(plotOverview);

        return rootView;
    }
}
