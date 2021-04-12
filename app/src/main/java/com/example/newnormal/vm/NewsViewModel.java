package com.example.newnormal.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newnormal.data.models.News;
import com.example.newnormal.data.models.Offer;
import com.example.newnormal.data.repositories.OfferRepository;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewsViewModel extends AndroidViewModel {
    final NewsApiClient newsApiClient = new NewsApiClient("ed691272a6f2434dab4498402eac8ad9");

    public NewsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<News>> getAllNews() {
        final MutableLiveData<List<News>> newsMutableList = new MutableLiveData<>();
        final List<News> newsList = new ArrayList<>();

        // /v2/everything
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("covid")
                        .language("en")
                        .sortBy("publishedAt")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        String newsTitle = response.getArticles().get(0).getTitle();
                        String newsDescription = response.getArticles().get(0).getDescription();
                        String newsPublishingDate = response.getArticles().get(0).getPublishedAt();
                        String newsImageUrl = response.getArticles().get(0).getUrlToImage();
                        News news = new News(newsTitle, newsDescription, newsPublishingDate, newsImageUrl);
                        newsList.add(news);
                        newsMutableList.setValue(newsList);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );

        return newsMutableList;
    }
}