package com.example.taskproject.view;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskproject.R;
import com.example.taskproject.contract.MainViewContract;
import com.example.taskproject.databinding.ActivityMainBinding;
import com.example.taskproject.model.Movie;
import com.example.taskproject.viewmodel.MainViewModel;
import com.example.taskproject.viewmodel.MovieItemViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity implements MainViewContract {

    private ProgressDialog progressDialog;
    private MovieAdapter movieAdapter;

    private MainViewModel mainViewModel;
    private Handler handler = new Handler();

    private static final int TEXT_PADDING = 40;
    private static final int ACTIVITY_DELAY = 350;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        mainViewModel = new MainViewModel(this);

        // xml에 있는 데이터와 바인딩
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMainViewModel(mainViewModel);
        binding.setListViewModel(new MovieItemViewModel((MainViewContract) this));

        movieAdapter = new MovieAdapter((MainViewContract) this, this);
        binding.recyclerViewMainActivityItems.setAdapter(movieAdapter);
        binding.recyclerViewMainActivityItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewMainActivityItems.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.recyclerViewMainActivityItems.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(recyclerView.canScrollVertically(1)) {
                    mainViewModel.loadMoreData();
                }
            }
        });
    }

    @Override
    public void updateSearchList(List<Movie> results) {
        movieAdapter.setItems(results);
    }

    @Override
    public void clearSearchList() {
        movieAdapter.clear();
    }

    @Override
    public void appendSearchList(List<Movie> results) {
        movieAdapter.addMovies(results);
    }

    @Override
    public void startDetailActivity(final String url) {
        final CustomTabsIntent intent = new CustomTabsIntent.Builder().
                setToolbarColor(MainActivity.this.getResources().getColor(R.color.mint)).build();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                intent.launchUrl(MainActivity.this, Uri.parse(url));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }, ACTIVITY_DELAY);
    }

    @Override
    public void showMessage(String msg) {
        // 미리 위에 정의해놓고싶은데... MVVM 에선 아닌건가..
        Toast toast = new Toast(this);
        // 흰색 텍스트
        TextView textView = new TextView(this);
        textView.setText(msg);
        textView.setPadding(TEXT_PADDING, TEXT_PADDING, TEXT_PADDING, TEXT_PADDING);
        textView.setTextSize(14);
        textView.setTextColor(getResources().getColor(R.color.white));

        toast.setView(textView);
        toast.getView().setBackgroundColor(getResources().getColor(R.color.mint));
        toast.show();
    }

    @Override
    public void setProgressDialogVisibility(int visibility, String message) {
        if (visibility == View.VISIBLE) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
                progressDialog.setMessage(message);
            }
        } else {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    @Override
    public void closeKeypad() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
