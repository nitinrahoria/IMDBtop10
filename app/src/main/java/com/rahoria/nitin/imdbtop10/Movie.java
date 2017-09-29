package com.rahoria.nitin.imdbtop10;

/**
 * Created by nitin on 9/29/2017.
 */

public class Movie {

    private String title, image, year, rating, url;

    public Movie() {
    }

    public Movie(String title, String image, String year, String rating, String url) {
        this.title = title;
        this.image = image;
        this.year = year;
        this.rating = rating;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "title: "+title+", rating: "+rating+", image: "+image+", year : "+year;
    }
}
