package com.example.ajeetyadav.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ajeet Yadav on 9/18/2017.
 */


class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<Movie> moviesList;
    public MoviesAdapter(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView picture;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.slist);
            picture = (ImageView) view.findViewById(R.id.icon);

        }
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.name.setText(movie.getName());
        holder.picture.setBackgroundResource(movie.getImage());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
