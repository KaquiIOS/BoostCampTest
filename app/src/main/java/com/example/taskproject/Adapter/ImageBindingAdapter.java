package com.example.taskproject.Adapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.taskproject.R;

public class ImageBindingAdapter {

    @BindingAdapter(value = {"url"}, requireAll = true)
    public static void loadImage(ImageView imageView,  String url) {
        Glide.with(imageView).load(url).
                apply(new RequestOptions().placeholder(R.drawable.ic_no_image).diskCacheStrategy(DiskCacheStrategy.RESOURCE)).
                into(imageView);
    }

    @BindingAdapter(value = {"rating"}, requireAll = true)
    public static void setUserRating(RatingBar rating, double value) {
        rating.setRating(((float)value/2));
    }
}
