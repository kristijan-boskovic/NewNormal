package com.example.newnormal.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newnormal.R;
import com.example.newnormal.data.models.BookmarkedNews;
import com.example.newnormal.ui.activities.MainActivity;
import com.example.newnormal.ui.activities.NewsArticleActivity;
import com.example.newnormal.ui.adapters.BookmarkedNewsAdapter;

import java.util.List;

public class BookmarkedNewsFragment extends Fragment {
    private final BookmarkedNewsAdapter newsAdapter = new BookmarkedNewsAdapter();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        setHasOptionsMenu(true);

        RecyclerView rvNewsArticles = view.findViewById(R.id.rv_news_articles);
        rvNewsArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNewsArticles.setHasFixedSize(true);
        rvNewsArticles.setAdapter(newsAdapter);

        // TODO: replace this with more loosely coupled solution (search on Google: "send data from activity to fragment")
        final MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.setTitle(R.string.bookmarks);

        LiveData<List<BookmarkedNews>> newsList = activity.getBookmarkedNewsMutableList();
        newsList.observe(getViewLifecycleOwner(), new Observer<List<BookmarkedNews>>() {
            @Override
            public void onChanged(@Nullable List<BookmarkedNews> bookmarkedNewsList) {
                newsAdapter.setNews(bookmarkedNewsList);
            }
        });

        newsAdapter.setOnItemClickListener(new BookmarkedNewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BookmarkedNews news) {
                Intent intent = new Intent(getActivity(), NewsArticleActivity.class);
                intent.putExtra("BookmarkedNews", news);
                startActivity(intent);
            }
        });

        return view;
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) { // TODO: Uncomment and fix (issue with query results not appearing on initial fragment start
//        super.onCreateOptionsMenu(menu, inflater);                                        // TODO: It has to to something with async data getting from database
//        inflater.inflate(R.menu.search_menu, menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                newsAdapter.getFilter().filter(newText);
//
//                return false;
//            }
//        });
//    }
}
