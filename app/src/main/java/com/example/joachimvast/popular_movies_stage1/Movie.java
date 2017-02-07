package com.example.joachimvast.popular_movies_stage1;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JoachimVAST on 07/02/2017.
 */

public class Movie {
    public String title;
    public String release;
    public String overview;
    public JSONObject movie;
    public String imagePath;
    public int rating;

    public Movie (String title, String release, String overview, JSONObject movie, int rating, String imagePath){
        this.title = title;
        this.release = release;
        this.overview = overview;
        this.movie = movie;
        this.rating = rating;
        this.imagePath = imagePath;
    }

    // Not-default constructor purely based on JSONObjects that we parse from our JSON String from our HTTP Response
    public Movie (JSONObject movie){
        this.movie = movie;

        // From the JSONObject we get the name-value pairs using the getString() method
        try {
            this.title = movie.getString("original_title");
            this.release = movie.getString("release_date");
            this.overview = movie.getString("overview");
            this.imagePath += "http://image.tmdb.org/t/p/w185" + movie.getString("poster_path");
            this.rating = movie.getInt("vote_average");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
