package com.example.alaa.moviesapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import Models.Movie;

public class MainActivity extends AppCompatActivity implements MovieAdapter.Listener, SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = "Main Activity";
    private static final String ITEM_STATE = "itemState";
    private Parcelable mitemState = null;

    /*UI*/
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    int lastFirstVisiblePosition;
    DatabaseHelper dbHelper;

    //fields
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_movies);

        //assign the value of mTwoPane
        mTwoPane = findViewById(R.id.details_frameLayout) != null;
        if (mTwoPane == true) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else if (mTwoPane == false) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        // setup the adapter
        movieAdapter = new MovieAdapter(this);
        movieAdapter.setListener(this);

        // bind the adapter to the list view
        recyclerView.setAdapter(movieAdapter);

        // get data from intent
        if (getIntent() != null && getIntent().hasExtra("Favourite")) {
           dbHelper = new DatabaseHelper(this);
            List<Movie> FavMovies = dbHelper.getMovies();
            movieAdapter.setData(FavMovies);

        } else {
            List<Movie> EmptyList = new ArrayList<>();
            movieAdapter.setData(EmptyList);
            loadData();
        }

        // reference the swipeRefresh
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_setting) {
            Log.v(LOG_TAG, "setting Clicked");
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        if (getIntent() != null && getIntent().hasExtra("Favourite")) {
            setIntent(null);
        } else {
            loadData();
        }
        super.onStart();
    }

    public void loadData() {
        String key = getString(R.string.pref_sortOrder_key);
        String defaultValue = getString(R.string.pref_sortOrder_most_popular);
        String SortBy = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString(key, defaultValue);

        Uri builtUri = Uri.parse("https://api.themoviedb.org/3/discover/movie?api_key=10a9d669a2da4a5802ae24830c3a394d").buildUpon()
                .appendQueryParameter("sort_by", SortBy)
                .build();
        String url = builtUri.toString();
        Log.d("Movie", "url = " + url);

        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String jsonString) {

                        swipeRefreshLayout.setRefreshing(false);

                        Log.d("Movie", "result = " + jsonString);

                        // check error
                        if (e != null) {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // parse the data
                        List<Movie> moviesList = parseMovies.parseResponse(jsonString);
                        if (moviesList == null) {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        movieAdapter.setData(moviesList);
                    }
                });
    }

    @Override
    public void onnMovieClick(Movie movie) {

        if (mTwoPane) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.details_frameLayout, DetailsFragment.newInstance(movie))
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("MovieExtra", movie);
            Log.d(LOG_TAG, movie.getOriginalTitle() + " in MainActivity");
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        loadData();
    }


    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mitemState = state.getParcelable(ITEM_STATE);


    }

    @Override
    protected void onResume() {
        super.onResume();
       // loadData();
        if (mitemState != null)
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view_movies);
        recyclerView.getLayoutManager().onRestoreInstanceState(mitemState);
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);
        mitemState = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        mitemState = recyclerView.getLayoutManager().onSaveInstanceState();
        lastFirstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        state.putParcelable(ITEM_STATE, mitemState);
    }
}
