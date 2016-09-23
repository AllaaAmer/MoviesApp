package com.example.alaa.moviesapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import Models.Movie;

/**
 * Created by alaa on 8/18/2016.
 */
public class parseMovies {

    public static Movie parseMovie(String ObjectStr) {

        try {
            JSONObject movie = new JSONObject(ObjectStr);

            Integer id = movie.getInt("id");
            String overview = movie.getString("overview");
            String original_title = movie.getString("original_title");
            String poster_path = movie.getString("poster_path");
            Float vote_average = Float.valueOf(movie.getString("vote_average"));
            String release_date = movie.getString("release_date");

            return new Movie(id , original_title , overview , poster_path , vote_average , release_date );

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Movie> parseResponse(String ResponseStr) {
        List<Movie> MoviesList = new ArrayList<>();

       // if (jsonStr != null) {
        try {
            JSONObject response = new JSONObject(ResponseStr);
            int status = response.getInt("page");

            if (status == 1) {
                JSONArray movies = response.getJSONArray("results");

                        for (int i = 0; i < movies.length(); i++) {
                            JSONObject movie = movies.getJSONObject(i);

                            MoviesList.add(parseMovie(movie.toString()));}
                return MoviesList;


            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
