package com.example.taskproject.contract;

import com.example.taskproject.model.Movie;

import java.util.List;

// viewModel 에서 Activity 를 간접적으로 참조하기 위한 함수들의 명세
public interface MainViewContract {

    void updateSearchList(List<Movie> results);
    void appendSearchList(List<Movie> results);
    void clearSearchList();

    void closeKeypad();
    void showMessage(String msg);
    void startDetailActivity(String url);
    void setProgressDialogVisibility(int visibility, String message);
}
