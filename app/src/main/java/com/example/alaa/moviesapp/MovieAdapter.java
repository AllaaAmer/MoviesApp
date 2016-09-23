package com.example.alaa.moviesapp;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Models.Movie;

/**
 * Created by alaa on 8/31/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final Context mContext;
    private List<Movie> mData ;
    private Listener mListener;

    public MovieAdapter(Context context) {
        this.mContext = context;
        mData = new ArrayList<>();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_movie,parent,false);
        return new MovieViewHolder(view);
    }

    public void setData(List<Movie> newData){
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged(); // kol mayt3'ayar feha
    }

    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = mData.get(position);
        String baseURL = "http://image.tmdb.org/t/p/";
        String size = "w500";
        String ImageUrl = baseURL +"/"+ size +"/" + movie.getPosterPath();
        Log.d("Imageeeeee URLLL" , ImageUrl);

        Picasso.with(mContext)
                .load(ImageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageView);

        // TODO
//        .resize(50, 50)
//         .centerCrop()

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.movie_poster_image_view);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if ( mListener != null){
                        Movie movie = mData.get(getAdapterPosition());
                        mListener.onnMovieClick(movie);
                        Log.d("Detailsfragment", movie.getPosterPath() + " in Adapter");
                    }
                }
            });

        }
    }

    public interface Listener {
        void onnMovieClick (Movie movie);
    }
}
