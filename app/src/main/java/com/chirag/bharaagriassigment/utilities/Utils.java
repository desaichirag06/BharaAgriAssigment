package com.chirag.bharaagriassigment.utilities;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chirag.bharaagriassigment.R;

public class Utils {

    public static final String TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342";
    public static final String TMDB_BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780";

    public static void loadImage(ImageView imageView, String url, CircularProgressDrawable progressDrawable) {
        RequestOptions options = new RequestOptions()
                .placeholder(progressDrawable)
                .error(R.drawable.poster_placeholder);

        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(options)
                .load(TMDB_POSTER_BASE_URL + url)
                .into(imageView);
    }

    public static void loadBackDropImage(ImageView imageView, String url, CircularProgressDrawable progressDrawable) {
        RequestOptions options = new RequestOptions()
                .placeholder(progressDrawable)
                .error(R.drawable.poster_placeholder);

        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(options)
                .load(TMDB_BACKDROP_BASE_URL + url)
                .into(imageView);
    }

    public static CircularProgressDrawable getProgressDrawable(Context context) {
        CircularProgressDrawable cpd = new CircularProgressDrawable(context);
        cpd.setStrokeWidth(10f);
        cpd.setCenterRadius(50f);
        cpd.start();

        return cpd;
    }

    @BindingAdapter("android:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        loadImage(imageView, url, getProgressDrawable(imageView.getContext()));
    }

    @BindingAdapter("android:backDropUrl")
    public static void loadBackDropImage(ImageView imageView, String url) {
        loadBackDropImage(imageView, url, getProgressDrawable(imageView.getContext()));
    }
}
