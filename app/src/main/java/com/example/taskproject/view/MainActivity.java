package com.example.taskproject.view;


import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskproject.Adapter.MovieAdapter;
import com.example.taskproject.R;
import com.example.taskproject.contract.MainViewContract;
import com.example.taskproject.databinding.ActivityMainBinding;
import com.example.taskproject.data.Movie;
import com.example.taskproject.viewmodel.MainViewModel;
import com.example.taskproject.viewmodel.MovieItemViewModel;

import java.util.List;

/*
* 전체적인 모습은 MVVM을 흉내낸 MVP 패턴과 유사하다고 생각됨
* RXJava, Databinding, LifeCycle 제어, MVVM 등.. 에 대해서 정확한 이해가 없는 상태로 프로그램 작성
* View -> Command -> ViewModel 의 구조도 만들어져있지 않음
* */


public class MainActivity extends AppCompatActivity implements MainViewContract {

    // Progress Dialog
    private ProgressDialog progressDialog;

    // 영화 리스트 어답터
    private MovieAdapter movieAdapter;

    // View는 ViewModel의 reference를 가진다.
    private MainViewModel mainViewModel;

    private Handler handler = new Handler();

    private static final int TEXT_PADDING = 40;
    private static final int TEXT_SIZE = 14;
    private static final int ACTIVITY_DELAY = 350;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 기본적으로 필요한 객체들 초기화
        progressDialog = new ProgressDialog(this);
        // 다이얼로그 이외의 부분을 눌렀을 때 작업 취소이 취소되는 것을 막음
        progressDialog.setCancelable(false);

        // 작업을 수행할 MainViewModel 정의
        mainViewModel = new MainViewModel(this);

        // xml에 있는 데이터와 바인딩
        // dataBinding을 통해 View와 ViewModel 사이의 종속을 없앰
        // MVP 에서는 Presenter를 통해 View를 업데이트 했지만 Databinding을 통해 자동적으로 View가 업데이트 된다.
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // xml에서 사용될 data를 바인딩하는 작업
        binding.setMainViewModel(mainViewModel);
        binding.setListViewModel(new MovieItemViewModel((MainViewContract) this));

        // 리스트 어답터 생성
        movieAdapter = new MovieAdapter((MainViewContract) this, this);

        //
        binding.recyclerViewMainActivityItems.setAdapter(movieAdapter);
        binding.recyclerViewMainActivityItems.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.recyclerViewMainActivityItems.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(recyclerView.canScrollVertically(1)) {
                    // 여기서 중요한게 LoadMoreData 를 하고 callback을 이용해서
                    // View를 업데이트 하지않고 databinding을 통해 recyclerView가 변경된다는 점이다.
                    mainViewModel.loadMoreData();
                }
            }
        });
    }

    // 여기선 View와 ViewModel의 의존성이 없기에
    // Adapter를 MVP 패턴처럼 사용하지 않는다.
    // ViewModel에서는 단순히 작업만 처리한다.
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
        // ChromeTab 색상을 변경한 후 열기
        final CustomTabsIntent intent = new CustomTabsIntent.Builder().
                setToolbarColor(MainActivity.this.getResources().getColor(R.color.mint)).build();
        // ChromeTab 을 일정 시간 후 실행
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                intent.launchUrl(MainActivity.this, Uri.parse(url));
                // 화면 전환 애니메이션 추가
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }, ACTIVITY_DELAY);
    }

    @Override
    public void showMessage(String msg) {
        // TextView 의 색상 등.. 변경
        // 미리 위에 정의해놓고싶은데... MVVM 에선 아닌건가..
        Toast toast = new Toast(this);
        // 흰색 텍스트
        TextView textView = new TextView(this);
        textView.setText(msg);
        textView.setPadding(TEXT_PADDING, TEXT_PADDING, TEXT_PADDING, TEXT_PADDING);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setBackgroundColor(getResources().getColor(R.color.mint));

        toast.setView(textView);
        //toast.getView().setBackgroundColor(getResources().getColor(R.color.mint));
        toast.show();
    }

    @Override
    public void setProgressDialogVisibility(int visibility, String message) {
        // 실행 화면의 Dialog
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
