package com.example.taskproject.model;

import android.text.SpannableString;

public class Movie {

    private String imageURL;
    private String link;
    private String title;
    private String actors;
    private String pubDate;
    private String director;
    private double userRating;

    public Movie(String imageURL, String link, String title, String pubDate, String director, String actors, double userRating) {
        this.imageURL = imageURL;
        this.link = link;
        this.title = title;
        this.pubDate = pubDate;
        this.director = director;
        this.actors = actors;
        this.userRating = userRating;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }
}
