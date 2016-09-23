package com.example.alaa.moviesapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import Models.Review;

/**
 * Created by alaa on 9/16/2016.
 */
public class parseReview {

    public static List<Review> parseResponse(String ResponseStr) {
        List<Review> ReviewList = new ArrayList<>();
        try {
            JSONObject response = new JSONObject(ResponseStr);
            int status = response.getInt("page");

            if (status == 1) {
                JSONArray reviews = response.getJSONArray("results");

                for (int i = 0; i < reviews.length(); i++) {
                    JSONObject review = reviews.getJSONObject(i);
                    ReviewList.add(parse_review(review.toString()));}

                return ReviewList;
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Review parse_review(String ObjectStr) {

        try {
            JSONObject review = new JSONObject(ObjectStr);
            String id = review.getString("id");
            String author = review.getString("author");
            String content = review.getString("content");
            String url = review.getString("url");

            return new Review(id , author , content , url );

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
