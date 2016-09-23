package com.example.alaa.moviesapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.List;

import Models.Movie;
import Models.Review;
import Models.Trailer;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private static final String LOG_TAG = "Details Fragment";
    /* UI */
    TextView TitleTextView;
    TextView OverViewTextView;
    TextView ReleaseDateTextView;
    TextView RatingTextView;
    ImageView posterImageView;
    CheckBox checkBoxFav;

    List<Trailer> TrailerList;
    List<Review> ReviewList;
    Movie movie;

    LinearLayout TrailersLinearLayout;
    LinearLayout ReviewsLinearLayout;
    DatabaseHelper dbHelper;

    public DetailsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_favoraite) {
            Log.v(LOG_TAG, "Favourite Clicked");
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("Favourite", "from menu");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static DetailsFragment newInstance(Movie movie) {

        DetailsFragment fragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MovieExtra", movie);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Detailsfragment", "ay7aga");
        dbHelper = new DatabaseHelper(getContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // reference view
        TitleTextView = (TextView) view.findViewById(R.id.title_text_view);
        OverViewTextView = (TextView) view.findViewById(R.id.overview_text_view);
        ReleaseDateTextView = (TextView) view.findViewById(R.id.release_date_text_view);
        RatingTextView = (TextView) view.findViewById(R.id.user_rating_text_view);
        posterImageView = (ImageView) view.findViewById(R.id.movie_poster_image_view_details);

        // get data from the arguments
        movie = (Movie) getArguments().getSerializable("MovieExtra");
        TitleTextView.setText(movie.getOriginalTitle());
        OverViewTextView.setText(movie.getOverview());
        ReleaseDateTextView.setText(movie.getReleaseDate());
        String rate = RatingTextView.getText().toString();
        RatingTextView.setText(movie.getVoteAverage().toString()+rate);

        String baseURL = "http://image.tmdb.org/t/p/";
        String size = "w500";
        String ImageUrl = baseURL + "/" + size + "/" + movie.getPosterPath();
        Log.d("Image URL", ImageUrl);

        Picasso.with(getContext())
                .load(ImageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(posterImageView);

        // get the trailers
        TrailersLinearLayout = (LinearLayout) view.findViewById(R.id.trailer_LinearLayout);
        LoadTrailers(movie.getId());

        //get the reviews
        ReviewsLinearLayout = (LinearLayout) view.findViewById(R.id.review_LinearLayout);
        LoadReview(movie.getId());

        //checkbox checked
        checkBoxFav = (CheckBox) view.findViewById(R.id.favorite_movie_check);
        checkBoxFav.setOnClickListener(new View.OnClickListener() {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    dbHelper.insertMovie(movie);
                    checkBoxFav.setText("Un Fav me!");
                } else {
                    dbHelper.deleteMovie(movie.getId());
                    checkBoxFav.setText("Fav me!");
                }
            }
        });

        List<Movie> checkList = dbHelper.getMovies();
        if (checkList == null) {
            checkBoxFav.setChecked(false);
        } else {
            for (Movie checkMovie : checkList) {
                if (checkMovie.getId().equals(movie.getId())) {
                    Log.d(LOG_TAG + " Check", " movie found");
                    checkBoxFav.setChecked(true);
                    checkBoxFav.setText("Un Fav me!");
                    break;
                } else {
                    Log.d(LOG_TAG + " Check", " movie not found");
                    checkBoxFav.setChecked(false);
                    checkBoxFav.setText("Fav me!");
                }
            }
        }

        return view;
    }


    public void LoadTrailers(final Integer id) {
        Uri builtUri = Uri.parse("http://api.themoviedb.org/3/movie/" + id + "/videos").buildUpon() // id string wala integer???
                .appendQueryParameter("api_key", "10a9d669a2da4a5802ae24830c3a394d")
                .build();
        String url = builtUri.toString();
        Log.d("trailer", "url = " + url);

        Ion.with(this)
                .load("GET", url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String jsonString) {

                        Log.d("Trailer", "result = " + jsonString);

                        // check error
                        if (e != null) {
                            Log.d("Trailer", e.getMessage());
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // int ID = Integer.parseInt(id);

                        // parse the data
                        TrailerList = parseTrailer.parseResponse(jsonString, id);
                        if (TrailerList == null) {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            return;
                        }



                        for (final Trailer trailer : TrailerList) {

                            View trailer_layout = LayoutInflater.from(getContext()).inflate(R.layout.trailer_layout, null, false);

                            TextView name = (TextView) trailer_layout.findViewById(R.id.trailer_name);
                            ImageView image = (ImageView) trailer_layout.findViewById(R.id.trailer_image);

                            name.setText(trailer.getName());
                            image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String URL = "http://www.youtube.com/watch?v=" + trailer.getKey();
                                 //   http://www.youtube.com/watch?v=cxLG2wtE7TM
                                    Log.d("Movie Trailer youtube", URL);
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse(URL)));
                                }
                            });

                            TrailersLinearLayout.addView(trailer_layout);

                        }
                    }
                });


        // return TrailerList;
    }

    public void LoadReview(final Integer id) {

        Uri builtUri = Uri.parse("http://api.themoviedb.org/3/movie/" + id + "/reviews").buildUpon()
                .appendQueryParameter("api_key", "10a9d669a2da4a5802ae24830c3a394d")
                .build();
        String url = builtUri.toString();
        Log.d("trailer", "url = " + url);

        Ion.with(this)
                .load("GET", url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String jsonString) {

                        Log.d("Reviews", "result = " + jsonString);

                        // check error
                        if (e != null) {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // int ID = Integer.parseInt(id);

                        // parse the data
                        ReviewList = parseReview.parseResponse(jsonString); // mfrod check 3l id too
                        if (ReviewList == null) {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (final Review review : ReviewList) {

                            View Review_layout = LayoutInflater.from(getContext()).inflate(R.layout.review_layout, null, false);

                            TextView author = (TextView) Review_layout.findViewById(R.id.author_text_view);
                            TextView content = (TextView) Review_layout.findViewById(R.id.content_text_view);


                            author.setText(review.getAuthor());
                            content.setText(review.getContent());

                            TrailersLinearLayout.addView(Review_layout);
                        }
                    }
                });
    }
}
