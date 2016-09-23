package com.example.alaa.moviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;

import Models.Movie;

/**
 * Created by alaa on 9/16/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie_database";
    public static int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createQuery =
                "CREATE TABLE Movie(\n" +
                        "_id TEXT PRIMARY KEY\n" +
                        ", originalTitle TEXT \n" +
                        ", overview TEXT \n" +
                        ", posterPath TEXT\n" +
                        ", voteAverage TEXT\n" +
                        ", releaseDate TEXT\n" +
                        ", UNIQUE (_id) ON CONFLICT REPLACE)";
        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropQuery = "DROP TABLE IF EXISTS Movie";
        sqLiteDatabase.execSQL(dropQuery);
        onCreate(sqLiteDatabase);
    }

    public void insertMovie(Movie movie)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry._ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLOUMN_TITLE, movie.getOriginalTitle());
        contentValues.put(MovieContract.MovieEntry.COLOUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLOUMN_POSTERPATH, movie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLOUMN_VOTE, movie.getVoteAverage());
        contentValues.put(MovieContract.MovieEntry.COLOUMN_RELEASEDATE, movie.getReleaseDate());

        getWritableDatabase()
                .insert("Movie"
                        , null
                        , contentValues);
    }

    public List<Movie> getMovies()
    {
        Cursor cursor = getReadableDatabase()
                .query(MovieContract.MovieEntry.TABLE_NAME
                        ,null
                        ,null //MovieContract.MovieEntry.COLOLUM_TYPE +"=?"
                        ,null //new String[]{cardType}
                        ,null
                        ,null
                        ,null);

        List<Movie> movieList = new ArrayList<>();
        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                Integer id =
                        cursor.getInt(
                                cursor.getColumnIndex(MovieContract.MovieEntry._ID));
                String title =
                        cursor.getString(
                                cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_TITLE));
                String overview =
                        cursor.getString(
                                cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_OVERVIEW));
                String poster_bath =
                        cursor.getString(
                                cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_POSTERPATH));
                Float vote_average =
                        cursor.getFloat(
                                cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_VOTE));
                String relase_date =
                        cursor.getString(
                                cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_RELEASEDATE));

                Movie movie = new Movie (id , title , overview , poster_bath,vote_average,relase_date );

                movieList.add(movie);
            }while(cursor.moveToNext());
        }
        return movieList;
    }

    public boolean deleteMovie(Integer id)
    {
        return getWritableDatabase()
                .delete(MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry._ID + "=" + id,
                        null) >0 ;

    }
}
