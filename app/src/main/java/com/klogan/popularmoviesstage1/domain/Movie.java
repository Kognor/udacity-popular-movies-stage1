package com.klogan.popularmoviesstage1.domain;

import android.net.Uri;

/**
 * Domain object to represent a movie. Note: an enhancement to this class would be to use
 * something like AutoValue
 */
public class Movie {

    // These static variables are used for keys from JSON results returned by the moviedb project.
    public static final String TITLE_KEY = "title";

    public static final String POSTER_THUMBNAIL_PATH_KEY = "poster_path";

    public static final String PLOT_OVERVIEW_KEY = "overview";

    public static final String USER_RATING_KEY = "vote_average";

    public static final String RELEASE_DATE_KEY = "release_date";

    // Sample image: https://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p";

    private static final String IMAGE_SIZE = "w185";

    private String title;

    private String posterThumbnailPath;

    private String plotOverview;

    private Double userRating;

    private String releaseDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterThumbnailPath() {
        return posterThumbnailPath;
    }

    public void setPosterThumbnailPath(String posterThumbnailPath) {
        this.posterThumbnailPath = posterThumbnailPath;
    }

    public String getPlotOverview() {
        return plotOverview;
    }

    public void setPlotOverview(String plotOverview) {
        this.plotOverview = plotOverview;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterImageUrl() {
        return Uri.parse(Movie.IMAGE_BASE_URL)
                .buildUpon()
                .appendEncodedPath(Movie.IMAGE_SIZE + posterThumbnailPath)
                .toString();
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", posterThumbnailPath='" + posterThumbnailPath + '\'' +
                ", plotOverview='" + plotOverview + '\'' +
                ", userRating=" + userRating +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
