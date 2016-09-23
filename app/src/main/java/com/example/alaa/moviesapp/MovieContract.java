package com.example.alaa.moviesapp;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alaa on 9/16/2016.
 */
public class MovieContract {

   // public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CARD = "card";

    public static final class MovieEntry implements BaseColumns {

      //  public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARD).build();

        public static final String TABLE_NAME = "Movie";
        public static final String COLOUMN_TITLE = "originalTitle";
        public static final String COLOUMN_OVERVIEW = "overview";
        public static final String COLOUMN_POSTERPATH = "posterPath";
        public static final String COLOUMN_VOTE = "voteAverage";
        public static final String COLOUMN_RELEASEDATE = "releaseDate";

    }
}
