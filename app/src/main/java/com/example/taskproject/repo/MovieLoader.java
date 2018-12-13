package com.example.taskproject.repo;

import android.os.AsyncTask;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;

import com.example.taskproject.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovieLoader extends AsyncTask<String, String, String>{

    private MovieLoaderCallback callback;
    private static final String API_URL = "https://openapi.naver.com/v1/search/movie.json";
    private static final String ENCODING = "utf-8";
    private static final String METHOD = "GET";

    private int startPos = 1;

    public MovieLoader(MovieLoaderCallback callback, int startPos) {
        this.callback = callback;
        this.startPos = startPos;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        // 결과 보내주기
        callback.returnSearchList(parseMovieJson(s));
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            String query = API_URL +
                    "?query=" + URLEncoder.encode(strings[0], ENCODING) +
                    "&start=" + String.format("%d", startPos);

            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod(METHOD);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-Naver-Client-Id", "MW9mJ10cD1YFY3ZgAUdS");
            conn.setRequestProperty("X-Naver-Client-Secret", "ZYycnw9s4p");

            if(conn.getResponseCode() == 200) {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), ENCODING));

                StringBuilder sb = new StringBuilder();
                String line = "";

                while((line = bufferedReader.readLine()) != null)
                    sb.append(line);

                return sb.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Movie> parseMovieJson(String json) {

        List<Movie> parseResult = new ArrayList<>();

        // Json 에서 문제가 발생하는 경우
        if(json == null || json.isEmpty())
            return parseResult;

        try {

            JSONObject jsonObject = new JSONObject(json);

            if(jsonObject.getInt("total") <= startPos) return parseResult;

            JSONArray array = jsonObject.getJSONArray("items");

            int len = array.length();

            for(int i = 0; i < len; ++i) {
                JSONObject obj = array.getJSONObject(i);
                Movie movie = new Movie(
                        obj.getString("image"),
                        obj.getString("link"),
                        obj.getString("title"),
                        obj.getString("pubDate"),
                        obj.getString("director"),
                        obj.getString("actor"),
                        obj.getDouble("userRating")
                );

                parseResult.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parseResult;
    }
}
