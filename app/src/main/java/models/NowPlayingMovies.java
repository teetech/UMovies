package models;

import android.widget.RatingBar;

/**
 * Created by Anthony awuzie on 5/10/2017.
 * email:anthonyawuzie@gmail.com
 */

public class NowPlayingMovies extends MovieModel{
    @Override
    public String getMovieTitle() {
        return super.getMovieTitle();
    }

    @Override
    public void setMovieTitle(String movieTitle) {
        super.setMovieTitle(movieTitle);
    }

    @Override
    public String getPoster() {
        return super.getPoster();
    }

    @Override
    public void setPoster(String poster) {
        super.setPoster(poster);
    }

    @Override
    public String getReleaseDate() {
        return super.getReleaseDate();
    }

    @Override
    public void setReleaseDate(String releaseDate) {
        super.setReleaseDate(releaseDate);
    }

    @Override
    public float getRatingBar() {return super.getRatingBar();
    }

    @Override
    public void setRatingBar(float ratingBar) {
        super.setRatingBar(ratingBar);
    }

    @Override
    public String getOverview() {return super.getOverview();}

    @Override
    public void setOverview(String overview) {super.setOverview(overview);}
}
