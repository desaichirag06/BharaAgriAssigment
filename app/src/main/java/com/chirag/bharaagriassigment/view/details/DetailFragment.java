package com.chirag.bharaagriassigment.view.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chirag.bharaagriassigment.R;
import com.chirag.bharaagriassigment.databinding.FragmentDetailBinding;
import com.chirag.bharaagriassigment.model.entities.MovieDetail;
import com.chirag.bharaagriassigment.model.entities.MoviePalette;
import com.chirag.bharaagriassigment.utilities.Utils;
import com.chirag.bharaagriassigment.viewmodel.DetailViewModel;

public class DetailFragment extends Fragment {

    FragmentDetailBinding mBinding;

    private int movieUuid;
    private String genres;
    private DetailViewModel detailViewModel;

    private MovieDetail currentMovie;


    public DetailFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            movieUuid = DetailFragmentArgs.fromBundle(getArguments()).getMovieUuid();
            genres = DetailFragmentArgs.fromBundle(getArguments()).getGenres();
        }

        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        detailViewModel.fetchMovie(movieUuid);
        mBinding.tvGenre.setText(genres);
        observeDetailViewModel();
    }

    private void observeDetailViewModel() {
        detailViewModel.movieLiveData.observe(getViewLifecycleOwner(), movieDetail -> {
            if (movieDetail != null) {
                currentMovie = movieDetail;
                mBinding.setMovie(movieDetail);
                if (movieDetail.backdropPath != null)
                    setupBackgroundColor(Utils.TMDB_BACKDROP_BASE_URL + currentMovie.posterPath);
            }
        });
    }

    private void setupBackgroundColor(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource)
                                .generate(palette -> {
                                    int intColor = 0;
                                    if (palette != null) {
                                        intColor = palette.getLightMutedSwatch() != null ? palette.getLightMutedSwatch().getRgb() : getResources().getColor(android.R.color.white);
                                    }

                                    MoviePalette moviePalette = new MoviePalette(intColor);
                                    mBinding.setPalette(moviePalette);
                                });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, Utils.TMDB_POSTER_BASE_URL + currentMovie.posterPath + " \nSeen " + currentMovie.title + " yet? Check out the details for it.");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Share with"));
        }
        return super.onOptionsItemSelected(item);
    }

}