package com.example.taskproject.repo;

import com.example.taskproject.model.Movie;

import java.util.List;

public interface MovieLoaderCallback {

    void returnSearchList(List<Movie> moveList);
}
