package com.klogan.popularmoviesstage1.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.klogan.popularmoviesstage1.R;
import com.klogan.popularmoviesstage1.activities.MovieDetailActivity;
import com.klogan.popularmoviesstage1.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for a grid view of movies.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = "MOVIE_ADAPTER";

    private LayoutInflater layoutInflater;

    public MovieAdapter(Context context, List<Movie> movieList) {
        super(context, R.layout.grid_item_movie, R.id.grid_item_movie_imageview, movieList);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_item_movie, parent, false);
        }

        final Movie movie = getItem(position);

        String imageUrl = null;

        if (movie == null) {
            Log.e(LOG_TAG, "Movie is null at position: " + position);
        } else {
            imageUrl = movie.getPosterImageUrl();
        }

        ImageView imageView = (ImageView) convertView;

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MovieDetailActivity.getIntent(getContext(), movie);
                getContext().startActivity(intent);
            }
        });

        Picasso.with(getContext()).load(imageUrl).into(imageView);

        return convertView;
    }
}
