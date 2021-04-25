package com.example.newnormal.ui.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newnormal.R;
import com.example.newnormal.data.models.News;
import com.example.newnormal.ui.activities.MainActivity;
import com.example.newnormal.ui.activities.NewsArticleActivity;
import com.example.newnormal.ui.adapters.NewsAdapter;

import java.util.List;

public class CroatianNewsFragment extends BaseNewsFragment {
    private static final int NEWS_ARTICLE_REQUEST = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        RecyclerView rvNewsArticles = view.findViewById(R.id.rv_news_articles);
        rvNewsArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNewsArticles.setHasFixedSize(true);

        final NewsAdapter newsAdapter = new NewsAdapter();
        rvNewsArticles.setAdapter(newsAdapter);

        // TODO: replace this with more loosely coupled solution (search on Google: "send data from activity to fragment")
        final MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        LiveData<List<News>> newsList = activity.getCroatianNewsList();

        newsList.observe(getViewLifecycleOwner(), new Observer<List<News>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(@Nullable List<News> newsList) {
                // TODO: add ProgressDialog (async)
                assert newsList != null;
                String newsTitlesString = mergeAllNewsTitles(newsList);
                filterPositiveNewsTitles(activity, newsList, newsTitlesString);
                newsAdapter.setNews(newsList);
            }
        });

        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(News news) {
                Intent i = new Intent(getActivity(), NewsArticleActivity.class);
                i.putExtra(NewsArticleActivity.EXTRA_NEWS_URL, news.getUrl());
                startActivityForResult(i, NEWS_ARTICLE_REQUEST);
            }
        });

        return view;
    }
}
