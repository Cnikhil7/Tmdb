package com.example.android.udacitymovieproject;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by NIKHIL on 3/2/2017.
 */


public class NetworkUtils {

    private final static String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String API_URL = "http://api.themoviedb.org/3";
    private final static String Movie_BASE_URL_ONE ="https://api.themoviedb.org/3/movie";
    private final static String API_KEY = "your api key goes here";
    private final static String API_APPEND = "api_key";
    private final static String LANGUAGE_TYPE = "language";
    private final static String PAGE = "page";
    private static final String MOVIE_QUERY = "movie";
    private final static String POSTER_SIZE = "w185";


    //method for building base url to get response from get response method

    public static URL buildUrl(String in) {

        Uri buildUri = Uri.parse(API_URL).buildUpon()
                .appendPath(MOVIE_QUERY).appendPath(in)
                .appendQueryParameter(API_APPEND, API_KEY)
                .appendQueryParameter(LANGUAGE_TYPE, "en-US")
                .appendQueryParameter(PAGE, "1")
                .build();

        URL url = null;

        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    //this method takes the input id string received in json and creates and returns
    //the poster url corresponding to that id;

    public static URL buildPosterUrl(String in) {

        Uri buildUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .build();
        URL urlOne = null;
        try {
            urlOne = new URL(buildUri.toString().concat(POSTER_SIZE).concat(in));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urlOne;

    }

    //this method builds the url for a specific movie id
    public static URL MovieDetailURl(String id) {
        Uri buildUri = Uri.parse(Movie_BASE_URL_ONE).buildUpon()
                .appendPath(id).appendQueryParameter(API_APPEND,API_KEY)
                .appendQueryParameter(LANGUAGE_TYPE,"en-US").build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    //method for getting response from url

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else return null;
        } finally {
            urlConnection.disconnect();
        }
    }


}
