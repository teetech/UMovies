package com.infoprogrammer.umovies.JsomModels;

/**
 * Created by Anthony awuzie on 5/10/2017.
 * email:anthonyawuzie@gmail.com
 */

public  abstract class MovieModel {

    private String movieTitle;
    private String poster;
    private String releasDate;
    private double ratting;


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

    public String getReleasDate() {
        return releasDate;
    }

    public void setReleasDate(String releasDate) {
        this.releasDate = releasDate;
    }

    public double getRatting() {
        return ratting;
    }

    public void setRatting(double ratting) {
        this.ratting = ratting;
    }
}
