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
import com.example.newnormal.ui.activities.MainActivity;
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
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;

public class NewsViewModel extends AndroidViewModel {
    private static final String TOTAL_CROATIA_NEWS_API_URL = "https://www.total-croatia-news.com";
    private static final String NEWS_API_KEY = "ed691272a6f2434dab4498402eac8ad9";

    private final NewsApiClient newsApiClient = new NewsApiClient(NEWS_API_KEY);

    public NewsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<News>> getWorldNewsFromApi() {
        final MutableLiveData<List<News>> worldNewsMutableList = new MutableLiveData<>();

        // /v2/everything
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("covid")
                        .language("en")
                        .sources("google-news,bbc-news,independent,abc-news,cbs-news,medical-news-today,nbc-news,time")
                        .sortBy("publishedAt")
                        .pageSize(100)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        List<Article> articles = response.getArticles();
                        List<News> worldNewsList = new ArrayList<>();
                        LinkedHashSet<News> hashSet = new LinkedHashSet<>();

                        fillWorldNewsList(articles, worldNewsList, hashSet);
                        HashSet<String> seen = new HashSet<>();
                        worldNewsList.removeIf(e -> !seen.add(e.getDescription())); // Remove duplicate news articles (same articles with different URLs)
                        String newsTitlesString = mergeAllNewsTitles(worldNewsList);
                        MainActivity.filterPositiveNewsTitles(worldNewsList, newsTitlesString);

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

    public LiveData<List<News>> getCroatianNewsFromScraping() throws ExecutionException, InterruptedException {
        List<News> croatianNewsList = new ArrayList<>();

        for (int i = 1; i < 2; i++) { // TODO: change this to < 11 after everything else is developed
            String url = TOTAL_CROATIA_NEWS_API_URL + "/tag/coronavirus/page-" + i;
            Elements elements = new CroatianNewsJsoupTask().execute(url).get();
            croatianNewsList.addAll(fillCroatianNewsList(elements));
        }

        MutableLiveData<List<News>> croatianNewsMutableList = new MutableLiveData<>();
        String newsTitlesString = mergeAllNewsTitles(croatianNewsList);
        MainActivity.filterPositiveNewsTitles(croatianNewsList, newsTitlesString);
        croatianNewsMutableList.setValue(croatianNewsList);

        return croatianNewsMutableList;
    }

    private static class CroatianNewsJsoupTask extends AsyncTask<String, Void, Elements> {
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

            return doc.select("div.listingPage-items");
        }

//        @Override
//        protected void onPostExecute(String result) {
//            //if you had a ui element, you could display the title
//            ((TextView)findViewById (R.id.myTextView)).setText (result);
//        }
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

    private List<News> fillCroatianNewsList(Elements elements) {
        List<News> croatianNewsList = new ArrayList<>();

        for (Element element : elements.select("div.listingPage-item")) { // News from current page
            String newsImageUrlPath = element.select("div.img-focus img").attr("style");
            String s1 = (newsImageUrlPath.substring(newsImageUrlPath.indexOf("/")));
            String s2 = (s1.substring(0, s1.length() - 3)).trim();
            String newsImageUrlFull = TOTAL_CROATIA_NEWS_API_URL + s2;

            String newsUrlPath = element.select("h2.listingPage-item-title a").attr("href");
            String newsUrlFull = TOTAL_CROATIA_NEWS_API_URL + newsUrlPath;

            String newsPublishingDate = element.select("div.listingPage-item-content span.listingPage-item-date").text();

            String newsTitle = element.select("div.listingPage-item-content h2.listingPage-item-title").text();

            String newsDescriptionFull = element.select("div.listingPage-item-content div.listingPage-item-introtext").text();
            String newsDescriptionShort = newsDescriptionFull.substring(0, 300);

            News news = new News(newsUrlFull, newsTitle, newsDescriptionShort, "Total Croatia News", newsPublishingDate, newsImageUrlFull);
            croatianNewsList.add(news);
        }

        return croatianNewsList;
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
            } else { // Adjust these signs in news articles, as they case invalid sentiment sentence reads
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
}