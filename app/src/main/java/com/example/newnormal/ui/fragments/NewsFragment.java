package com.example.newnormal.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newnormal.R;
import com.example.newnormal.data.models.News;
//import com.example.newnormal.data.models.SentimentInfo;
import com.example.newnormal.ui.activities.MainActivity;
import com.example.newnormal.ui.activities.NewsArticleActivity;
import com.example.newnormal.ui.adapters.NewsAdapter;
import com.google.cloud.language.v1.Sentiment;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NewsFragment extends Fragment {
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
        LiveData<List<News>> newsList = activity.getNewsList();

        newsList.observe(getViewLifecycleOwner(), new Observer<List<News>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(@Nullable List<News> newsList) {
                // TODO: add ProgressDialog (async)
                // TODO: move this sentiment analysis process somewhere where will be done only once (e.g. MainActivity or NewsViewModel)
                List<String> newsTitlesList = new ArrayList<>();
                for (News news : newsList) {
                    newsTitlesList.add(news.getTitle());
                }

                StringBuilder newsTitlesSb = new StringBuilder();
//                for (String newsTitle : newsTitlesList) {
                Iterator<String> newsTitleIterator = newsTitlesList.iterator();
                while (newsTitleIterator.hasNext()) {
                    String newsTitle = newsTitleIterator.next();
                    if ((newsTitle.contains(". ") || newsTitle.contains(".' ") || newsTitle.contains(".\" ")) && newsTitle.indexOf(".") < newsTitle.length() - 1) {
                        newsList.removeIf(e -> newsTitle.equals(e.getTitle())); // Remove news articles with dot inside text.
                        newsTitleIterator.remove();
                    } else {
                        if (newsTitle.endsWith(".") || newsTitle.endsWith("?") || newsTitle.endsWith("!")) {
                            if (newsTitle.endsWith("...")) {
                                newsTitlesSb.append(newsTitle.substring(0, newsTitle.length() - 2)).append(" ");
                            } else {
                                newsTitlesSb.append(newsTitle).append(" ");
                            }
                        } else {
                            newsTitlesSb.append(newsTitle).append(". ");
                        }
                    }
                }
//                }
                String newsTitlesString = newsTitlesSb.toString().trim();

                List<Sentiment> sentiments = activity.performSentimentAnalysisClient(newsTitlesString);
                if (sentiments.size() == newsList.size()) {
                    Iterator<News> newsIterator = newsList.iterator();
                    int index = 0;
                    while (newsIterator.hasNext()) {
                        News news = newsIterator.next();
                        Sentiment sentiment = sentiments.get(index);
                        float sentimentScore = 0;
                        if (sentiment != null) {
                            sentimentScore = sentiment.getScore();
                        }
                        if (sentimentScore <= 0) {
                            newsIterator.remove();
                        }
                        index++;
                    }
                }
                else {
                    Toast.makeText(getActivity(),"Sentiment analysis failed, displaying all news articles!",Toast.LENGTH_LONG).show();
                }
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
