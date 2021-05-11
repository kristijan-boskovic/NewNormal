package com.example.newnormal.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.newnormal.R;
import com.example.newnormal.data.models.BookmarkedNews;
import com.example.newnormal.data.models.News;
import com.example.newnormal.data.models.User;
import com.example.newnormal.util.UserClient;
import com.example.newnormal.vm.BookmarkedNewsViewModel;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

public class NewsArticleActivity extends AppCompatActivity {
    private BookmarkedNewsViewModel bookmarkedNewsViewModel;
    private String url = "";
    private News news;
    private BookmarkedNews bookmarkedNews;

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);

        WebView webView = findViewById(R.id.web);
        Intent intent = getIntent();
        news = intent.getParcelableExtra("News");
        bookmarkedNews = intent.getParcelableExtra("BookmarkedNews");
        if (news != null) {
            url = news.getUrl();
        }
        else if (bookmarkedNews != null) {
            url = bookmarkedNews.getUrl();
        }

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setTitle("News");
//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("blue")));

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookmark_menu, menu);

        bookmarkedNewsViewModel = ViewModelProviders.of(this).get(BookmarkedNewsViewModel.class);
        try {
            bookmarkedNews = bookmarkedNewsViewModel.getBookmarkedNewsByUrl(url);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MenuItem item = menu.findItem(R.id.action_bookmark);
        if (bookmarkedNews == null) {
            item.setIcon(R.drawable.ic_baseline_bookmark_24);
        }
        else {
            item.setIcon(R.drawable.ic_baseline_bookmark_added_24);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        else if (item.getItemId() == R.id.action_bookmark) {
            try {
                BookmarkedNews bookmarkedNews = bookmarkedNewsViewModel.getBookmarkedNewsByUrl(url);
                if (bookmarkedNews == null) {
                    BookmarkedNews newBookmarkedNews = new BookmarkedNews(
                            news.getUrl(),
                            news.getTitle(),
                            news.getDescription(),
                            news.getSource(),
                            news.getPublishingDate(),
                            news.getImageUrl());
                    bookmarkedNewsViewModel.insert(newBookmarkedNews);
                    item.setIcon(R.drawable.ic_baseline_bookmark_added_24);
                    Toast.makeText(this, "News article bookmarked!", Toast.LENGTH_SHORT).show();
                }
                else {
                    bookmarkedNewsViewModel.delete(bookmarkedNews);
                    item.setIcon(R.drawable.ic_baseline_bookmark_24);
                    Toast.makeText(this, "News article removed from bookmarks!", Toast.LENGTH_SHORT).show();
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
