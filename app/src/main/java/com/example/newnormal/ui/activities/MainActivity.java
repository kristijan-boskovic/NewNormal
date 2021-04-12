package com.example.newnormal.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;

import com.example.newnormal.R;
import com.example.newnormal.ui.BottomNavigationBehavior;
import com.example.newnormal.ui.fragments.BlankFragment;
import com.example.newnormal.ui.fragments.NewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
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

//        NewsApiClient newsApiClient = new NewsApiClient("ed691272a6f2434dab4498402eac8ad9");
//
//        // /v2/everything
//        newsApiClient.getEverything(
//                new EverythingRequest.Builder()
//                        .q("covid")
//                        .language("en")
//                        .sortBy("publishedAt")
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
//                        ImageView ivFeaturedNewsImage = findViewById(R.id.iv_featured_news_image);
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
    }

    public void switchToNewsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new NewsFragment()).commit();
    }

    public void switchToBlankFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new BlankFragment()).commit();
    }

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
