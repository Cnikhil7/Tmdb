package com.example.android.udacitymovieproject.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.udacitymovieproject.JsonUtils;
import com.example.android.udacitymovieproject.MovieObject;
import com.example.android.udacitymovieproject.NetworkUtils;
import com.example.android.udacitymovieproject.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.net.URL;

import static com.squareup.picasso.Picasso.with;

public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {

    private static final String MOVIE_DETAIL_URL = "url";
    private static final int DETAIL_LOADER = 25;
    private static final String URL_RESULT = "queryResult";
    private String urlResult;
    private TextView mTitle;
    private TextView mReleaseYear;
    private TextView mRating;
    private TextView mRuntime;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;
    private ImageView mPoster;
    private TextView mSynopsis;
    private Boolean mTag = true;
    private Target mTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //checking for saved instance state for saved json file in url result variable

        if (savedInstanceState != null) {
            urlResult = savedInstanceState.getString(URL_RESULT);
        }


        //accessing the extra string passed in intent from main activity
        Intent intent = getIntent();
        String title = intent.getStringExtra("MOVIE_ID");


        //getting references to the xml elements

        mTitle = (TextView) findViewById(R.id.detail_itemTitle);
        mReleaseYear = (TextView) findViewById(R.id.details_itemRelease);
        mRating = (TextView) findViewById(R.id.detail_itemRatings);
        mRuntime = (TextView) findViewById(R.id.detail_itemRuntime);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.detail_progressBar);
        mErrorMessage = (TextView) findViewById(R.id.detail_errorMessage);
        mPoster = (ImageView) findViewById(R.id.detail_itemPoster);
        mSynopsis = (TextView)findViewById(R.id.detail_itemSynopsis);
        toggleSynopsis();
        makeQuery(title);

    }

    private void toggleSynopsis() {
        // toggling synopsis text view for full summary
        mSynopsis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTag) {
                    mSynopsis.setMaxLines(Integer.MAX_VALUE);
                    mTag = false;
                } else {
                    mSynopsis.setMaxLines(4);
                    mTag = true;
                }
            }
        });
    }


    //this method invoke the loader with an appropriate url received from NetworkUtils class

    private void makeQuery(String id) {

        URL url = NetworkUtils.MovieDetailURl(id);
        Bundle bundle = new Bundle();
        bundle.putString(MOVIE_DETAIL_URL, url.toString());

        LoaderManager manager = getSupportLoaderManager();
        Loader<String> loader = manager.getLoader(DETAIL_LOADER);

        if (loader == null) {
            manager.initLoader(DETAIL_LOADER, bundle, this);
        } else {
            manager.restartLoader(DETAIL_LOADER, bundle, this);
        }

    }

    //loader method responsible for taking in the url and fetching result
    // in background thread

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }

                mLoadingIndicator.setVisibility(View.VISIBLE);


                //checks if results are already present

                if (urlResult != null) {
                    deliverResult(urlResult);

                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {

                String query = args.getString(MOVIE_DETAIL_URL);

                if (query == null || TextUtils.isEmpty(query)) {
                    return null;
                }
                try {
                    URL url = new URL(query);
                    query = NetworkUtils.getResponseFromHttpUrl(url);
                    return query;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(String data) {
                if (args.getString(MOVIE_DETAIL_URL) != null) {
                    urlResult = data;
                }


                super.deliverResult(data);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        if (data != null && !data.equals("")) {
            MovieObject item = JsonUtils.getMovieDetails(data);
            displayData(item);
        } else {
            showErrorMessage();
        }
    }

    private void displayData(MovieObject item) {
        if (item != null) {

            String title = item.getTitle();
            String rating = item.getRating();
            String runtime = item.getRuntime();
            String posterUrl = item.getPosterUrl();
            String year = item.getReleaseDate();
            String synopsis = item.getSypnosis();
            getPoster(posterUrl);

            mRating.setText(rating.concat("/10"));
            mReleaseYear.setText(year.substring(0, 4));
            mTitle.setText(title);
            mRuntime.setText(runtime.concat(" minutes"));
            mSynopsis.setText(synopsis);

        } else showErrorMessage();

    }


    //this method displays the poster in details activity and also uses palette
    //class for grabbing header color

    private void getPoster(String url) {


        with(this).load(url).resize(300, 450)
                .into(mTarget = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        mPoster.setImageBitmap(bitmap);

                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

                            @Override
                            public void onGenerated(Palette palette) {

                                Palette.Swatch swatch = palette.getVibrantSwatch();

                                if (swatch == null) {
                                    swatch = palette.getLightMutedSwatch();
                                    if (swatch != null) {

                                        mTitle.setBackgroundColor(swatch.getRgb());
                                    } else
                                        Toast.makeText(getApplicationContext(), "swatch null", Toast.LENGTH_SHORT).show();
                                } else {

                                    mTitle.setBackgroundColor(swatch.getRgb());
                                }
                            }


                        });
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

        mLoadingIndicator.setVisibility(View.INVISIBLE);

    }

    //displays error message and hide loading indicator

    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    //saving the query results
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(URL_RESULT, urlResult);
    }


}
