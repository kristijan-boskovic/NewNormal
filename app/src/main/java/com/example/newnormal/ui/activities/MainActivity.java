package com.example.newnormal.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.newnormal.R;
import com.example.newnormal.data.models.News;
import com.example.newnormal.data.models.SentimentInfo;
import com.example.newnormal.ui.BottomNavigationBehavior;
import com.example.newnormal.ui.fragments.ApiFragment;
import com.example.newnormal.ui.fragments.BlankFragment;
import com.example.newnormal.ui.fragments.NewsFragment;
import com.example.newnormal.util.AccessTokenLoader;
import com.example.newnormal.vm.NewsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements ApiFragment.Callback {
    private static final String FRAGMENT_API = "api";
    private static final int LOADER_ACCESS_TOKEN = 1;

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

        // Prepare the API
        final FragmentManager fm = getSupportFragmentManager();
        if (getApiFragment() == null) {
            fm.beginTransaction().add(new ApiFragment(), FRAGMENT_API).commit();
        }
        prepareApi();

        newsList = (MutableLiveData<List<News>>) getNewsFromApi();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public SentimentInfo performSentimentAnalysis(String text) {
        SentimentInfo sentimentInfo = null;
        try {
            sentimentInfo = getApiFragment().analyzeSentiment(text);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sentimentInfo;
    }

    private ApiFragment getApiFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_API);
        return (ApiFragment) fragment;
    }

    private void prepareApi() {
        // Initiate token refresh
        getSupportLoaderManager().initLoader(LOADER_ACCESS_TOKEN, null,
                new LoaderManager.LoaderCallbacks<String>() {
                    @Override
                    public Loader<String> onCreateLoader(int id, Bundle args) {
                        return new AccessTokenLoader(MainActivity.this);
                    }

                    @Override
                    public void onLoadFinished(Loader<String> loader, String token) {
                        getApiFragment().setAccessToken(token);
                    }

                    @Override
                    public void onLoaderReset(Loader<String> loader) {
                    }
                });
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

//    @Override
//    public void onEntitiesReady(EntityInfo[] entities) {
//
//    }

//    @Override
//    public void onSyntaxReady(TokenInfo[] tokens) {
//
//    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_dark_mode) {
//            //code for setting dark mode
//            //true for dark mode, false for day mode, currently toggling on each click
//            DarkModePrefManager darkModePrefManager = new DarkModePrefManager(this);
//            darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            recreate();
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
