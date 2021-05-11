package com.example.newnormal.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import com.example.newnormal.R;
import com.example.newnormal.data.BookmarkedNewsDatabase;
import com.example.newnormal.data.dao.BookmarkedNewsDao;
import com.example.newnormal.data.models.BookmarkedNews;
import com.example.newnormal.data.models.News;
import com.example.newnormal.data.models.TravelAdvisory;
import com.example.newnormal.ui.BottomNavigationBehavior;
import com.example.newnormal.ui.fragments.BookmarkedNewsFragment;
import com.example.newnormal.ui.fragments.CroatianNewsFragment;
import com.example.newnormal.ui.fragments.TravelRiskFragment;
import com.example.newnormal.ui.fragments.WorldNewsFragment;
import com.example.newnormal.vm.BookmarkedNewsViewModel;
import com.example.newnormal.vm.NewsViewModel;
import com.example.newnormal.vm.TravelRiskViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.Sentence;
import com.google.cloud.language.v1.Sentiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static LanguageServiceClient mLanguageClient;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigationWorldNews:
                    switchToWorldNewsFragment();
                    return true;
                case R.id.navigationCroatianNews:
                    switchToCroatianNewsFragment();
                    return true;
                case R.id.navigationTravelRiskMap:
                    switchToTravelRiskFragment();
                    return true;
                case R.id.navigationBookmarkedNews:
                    switchToBookmarkedNewsFragment();
                    return true;
            }

            return false;
        }
    };

    MutableLiveData<List<News>> worldNewsMutableList = new MutableLiveData<>();
    MutableLiveData<List<News>> croatianNewsMutableList = new MutableLiveData<>();
    LiveData<List<BookmarkedNews>> bookmarkedNewsMutableList;
    MutableLiveData<Map<String, TravelAdvisory.CountryData.Advisory>> travelAdvisoryMutableMap = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if(new DarkModePrefManager(this).isNightMode()){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigationWorldNews);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        // create the language client
        try {
            mLanguageClient = LanguageServiceClient.create(
                    LanguageServiceSettings.newBuilder()
                            .setCredentialsProvider(() ->
                                    GoogleCredentials.fromStream(getApplicationContext()
                                            .getResources()
                                            .openRawResource(R.raw.credential)))
                            .build());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create a language client", e);
        }

        try {
//            worldNewsMutableList = (MutableLiveData<List<News>>) getNewsFromApi();
//            croatianNewsMutableList = (MutableLiveData<List<News>>) getNewsFromScraping();
            croatianNewsMutableList = (MutableLiveData<List<News>>) getNewsFromScraping(); // TODO: delete this line and uncomment two lines above after everything else is developed
            worldNewsMutableList = croatianNewsMutableList; // TODO: delete this line and uncomment two lines above after everything else is developed
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        travelAdvisoryMutableMap = (MutableLiveData<Map<String, TravelAdvisory.CountryData.Advisory>>) getTravelAdvisory(); // TODO: avoiding API calls, uncomment later

        bookmarkedNewsMutableList = getBookmarkedNewsFromDatabase();
    }

    //region Sentiment analysis methods
    public static void filterPositiveNewsTitles(List<News> newsList, String newsTitlesString) {
        List<Sentiment> sentiments = performSentimentAnalysisClient(newsTitlesString);
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

    public static List<Sentiment> performSentimentAnalysisClient(String text) {
        return analyzeSentiment(text);
    }

    private static List<Sentiment> analyzeSentiment(String text) {
        List<Sentiment> sentiments = new ArrayList<>();

        Document document = Document.newBuilder()
                .setContent(text)
                .setType(Document.Type.PLAIN_TEXT)
                .build();
        try {
            AnalyzeSentimentResponse response = mLanguageClient.analyzeSentiment(document);
            List<Sentence> sentenceList = response.getSentencesList();
            for (Sentence sentence : sentenceList) {
                Sentiment sentiment = sentence.getSentiment();
                sentiments.add(sentiment);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return sentiments;
    }
    //endregion

    //region Fragment switching methods
    public void switchToWorldNewsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new WorldNewsFragment()).commit();
    }

    public void switchToCroatianNewsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new CroatianNewsFragment()).commit();
    }

    public void switchToTravelRiskFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new TravelRiskFragment()).commit();
    }

    public void switchToBookmarkedNewsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new BookmarkedNewsFragment()).commit();
    }
    //endregion

    //region Data fetching methods
    public LiveData<List<News>> getNewsFromApi() {
        NewsViewModel newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        return newsViewModel.getWorldNewsFromApi();
    }

    public LiveData<List<News>> getNewsFromScraping() throws InterruptedException, ExecutionException {
        NewsViewModel newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        return newsViewModel.getCroatianNewsFromScraping();
    }

    public LiveData<List<BookmarkedNews>> getBookmarkedNewsFromDatabase() {
        BookmarkedNewsViewModel bookmarkedNewsViewModel = ViewModelProviders.of(this).get(BookmarkedNewsViewModel.class);
//        bookmarkedNewsViewModel.deleteAllBookmarkedNews(); // TODO: This is just for database testing purposes. Delete this block of code later.
//        BookmarkedNews bookmarkedNews = new BookmarkedNews(croatianNewsMutableList.getValue().get(1).getUrl(),
//                croatianNewsMutableList.getValue().get(1).getTitle(),
//                croatianNewsMutableList.getValue().get(1).getDescription(),
//                croatianNewsMutableList.getValue().get(1).getSource(),
//                croatianNewsMutableList.getValue().get(1).getPublishingDate(),
//                croatianNewsMutableList.getValue().get(1).getImageUrl());
//        bookmarkedNewsViewModel.insert(bookmarkedNews);
        return bookmarkedNewsViewModel.getAllBookmarkedNews();
    }

    public LiveData<Map<String, TravelAdvisory.CountryData.Advisory>> getTravelAdvisory() {
        TravelRiskViewModel travelRiskViewModel = ViewModelProviders.of(this).get(TravelRiskViewModel.class);

        return travelRiskViewModel.getTravelAdvisory();
    }
    //endregion

    //region Getters
    public LiveData<List<News>> getWorldNewsMutableList() {
        return worldNewsMutableList;
    }

    public LiveData<List<News>> getCroatianNewsMutableList() {
        return croatianNewsMutableList;
    }

    public LiveData<List<BookmarkedNews>> getBookmarkedNewsMutableList() {
        return bookmarkedNewsMutableList;
    }

    public LiveData<Map<String, TravelAdvisory.CountryData.Advisory>> getTravelAdvisoryMutableMap() {
        return travelAdvisoryMutableMap;
    }
    //endregion

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // shutdown the connection
        mLanguageClient.shutdown();
    }
}