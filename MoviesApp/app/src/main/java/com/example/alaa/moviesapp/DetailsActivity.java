package com.example.alaa.moviesapp;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import Models.Movie;

public class DetailsActivity extends AppCompatActivity {

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

         //to draw the back title
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      // get data from intent
        if (getIntent() != null && getIntent().hasExtra("MovieExtra")) {
            movie = (Movie) getIntent().getSerializableExtra("MovieExtra");
            Log.d("DetailsActivity", movie.getOriginalTitle());
        }

        // create details fragment
        DetailsFragment detailsFragment = DetailsFragment.newInstance(movie);
        if ( getSupportFragmentManager().findFragmentById(R.id.details_frameLayout) == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.details_frameLayout, detailsFragment).commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
                Intent myIntent = new Intent(this, MainActivity.class);
                startActivityForResult(myIntent, 0);
                return true;
            }

        return super.onOptionsItemSelected(item);
    }

    public static Intent CreateIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("MovieExtra" , movie);
        Log.d("Detailsfragment", movie.getTitle() + " in DetailsActivity");
        return intent;
    }
}
