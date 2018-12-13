package com.example.taskproject.viewmodel;

import android.databinding.ObservableDouble;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.text.SpannableString;
import android.view.View;

import com.example.taskproject.contract.MainViewContract;
import com.example.taskproject.model.Movie;

public class MovieItemViewModel {

    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> imageURL = new ObservableField<>();
    public ObservableField<String> director = new ObservableField<>();
    public ObservableField<String> actors = new ObservableField<>();
    public ObservableField<String> openDay = new ObservableField<>();

    public ObservableDouble rating = new ObservableDouble();

    private String link;
    private MainViewContract mainView;

    public MovieItemViewModel(MainViewContract mainView) {
        this.mainView = mainView;
    }

    public void loadItem(Movie movie) {
        link = movie.getLink();
        imageURL.set(movie.getImageURL());
        title.set(movie.getTitle());
        director.set(movie.getDirector());
        actors.set(movie.getActors());
        rating.set(movie.getUserRating());
        openDay.set(movie.getPubDate());
    }

    public void OnItemClicked(View view) {
        mainView.startDetailActivity(link);
    }
}
