package com.example.android.udacitymovieproject;

import java.net.URL;


public class MovieObject {
    private String mSypnosis;
    private String mRating;
    private String mReleaseDate;
    private String mRuntime;
    private String mTitle;
    private String mPosterUrlKey;
    private String mId;


    MovieObject(String vTitle,String vPosterUrl,String vSypnosis,String vRating, String vReleaseDate
            ,String vRuntime){
        mTitle = vTitle;
        mPosterUrlKey = vPosterUrl;
        mSypnosis = vSypnosis;
        mRating = vRating;
        mReleaseDate = vReleaseDate;
        mRuntime = vRuntime;
    }

    public MovieObject(String vTitle, String vid, String vPosterUrlKey) {
        mTitle = vTitle;
        mId = vid;
        mPosterUrlKey = vPosterUrlKey;
    }

    public String getPosterUrl() {

        URL title = NetworkUtils.buildPosterUrl(mPosterUrlKey);
        return title.toString();
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }


    public String getSypnosis() {
        return mSypnosis;
    }

    public String getRating() {
        return mRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getRuntime() {
        return mRuntime;
    }
}
