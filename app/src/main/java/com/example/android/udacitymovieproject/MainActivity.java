package com.example.android.udacitymovieproject;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.udacitymovieproject.Activity.DetailsActivity;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>
        , MyAdapter.ItemClickListener {

    private static final String URL_QUERY = "url";
    private static final int QUERY_LOADER = 55;
    private static final String QUERY_RESULT = "results";
    private static final String Last_URL_QUERIED = "lastUrlQueried";
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private String queryResult;
    private String lastUrlQueried;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting support action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        //retrieving urlResult from savedInstanceState if any
        if (savedInstanceState != null) {
            queryResult = savedInstanceState.getString(QUERY_RESULT);
            lastUrlQueried = savedInstanceState.getString(Last_URL_QUERIED);
        }

        mLoadingIndicator = (ProgressBar) findViewById(R.id.main_progress_bar);
        mErrorMessage = (TextView) findViewById(R.id.main_error_message);

        //initially passing in popular to create a url that provides popular movies
        //to inflate on startup
        if (lastUrlQueried == null || lastUrlQueried
                .equals(NetworkUtils.buildUrl("popular").toString())) {
            makeQuery("popular");
        } else {
            makeQuery("top_rated");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //choosing movies url to query from
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuSelected = item.getItemId();

        switch (menuSelected) {
            case R.id.action_popular:
                makeQuery("popular");
                return true;
            case R.id.action_top_rated:
                makeQuery("top_rated");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //this method builds the and passes the url to the loader using a bundle for getting response
    //from the server

    private void makeQuery(String in) {

        URL url = NetworkUtils.buildUrl(in);

        Bundle bundle = new Bundle();
        bundle.putString(URL_QUERY, url.toString());

        LoaderManager manager = getSupportLoaderManager();
        Loader<String> loader = manager.getLoader(QUERY_LOADER);

        if (loader == null) {
            manager.initLoader(QUERY_LOADER, bundle, this);
        } else {
            manager.restartLoader(QUERY_LOADER, bundle, this);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                //checking if input arguments are null and simply returning if true
                if (args == null) {
                    return;
                }

                //checking if results are already stored from previous similar request
                // and simply delivering data by overPassing loadInBackground to prevent
                //redundant api calls

                String query = args.getString(URL_QUERY);

                if (lastUrlQueried == null) {
                    lastUrlQueried = query;
                }

                if (query != null && !query.equals(lastUrlQueried)) {
                    lastUrlQueried = query;
                    queryResult = null;
                }

                showProgressbar();

                if (queryResult != null) {
                    deliverResult(queryResult);
                } else {
                    forceLoad();
                }
            }

            //this method fetches the url string from bundle and calls get response method
            //from network utils

            @Override
            public String loadInBackground() {


                String searchUrl = args.getString(URL_QUERY);
                if (searchUrl == null || TextUtils.isEmpty(searchUrl)) {
                    return null;
                }
                try {
                    URL url = new URL(searchUrl);
                    lastUrlQueried = url.toString();
                    return NetworkUtils.getResponseFromHttpUrl(url);

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                queryResult = data;
                super.deliverResult(data);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }
        };

    }

    // this method receives the data from load in background and calls JsonUtils class
    // for parsing data and calling displayResult method on data

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (data != null) {
            ArrayList<MovieObject> list = JsonUtils.getArrayList(data);
            displayResult(list);
        } else showErrorMessage();

    }

    private void displayResult(ArrayList<MovieObject> in) {

        if (in != null) {

            int span = 2;
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                span = 4;
            }

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_gridRecyclerView);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, span);
            RecyclerView.Adapter adapter = new MyAdapter(this, in, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);

            mLoadingIndicator.setVisibility(View.INVISIBLE);
        } else showErrorMessage();

    }

    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showProgressbar() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    //saving queryResults so it's not lost on rotation of device
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString(QUERY_RESULT, queryResult);
        outState.putString(Last_URL_QUERIED, lastUrlQueried);
    }


    //implementing interface onItemClick method from adapter class
    @Override
    public void onItemClick(String id) {
        Toast.makeText(getApplicationContext(), "Item " + id + " clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("MOVIE_ID", id);
        startActivity(intent);
    }
}
