package com.example.taskproject.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskproject.R;
import com.example.taskproject.contract.MainViewContract;
import com.example.taskproject.databinding.ItemMovieBinding;
import com.example.taskproject.model.Movie;
import com.example.taskproject.viewmodel.MovieItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private final MainViewContract mainView;
    private final Context context;
    private List<Movie> items;

    public MovieAdapter(MainViewContract mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
        this.items = new ArrayList<>();
    }

    public void setItems(List<Movie> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public Movie getItem(int idx) {
        return items.get(idx);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_movie, parent, false);
        binding.setViewModel(new MovieItemViewModel(mainView));
        return new MovieViewHolder(binding.getRoot(), binding.getViewModel());
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        final Movie item = items.get(position);
        holder.loadItem(item);
    }

    public void addMovies(List<Movie> movies) {
        int preSize = items.size();
        this.items.addAll(movies);
        notifyItemMoved(preSize, items.size());
    }

    public void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        private final MovieItemViewModel viewModel;

        public MovieViewHolder(View itemView, MovieItemViewModel viewModel) {
            super(itemView);
            this.viewModel = viewModel;
        }

        public void loadItem(Movie item) {
            viewModel.loadItem(item);
        }
    }
}
