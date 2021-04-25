package com.example.newnormal.ui.fragments;

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
import com.example.newnormal.ui.activities.MainActivity;
import com.example.newnormal.ui.activities.NewsArticleActivity;
import com.example.newnormal.ui.adapters.NewsAdapter;
import com.google.cloud.language.v1.Sentiment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
        assert activity != null;
        LiveData<List<News>> newsList = activity.getNewsList();

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

    private String mergeAllNewsTitles(List<News> newsList) {
        List<String> newsTitlesList = new ArrayList<>();
        for (News news : newsList) {
            newsTitlesList.add(news.getTitle());
        }

        StringBuilder newsTitlesSb = new StringBuilder();
        ListIterator<String> newsTitlesIterator = newsTitlesList.listIterator();
        while (newsTitlesIterator.hasNext()) {
            int index = newsTitlesIterator.nextIndex();
            String newsTitle = newsTitlesIterator.next();

            if (Character.isDigit(newsTitle.charAt(0))) { // Remove articles starting with number as they case invalid sentiment sentence reads
                newsList.remove(index);
                newsTitlesIterator.remove();
            }
            else { // Adjust these signs in news articles, as they case invalid sentiment sentence reads
                if (newsTitle.contains(".")) {
                    newsTitle = newsTitle.replace(".", "");
                }
                if (newsTitle.contains("!")) {
                    newsTitle = newsTitle.replace("!", "");
                }
                if (newsTitle.contains("?")) {
                    newsTitle = newsTitle.replace("?", "");
                }
                if (newsTitle.contains("$")) {
                    newsTitle = newsTitle.replace("$", " dollars");
                }
                if (newsTitle.contains("€")) {
                    newsTitle = newsTitle.replace("€", " euros");
                }
                if (newsTitle.contains("£")) {
                    newsTitle = newsTitle.replace("£", " pounds");
                }

                newsTitlesList.set(index, newsTitle);
                newsTitlesSb.append(newsTitle).append(". ");
            }
        }
        return newsTitlesSb.toString().trim();
    }

    private void filterPositiveNewsTitles(MainActivity activity, List<News> newsList, String newsTitlesString) {
        // TODO: move this sentiment analysis process somewhere where will be done only once (e.g. MainActivity or NewsViewModel)
        List<Sentiment> sentiments = activity.performSentimentAnalysisClient(newsTitlesString);
        if (sentiments.size() == newsList.size()) { // Check if number of sentiments match number of news articles (sentiment analysis done correctly)
            Iterator<News> newsIterator = newsList.iterator();
            int index = 0;
            while (newsIterator.hasNext()) {
                newsIterator.next();
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
//            Toast.makeText(getActivity(),"Sentiment analysis failed, displaying all news articles!",Toast.LENGTH_LONG).show();
        }
    }
}
