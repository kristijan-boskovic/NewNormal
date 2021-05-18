package com.example.newnormal.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.newnormal.R;
import com.example.newnormal.data.models.BookmarkedNews;
import com.example.newnormal.data.models.News;
import com.example.newnormal.vm.BookmarkedNewsViewModel;

import java.util.concurrent.ExecutionException;

public class NewsArticleActivity extends AppCompatActivity {
    private BookmarkedNewsViewModel bookmarkedNewsViewModel;
    private String url = "";
    private String title = "";
    private String description = "";
    private String source = "";
    private String publishingDate = "";
    private String imageUrl = "";

    private BookmarkedNews bookmarkedNews;

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);

        WebView webView = findViewById(R.id.web);
        Intent intent = getIntent();
        News news = intent.getParcelableExtra("News");
        bookmarkedNews = intent.getParcelableExtra("BookmarkedNews");

        if (news != null) { // Activity was entered from WorldNewsFragment or CroatianNewsFragment
            url = news.getUrl();
            title = news.getTitle();
            description = news.getDescription();
            source = news.getSource();
            publishingDate = news.getPublishingDate();
            imageUrl = news.getImageUrl();
        }
        else if (bookmarkedNews != null) { // Activity was entered from BookmarkedNewsFragment
            url = bookmarkedNews.getUrl();
            title = bookmarkedNews.getTitle();
            description = bookmarkedNews.getDescription();
            source = bookmarkedNews.getSource();
            publishingDate = bookmarkedNews.getPublishingDate();
            imageUrl = bookmarkedNews.getImageUrl();
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
            item.setIcon(R.drawable.ic_baseline_bookmark_border_24);
        }
        else {
            item.setIcon(R.drawable.ic_baseline_bookmark_24);
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
                    bookmarkedNewsViewModel.insert(new BookmarkedNews(url, title, description, source, publishingDate, imageUrl));
                    item.setIcon(R.drawable.ic_baseline_bookmark_24);
                    Toast.makeText(this, "News article bookmarked!", Toast.LENGTH_SHORT).show();
                }
                else {
                    bookmarkedNewsViewModel.delete(bookmarkedNews);
                    item.setIcon(R.drawable.ic_baseline_bookmark_border_24);
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
