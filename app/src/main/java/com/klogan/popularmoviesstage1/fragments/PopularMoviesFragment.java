package com.klogan.popularmoviesstage1.fragments;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.klogan.popularmoviesstage1.BuildConfig;
import com.klogan.popularmoviesstage1.R;
import com.klogan.popularmoviesstage1.adapters.MovieAdapter;
import com.klogan.popularmoviesstage1.domain.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment containing a simple grid view of popular movies.
 */
public class PopularMoviesFragment extends Fragment {

    private static final String MOVIE_DB_API_BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String PAGE_QUERY_PARAM = "page";

    private static final String API_KEY_QUERY_PARAM = "api_key";

    private MovieAdapter movieArrayAdapter;

    public PopularMoviesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        movieArrayAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());

        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);

        GridView popularMoviesGridView = (GridView)rootView.findViewById(R.id.gridview_popular_movies);

        popularMoviesGridView.setAdapter(movieArrayAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Note: this would be a great place to inject an interface containing a method called
        // updateMoviesList(). Then the implementation can be substituted with
        // an AsyncTask, RxJava, or any other concrete implementation to play with.
        updateMoviesList();
    }

    private void updateMoviesList() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = sharedPreferences.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));
        DownloadMoviesListTask downloadMoviesListTask = new DownloadMoviesListTask();
        downloadMoviesListTask.execute(sortBy);
    }

    private class DownloadMoviesListTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = DownloadMoviesListTask.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(String... params) {

            if (params.length == 0) {
                throw new IllegalArgumentException("No 'params' given to download movies list");
            }

            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String movieInputData = null;

            String queryPath = params[0];

            Uri builtUri = Uri.parse(MOVIE_DB_API_BASE_URL)
                    .buildUpon()
                    .appendPath(queryPath)
                    .appendQueryParameter(PAGE_QUERY_PARAM, "1")
                    .appendQueryParameter(API_KEY_QUERY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .build();

            try {
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    Log.w(LOG_TAG, "Empty InputStream from urlConnection");
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder inputData = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    inputData.append(line);
                    inputData.append("\n");
                }

                if (inputData.length() == 0) {
                    Log.i(LOG_TAG, "Stream was empty, no point in parsing results");
                    return null;
                }

                movieInputData = inputData.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem occurred downloading movieList", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing bufferedReader", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieInputData);
            } catch (JSONException jsonException) {
                Log.i(LOG_TAG, "Movie Input Data: " + movieInputData);
                Log.e(LOG_TAG, "Problem parsing json", jsonException);
            }

            // An error occurred parsing content.
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movieList) {
            if (movieList != null) {
                movieArrayAdapter.clear();
                movieArrayAdapter.addAll(movieList);
            }
        }

        private List<Movie> getMovieDataFromJson(String movieInputDataJsonString) throws JSONException {
            List<Movie> movieList = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(movieInputDataJsonString);
            JSONArray jsonArrayResults = jsonObject.getJSONArray("results");
            if (jsonArrayResults == null || jsonArrayResults.length() == 0) {
                Log.i(LOG_TAG, "No results for movieInputDataJsonString: " + movieInputDataJsonString);
                return movieList;
            }

            for (int k = 0;k < jsonArrayResults.length(); ++k) {
                JSONObject jsonObjectMovie = jsonArrayResults.getJSONObject(k);
                Movie movie = new Movie();
                movie.setTitle(jsonObjectMovie.getString(Movie.TITLE_KEY));
                movie.setPosterThumbnailPath(jsonObjectMovie.getString(Movie.POSTER_THUMBNAIL_PATH_KEY));
                movie.setPlotOverview(jsonObjectMovie.getString(Movie.PLOT_OVERVIEW_KEY));
                movie.setUserRating(jsonObjectMovie.getDouble(Movie.USER_RATING_KEY));
                movie.setReleaseDate(jsonObjectMovie.getString(Movie.RELEASE_DATE_KEY));
                movieList.add(movie);
            }

            return movieList;
        }
    }
}
