package models;

import android.widget.RatingBar;

/**
 * Created by Anthony awuzie on 5/10/2017.
 * email:anthonyawuzie@gmail.com
 */

public  abstract class MovieModel {

    private String movieTitle;
    private String poster;
    private String releaseDate;
    private float ratingBar;
    private String overview;


    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {this.releaseDate = releaseDate;}

    public float getRatingBar() {return ratingBar;}

    public void setRatingBar(float ratingBar) {this.ratingBar = ratingBar;}

    public String getOverview() {return overview;}

    public void setOverview(String overview) { this.overview = overview;}
}
