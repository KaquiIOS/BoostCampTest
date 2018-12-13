package com.example.taskproject.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.taskproject.R;
import com.example.taskproject.contract.MainViewContract;
import com.example.taskproject.model.Movie;
import com.example.taskproject.repo.MovieLoader;
import com.example.taskproject.repo.MovieLoaderCallback;
import com.example.taskproject.view.MainActivity;

import java.util.List;

public class MainViewModel implements MovieLoaderCallback {

    public ObservableField<String> searchWord = new ObservableField<>();

    private MainViewContract mainView;

    private String searchItem;
    private int currentNumber = 1;
    private boolean isScrolled = false;
    private boolean hasMoreItems = true;
    ConnectivityManager cm;

    public MainViewModel(MainViewContract mainView) {
        this.mainView = mainView;
        if (mainView instanceof MainActivity) {
            cm = (ConnectivityManager) (((MainActivity) mainView).getSystemService(Context.CONNECTIVITY_SERVICE));
        }

        // 인터넷 연결
        if (cm != null && !checkNetworkStatus()) {
            mainView.showMessage("인터넷 연결을 먼저 확인해 주세요 !");
        }
    }

    public void onButtonClicked(View view) {

        // 빈 문자열 예외처리
        if (searchWord.get().isEmpty()) {
            mainView.showMessage(((MainActivity) mainView).getString(R.string.empty_input_err));
            return;
        }

        if(!checkNetworkStatus()) {
            mainView.showMessage("인터넷 연결을 먼저 확인해 주세요 !");
            return;
        }


        currentNumber = 1;
        isScrolled = false;
        hasMoreItems = true;

        // 이전 검색 결과 삭제
        mainView.clearSearchList();
        mainView.closeKeypad();

        // 검색어를 Loader에 넘겨주기
        searchItem = searchWord.get();
        mainView.setProgressDialogVisibility(View.VISIBLE, String.format("%s 검색을 시작합니다", searchItem));
        new MovieLoader(this, currentNumber).execute(searchItem);
    }


    @Override
    public void returnSearchList(List<Movie> movieList) {

        currentNumber += movieList.size();

        if (isScrolled) {
            if (movieList.size() > 0)
                mainView.appendSearchList(movieList);
            else
                hasMoreItems = false;
            isScrolled = false;
        } else {

            mainView.setProgressDialogVisibility(View.GONE, "");

            if (movieList.size() > 0)
                mainView.updateSearchList(movieList);
            else
                mainView.showMessage(String.format("%s 는 검색 결과가 없습니다", searchItem));
        }
    }

    public void loadMoreData() {
        if (!isScrolled && hasMoreItems && searchItem != null && !searchItem.isEmpty()) {
            isScrolled = true;
            new MovieLoader(this, currentNumber).execute(searchItem);
        }
    }

    private boolean checkNetworkStatus() {
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
