package com.example.android.udacitymovieproject.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.udacitymovieproject.R;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        //accessing the extra string passed in intent from main activity
        Intent intent = getIntent();
        String title = intent.getStringExtra("MOVIE_ID");

        TextView textView = (TextView) findViewById(R.id.detail_itemTitle);
        textView.setText(title);

        ImageView poster = (ImageView)findViewById(R.id.detail_itemPoster);
        Picasso.with(this).load(intent.getStringExtra("POSTER_ID")).into(poster);
    }
}
