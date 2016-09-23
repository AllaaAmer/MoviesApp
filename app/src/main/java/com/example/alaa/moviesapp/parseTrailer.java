package com.example.alaa.moviesapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Models.Review;
import Models.Trailer;

/**
 * Created by alaa on 9/16/2016.
 */
public class parseTrailer {

    public static List<Trailer> parseResponse(String ResponseStr, int id) {
        List<Trailer> TrailerList = new ArrayList<>();
        try {
            JSONObject response = new JSONObject(ResponseStr);
            int ID = response.getInt("id");

            if (ID == id) {
                JSONArray Trailers = response.getJSONArray("results");

                for (int i = 0; i < Trailers.length(); i++) {
                    JSONObject trailer = Trailers.getJSONObject(i);
                    TrailerList.add(parse_trailer(trailer.toString()));
                }

                return TrailerList;
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Trailer parse_trailer(String ObjectStr) {
        try {
            JSONObject trailer = new JSONObject(ObjectStr);

            String id = trailer.getString("id");
            String iso_639_1 = trailer.getString("iso_639_1");
            String key = trailer.getString("key");
            String name = trailer.getString("name");
            String site = trailer.getString("site");
            String type = trailer.getString("type");

          //  if (type == "Trailer")
                return new Trailer(id, iso_639_1, key, name, site, type);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
