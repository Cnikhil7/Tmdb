package com.example.android.udacitymovieproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//this class contain methods that parses the received json and returns the relevant data
//requested by getter methods

public final class JsonUtils {

    public static ArrayList<MovieObject> getArrayList(String in) {

        ArrayList<MovieObject> movieTitle = new ArrayList<>();
        try {
            JSONObject results = new JSONObject(in);
            JSONArray resultsArray = results.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject jsonObject = resultsArray.getJSONObject(i);
                String title = jsonObject.getString("original_title");
                String poster = jsonObject.getString("poster_path");

                String id = jsonObject.getString("id");
                movieTitle.add(new MovieObject(title, id, poster));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return movieTitle;
    }

    public static MovieObject getMovieDetails(String data) {

        MovieObject movieObject = null;
        JSONObject results = null;
        try {
            results = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            if (results != null) {
                String title = results.getString("original_title");
                String poster = results.getString("poster_path");
                String synopsis = results.getString("overview");
                String rating = results.getString("vote_average");
                String year = results.getString("release_date");
                String runtime = results.getString("runtime");
                movieObject = new MovieObject(title, poster
                        , synopsis, rating, year, runtime);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return movieObject;
    }
}
