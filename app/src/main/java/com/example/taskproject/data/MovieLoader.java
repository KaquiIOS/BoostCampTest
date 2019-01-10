package com.example.taskproject.data;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
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

            MovieBuilder movieBuilder = new MovieBuilder();

            for(int i = 0; i < len; ++i) {
                JSONObject obj = array.getJSONObject(i);
                // 생성 부분을 Builder 로 수정
                Movie movie = movieBuilder
                        .setImageURL(obj.getString("image"))
                        .setLink(obj.getString("link"))
                        .setTitle(obj.getString("title"))
                        .setPubDate(obj.getString("pubDate"))
                        .setDirector(obj.getString("director"))
                        .setActors(obj.getString("actor"))
                        .setUserRating(obj.getDouble("userRating"))
                        .build();

                parseResult.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parseResult;
    }
}
