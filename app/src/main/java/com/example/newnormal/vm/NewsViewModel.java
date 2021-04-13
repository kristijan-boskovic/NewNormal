package com.example.newnormal.vm;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newnormal.data.models.News;
import com.example.newnormal.data.models.Offer;
import com.example.newnormal.data.repositories.OfferRepository;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewsViewModel extends AndroidViewModel {
    final NewsApiClient newsApiClient = new NewsApiClient("ed691272a6f2434dab4498402eac8ad9");

    public NewsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<News>> getNewsFromApi() {
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
                        List<Article> articles = response.getArticles();
                        for (Article article : articles) {
                            String newsUrl = article.getUrl();
                            String newsTitle = article.getTitle();
                            String newsDescription = article.getDescription();
                            String newsPublishingTimeStamp = article.getPublishedAt();
                            @SuppressLint("SimpleDateFormat")
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date date = null;
                            try {
                                date = sdf.parse(newsPublishingTimeStamp);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            @SuppressLint("SimpleDateFormat")
                            Format formatter = new SimpleDateFormat("dd.M.yyyy. HH:mm:ss");
                            String newsPublishingDate = formatter.format(date);
                            String newsImageUrl = article.getUrlToImage();
                            String newsAuthor = article.getAuthor();
                            String newsSource = article.getSource().getName();
                            News news = new News(newsUrl, newsTitle, newsDescription, newsAuthor, newsSource, newsPublishingDate, newsImageUrl);
                            newsList.add(news);
                        }
                        newsMutableList.setValue(newsList);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );

        // /v2/top-headlines
//        newsApiClient.getTopHeadlines(
//                new TopHeadlinesRequest.Builder()
//                        .q("covid")
//                        .language("en")
//                        .build(),
//                new NewsApiClient.ArticlesResponseCallback() {
//                    @Override
//                    public void onSuccess(ArticleResponse response) {
//                        String newsTitle = response.getArticles().get(0).getTitle();
//                        String newsDescription = response.getArticles().get(0).getDescription();
//                        String newsImageUrl = response.getArticles().get(0).getUrlToImage();
//
//                        TextView tvFeaturedNewsTitle = findViewById(R.id.tv_featured_news_title);
//                        TextView tvFeaturedNewsDescription = findViewById(R.id.tv_featured_news_description);
//                        ImageView ivFeaturedNewsImage = findViewById(R.id.iv_featured_news);
//
//                        tvFeaturedNewsTitle.setText(newsTitle);
//                        tvFeaturedNewsDescription.setText(newsDescription);
//                        Picasso.get().load(newsImageUrl).into(ivFeaturedNewsImage);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable throwable) {
//                        System.out.println(throwable.getMessage());
//                    }
//                }
//        );

        return newsMutableList;
    }
}