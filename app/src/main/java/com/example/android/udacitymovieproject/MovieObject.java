package com.example.android.udacitymovieproject;

import java.net.URL;


class MovieObject {
    private String mTitle;
    private String mPosterUrlKey;
    private String mId;


    MovieObject(String vTitle, String vid, String vPosterUrlKey) {
        mTitle = vTitle;
        mId = vid;
        mPosterUrlKey = vPosterUrlKey;
    }

    String getPosterUrl() {

        URL title = NetworkUtils.buildPosterUrl(mPosterUrlKey);
        return title.toString();
    }

    public String getId() {
        return mId;
    }

    String getTitle() {
        return mTitle;
    }


}
