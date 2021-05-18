package com.example.newnormal.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.newnormal.data.models.BookmarkedNews;
import com.example.newnormal.data.models.CachedNews;
import com.example.newnormal.data.repositories.BookmarkedNewsRepository;
import com.example.newnormal.data.repositories.CachedNewsRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CachedNewsViewModel extends AndroidViewModel {
    private final CachedNewsRepository cachedNewsRepository;

    public CachedNewsViewModel(@NonNull Application application) {
        super(application);
        cachedNewsRepository = new CachedNewsRepository(application);
    }

    public void insert(CachedNews cachedNews) {
        cachedNewsRepository.insert(cachedNews);
    }

    public void update(CachedNews cachedNews) {
        cachedNewsRepository.update(cachedNews);
    }

    public void delete(CachedNews cachedNews) {
        cachedNewsRepository.delete(cachedNews);
    }

    public void deleteAllCachedNews() {
        cachedNewsRepository.deleteAllCachedNews();
    }

    public LiveData<List<CachedNews>> getAllCachedNews() {
        return cachedNewsRepository.getAllCachedNews();
    }

    public CachedNews getCachedNewsByUrl(String url) throws ExecutionException, InterruptedException {
        return cachedNewsRepository.getCachedNewsByUrl(url);
    }
}