package com.chirag.bharaagriassigment.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.chirag.bharaagriassigment.R;
import com.chirag.bharaagriassigment.databinding.ItemMovieImageBinding;
import com.chirag.bharaagriassigment.model.entities.Genre;
import com.chirag.bharaagriassigment.model.entities.MovieDetail;
import com.chirag.bharaagriassigment.view.home.ListFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder> implements MovieClickListener {

    private ArrayList<MovieDetail> moviesList;
    private ArrayList<Genre> genresList;

    public MoviesListAdapter(ArrayList<MovieDetail> moviesList, ArrayList<Genre> genresList) {
        this.moviesList = moviesList;
        this.genresList = genresList;
    }

    public void updateMoviesList(List<MovieDetail> newMovieList) {
        moviesList.clear();
        moviesList.addAll(newMovieList);
        notifyDataSetChanged();
    }

    public void updateGenreList(List<Genre> newGenreList) {
        genresList.clear();
        genresList.addAll(newGenreList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemMovieImageBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_movie_image, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.itemView.setMovie(moviesList.get(position));
        String movieGenresList = "";
        int i = 0;
        while (i < moviesList.get(position).genreIds.size()) {

            for (int j = 0; j < genresList.size(); j++) {
                if (moviesList.get(position).genreIds.get(i).equals(genresList.get(j).getId()))
                    movieGenresList = String.format("%s%s", movieGenresList, genresList.get(j).getName());
            }
            if (i < moviesList.get(position).genreIds.size() - 1)
                movieGenresList = String.format("%s, ", movieGenresList);

            i++;
        }
        Log.e("movieGenresList: ", movieGenresList);
        holder.itemView.genreId.setText(movieGenresList);
        holder.itemView.setListener(this);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    @Override
    public void onMovieClicked(View v) {
        String uuidString = ((TextView) v.findViewById(R.id.movieId)).getText().toString();
        String genreUidString = ((TextView) v.findViewById(R.id.genreId)).getText().toString();
        int uuid = Integer.parseInt(uuidString);
        ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail(genreUidString);
        action.setMovieUuid(uuid);
        Navigation.findNavController(v).navigate(action);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ItemMovieImageBinding itemView;

        public MovieViewHolder(@NonNull ItemMovieImageBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }
}
