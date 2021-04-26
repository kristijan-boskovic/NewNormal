package com.example.newnormal.vm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newnormal.data.models.News;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewsViewModel extends AndroidViewModel {
    final NewsApiClient newsApiClient = new NewsApiClient("ed691272a6f2434dab4498402eac8ad9");

    public NewsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<News>> getWorldNewsFromApi() {
        final MutableLiveData<List<News>> worldNewsMutableList = new MutableLiveData<>();
        final List<News> worldNewsList = new ArrayList<>();
        final LinkedHashSet<News> hashSet = new LinkedHashSet<>();

        // /v2/everything
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("covid")
                        .language("en")
                        .sources("google-news,bbc-news,independent,abc-news,cbs-news,cnn,medical-news-today,nbc-news,time")
                        .sortBy("publishedAt")
                        .pageSize(100)
//                        .page(1)
                        .build(),
//        newsApiClient.getTopHeadlines(
//                new TopHeadlinesRequest.Builder()
//                        .q("covid")
//                        .language("en")
//                        .sources("google-news,bbc-news,independent,abc-news,cbs-news,cnn,medical-news-today,nbc-news,time")
//                        .category("health")
//                        .pageSize(100)
//                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        List<Article> articles = response.getArticles();
                        fillWorldNewsList(articles, worldNewsList, hashSet);
                        HashSet<String> seen = new HashSet<>();
                        worldNewsList.removeIf(e -> !seen.add(e.getDescription())); // Remove duplicate news articles (same articles with different URLs)
                        worldNewsMutableList.setValue(worldNewsList);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );
        return worldNewsMutableList;
    }

    public LiveData<List<News>> getCroatianNewsFromScraping() throws IOException, ExecutionException, InterruptedException {
        final MutableLiveData<List<News>> croatianNewsMutableList = new MutableLiveData<>();
        final List<News> croatianNewsList = new ArrayList<>();

        for (int i = 1; i < 11; i++) { // News from first 10 pages (total of 100 news)
            String url = "https://www.total-croatia-news.com/tag/coronavirus/page-" + i;
            Elements elements = new MyTask().execute(url).get();
            fillCroatianNewsList(elements, croatianNewsList);
        }
        croatianNewsMutableList.setValue(croatianNewsList);

        return croatianNewsMutableList;
    }

    private void fillWorldNewsList(List<Article> articles, List<News> worldNewsList, LinkedHashSet<News> hashSet) {
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
//            Format formatter = new SimpleDateFormat("dd.M.yyyy. HH:mm:ss");
            Format formatter = new SimpleDateFormat("EEEE, d MMMM y");
            String newsPublishingDate = formatter.format(date);
            String newsImageUrl = article.getUrlToImage();
            String newsSource = article.getSource().getName();

            News news = new News(newsUrl, newsTitle, newsDescription, newsSource, newsPublishingDate, newsImageUrl);
            if (hashSet.add(news)) {
                worldNewsList.add(news);
            }
        }
    }

    private void fillCroatianNewsList(Elements elements, List<News> croatianNewsList) {
        for (Element element : elements.select("div.listingPage-item")) { // News from current page
            String newsImageUrlPath = element.select("div.img-focus img").attr("style");
            String s1 = (newsImageUrlPath.substring(newsImageUrlPath.indexOf("/")));
            String s2 = (s1.substring(0, s1.length() - 3)).trim();
            String newsImageUrlFull = "https://www.total-croatia-news.com" + s2;

            String newsUrlPath = element.select("h2.listingPage-item-title a").attr("href");
            String newsUrlFull = "https://www.total-croatia-news.com" + newsUrlPath;

            String newsPublishingDate = element.select("div.listingPage-item-content span.listingPage-item-date").text(); // TODO: reformat just like world news date

            String newsTitle = element.select("div.listingPage-item-content h2.listingPage-item-title").text();

            String newsDescriptionFull = element.select("div.listingPage-item-content div.listingPage-item-introtext").text();
            String newsDescriptionShort = newsDescriptionFull.substring(0, 300);

            News news = new News(newsUrlFull, newsTitle, newsDescriptionShort, "Total Croatia News", newsPublishingDate, newsImageUrlFull);
            croatianNewsList.add(news);
        }
    }

    private static class MyTask extends AsyncTask<String, Void, Elements> {
        @Override
        protected Elements doInBackground(String... url) {
            Document doc = null;
            try {
                doc = Jsoup.connect(url[0])
                        .timeout(6000)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert doc != null;
            Elements elements = doc.select("div.listingPage-items");

            return elements;
        }

//        @Override
//        protected void onPostExecute(String result) {
//            //if you had a ui element, you could display the title
//            ((TextView)findViewById (R.id.myTextView)).setText (result);
//        }
    }
}