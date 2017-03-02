package com.example.android.udacitymovieproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//this class contain methods that parses the received json and returns the relevant data
//requested by getter methods

final class JsonUtils {

    static ArrayList<MovieObject> getArrayList(String in) {

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

}
