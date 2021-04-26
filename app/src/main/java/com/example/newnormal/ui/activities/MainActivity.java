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

import com.example.newnormal.R;
import com.example.newnormal.data.models.News;
import com.example.newnormal.ui.BottomNavigationBehavior;
import com.example.newnormal.ui.fragments.BlankFragment;
import com.example.newnormal.ui.fragments.CroatianNewsFragment;
import com.example.newnormal.ui.fragments.WorldNewsFragment;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private LanguageServiceClient mLanguageClient;

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
                case R.id.navigationToDoTwo:
                    switchToBlankFragment();
                    return true;
                case R.id.navigationToDoThree:
                    switchToBlankFragment();
                    return true;
            }
            return false;
        }
    };

    MutableLiveData<List<News>> worldNewsList = new MutableLiveData<>();
    MutableLiveData<List<News>> croatianNewsList = new MutableLiveData<>();
//    List<Sentiment> sentiments = new ArrayList<>();

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

        try {
            worldNewsList = (MutableLiveData<List<News>>) getNewsFromApi();
            croatianNewsList = (MutableLiveData<List<News>>) getNewsFromScraping();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

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
    }

    public List<Sentiment> performSentimentAnalysisClient(String text) {
        return analyzeSentiment(text);
    }

    private List<Sentiment> analyzeSentiment(String text) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // shutdown the connection
        mLanguageClient.shutdown();
    }

    public void switchToWorldNewsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new WorldNewsFragment()).commit();
    }

    public void switchToCroatianNewsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new CroatianNewsFragment()).commit();
    }

    public void switchToBlankFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new BlankFragment()).commit();
    }

    public LiveData<List<News>> getNewsFromApi() {
        NewsViewModel newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        return newsViewModel.getWorldNewsFromApi();
    }

    public LiveData<List<News>> getWorldNewsList() {
        return worldNewsList;
    }

    public LiveData<List<News>> getNewsFromScraping() throws IOException, ExecutionException, InterruptedException {
        NewsViewModel newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        return newsViewModel.getCroatianNewsFromScraping();
    }

    public LiveData<List<News>> getCroatianNewsList() {
        return croatianNewsList;
    }
}