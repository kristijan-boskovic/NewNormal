package com.example.newnormal.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.example.newnormal.R;
import com.example.newnormal.data.models.News;
import com.example.newnormal.data.models.TravelAdvisory;
import com.example.newnormal.ui.BottomNavigationBehavior;
import com.example.newnormal.ui.fragments.BlankFragment;
import com.example.newnormal.ui.fragments.CroatianNewsFragment;
import com.example.newnormal.ui.fragments.TravelRestrictionsFragment;
import com.example.newnormal.ui.fragments.WorldNewsFragment;
import com.example.newnormal.util.TravelAdvisoryApi;
import com.example.newnormal.vm.NewsViewModel;
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
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TRAVEL_ADVISORY_URL = "https://www.travel-advisory.info";

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
                case R.id.navigationTravelRestrictions:
                    switchToTravelRestrictionsFragment();
                    return true;
                case R.id.navigationToDoThree:
                    switchToBlankFragment();
                    return true;
            }

            return false;
        }
    };

    MutableLiveData<List<News>> worldNewsMutableList = new MutableLiveData<>();
    MutableLiveData<List<News>> croatianNewsMutableList = new MutableLiveData<>();

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
            worldNewsMutableList = (MutableLiveData<List<News>>) getNewsFromApi();
            croatianNewsMutableList = (MutableLiveData<List<News>>) getNewsFromScraping();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        getTravelAdvisory(); // TODO: make method return processed data
    }

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

    public void switchToWorldNewsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new WorldNewsFragment()).commit();
    }

    public void switchToCroatianNewsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new CroatianNewsFragment()).commit();
    }

    public void switchToTravelRestrictionsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new TravelRestrictionsFragment()).commit();
    }

    public void switchToBlankFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new BlankFragment()).commit();
    }

    public LiveData<List<News>> getNewsFromApi() {
        NewsViewModel newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        return newsViewModel.getWorldNewsFromApi();
    }

    public LiveData<List<News>> getNewsFromScraping() throws InterruptedException, ExecutionException {
        NewsViewModel newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        return newsViewModel.getCroatianNewsFromScraping();
    }

    public void getTravelAdvisory() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TRAVEL_ADVISORY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TravelAdvisoryApi travelAdvisoryApi = retrofit.create(TravelAdvisoryApi.class);
        Call<TravelAdvisory> call = travelAdvisoryApi.getTravelAdvisory();
        call.enqueue(new Callback<TravelAdvisory>() {
            @Override
            public void onResponse(@NonNull Call<TravelAdvisory> call, @NonNull Response<TravelAdvisory> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", "Fetching data from Travel Advisory API successful!");

                    TravelAdvisory travelAdvisory = response.body();
                    if (travelAdvisory != null) {
//                        String restrictions = travelAdvisory.get(3).toString();
                    }
//                    Log.d("Success", travelRestrictions.get(3).toString());
//                    TextView textView = findViewById(R.id.text);
//                    textView.setText(posts.get(3).getBody().toString());
                } else {
                    Log.d("Fail", "Fetching data from Travel Advisory API failed!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<TravelAdvisory> call, @NonNull Throwable t) {
                Log.d("Error", "Error occurred during fetching data from Travel Advisory API!");
            }
        });
    }

    public LiveData<List<News>> getWorldNewsMutableList() {
        return worldNewsMutableList;
    }

    public LiveData<List<News>> getCroatianNewsMutableList() {
        return croatianNewsMutableList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // shutdown the connection
        mLanguageClient.shutdown();
    }
}