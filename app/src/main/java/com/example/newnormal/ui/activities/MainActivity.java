package com.example.newnormal.ui.activities;

import android.os.AsyncTask;
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
//import com.example.newnormal.data.models.SentimentInfo;
import com.example.newnormal.ui.BottomNavigationBehavior;
//import com.example.newnormal.ui.fragments.ApiFragment;
import com.example.newnormal.ui.fragments.BlankFragment;
import com.example.newnormal.ui.fragments.NewsFragment;
//import com.example.newnormal.util.AccessTokenLoader;
import com.example.newnormal.vm.NewsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v1.AnalyzeSentimentRequest;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
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
                case R.id.navigationMyCourses:
                    switchToBlankFragment();
                    return true;
                case R.id.navigationHome:
                    switchToNewsFragment();
                    return true;
                case R.id.navigationSearch:
                    switchToBlankFragment();
                    return true;
                case R.id.navigationMenu:
                    switchToBlankFragment();
                    return true;
            }
            return false;
        }
    };

    MutableLiveData<List<News>> newsList = new MutableLiveData<>();
    List<Sentiment> sentiments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if(new DarkModePrefManager(this).isNightMode()){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigationHome);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        newsList = (MutableLiveData<List<News>>) getNewsFromApi();

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

//        String text = "Hi there Sunnyvale California!";
//        // call the API
//        new AnalyzeTask().execute(text);
    }

    public List<Sentiment> performSentimentAnalysisClient(List<String> texts) {
        // call the API
        for (String text : texts) {
//            new AnalyzeTask().execute(text);
            analyzeSentiment(text);
        }
        return sentiments;
    }

    private void analyzeSentiment(String text) {
        AnalyzeSentimentResponse response = null;

        AnalyzeSentimentRequest request = AnalyzeSentimentRequest.newBuilder()
                .setDocument(Document.newBuilder()
                        .setContent(text)
                        .setType(Document.Type.PLAIN_TEXT)
                        .build())
                .build();
        try {
            ApiFuture<AnalyzeSentimentResponse> future = mLanguageClient.analyzeSentimentCallable().futureCall(request);
            response = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//            return mLanguageClient.analyzeSentimentCallable(request);
//        return response;
        Sentiment sentiment = response.getDocumentSentiment();
        sentiments.add(sentiment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // shutdown the connection
        mLanguageClient.shutdown();
    }

    private class AnalyzeTask extends AsyncTask<String, Void, AnalyzeSentimentResponse> {
        AnalyzeSentimentResponse response = null;

        @Override
        protected AnalyzeSentimentResponse doInBackground(String... args) {
            AnalyzeSentimentRequest request = AnalyzeSentimentRequest.newBuilder()
                    .setDocument(Document.newBuilder()
                            .setContent(args[0])
                            .setType(Document.Type.PLAIN_TEXT)
                            .build())
                    .build();
            try {
                ApiFuture<AnalyzeSentimentResponse> future = mLanguageClient.analyzeSentimentCallable().futureCall(request);
                response = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
//            return mLanguageClient.analyzeSentimentCallable(request);
            return response;
        }

        @Override
        protected void onPostExecute(AnalyzeSentimentResponse analyzeEntitiesResponse) {
            // update UI with results
//            mProgressView.setVisibility(View.GONE);
//            mResultText.setText(analyzeEntitiesResponse.toString());
            Sentiment sentiment = analyzeEntitiesResponse.getDocumentSentiment();
            sentiments.add(sentiment);
        }
    }

    public void switchToNewsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new NewsFragment()).commit();
    }

    public void switchToBlankFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new BlankFragment()).commit();
    }

    public LiveData<List<News>> getNewsFromApi() {
        NewsViewModel newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        return newsViewModel.getNewsFromApi();
    }

    public LiveData<List<News>> getNewsList() {
        return newsList;
    }
}