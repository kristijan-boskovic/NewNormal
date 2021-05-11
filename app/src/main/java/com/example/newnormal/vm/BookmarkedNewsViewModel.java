package com.example.newnormal.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.newnormal.data.models.BookmarkedNews;
import com.example.newnormal.data.repositories.BookmarkedNewsRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BookmarkedNewsViewModel extends AndroidViewModel {
    private final BookmarkedNewsRepository bookmarkedNewsRepository;

    public BookmarkedNewsViewModel(@NonNull Application application) {
        super(application);
        bookmarkedNewsRepository = new BookmarkedNewsRepository(application);
    }

    public void insert(BookmarkedNews bookmarkedNews) {
        bookmarkedNewsRepository.insert(bookmarkedNews);
    }

    public void update(BookmarkedNews bookmarkedNews) {
        bookmarkedNewsRepository.update(bookmarkedNews);
    }

    public void delete(BookmarkedNews bookmarkedNews) {
        bookmarkedNewsRepository.delete(bookmarkedNews);
    }

    public void deleteAllBookmarkedNews() {
        bookmarkedNewsRepository.deleteAllBookmarkedNews();
    }

    public LiveData<List<BookmarkedNews>> getAllBookmarkedNews() {
        return bookmarkedNewsRepository.getAllBookmarkedNews();
    }

    public BookmarkedNews getBookmarkedNewsByUrl(String url) throws ExecutionException, InterruptedException {
        return bookmarkedNewsRepository.getBookmarkedNewsByUrl(url);
    }
}