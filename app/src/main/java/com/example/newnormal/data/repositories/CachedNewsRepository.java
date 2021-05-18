package com.example.newnormal.data.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.newnormal.data.NewsDatabase;
import com.example.newnormal.data.dao.CachedNewsDao;
import com.example.newnormal.data.models.CachedNews;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CachedNewsRepository {
    private final CachedNewsDao cachedNewsDao;
    private final LiveData<List<CachedNews>> allCachedNews;

    public CachedNewsRepository(Application application) {
        NewsDatabase newsDatabase = NewsDatabase.getInstance(application);
        cachedNewsDao = newsDatabase.cachedNewsDao();
        allCachedNews = cachedNewsDao.getAllCachedNews();
    }

    public void insert(CachedNews cachedNews) {
        new InsertCachedNewsAsyncTask(cachedNewsDao).execute(cachedNews);
    }

    public void update(CachedNews cachedNews) {
        new UpdateCachedNewsAsyncTask(cachedNewsDao).execute(cachedNews);
    }

    public void delete(CachedNews cachedNews) {
        new DeleteCachedNewsAsyncTask(cachedNewsDao).execute(cachedNews);
    }

    public void deleteAllCachedNews() {
        new DeleteAllCachedNewsAsyncTask(cachedNewsDao).execute();
    }

    public LiveData<List<CachedNews>> getAllCachedNews() {
        return allCachedNews;
    }

    public CachedNews getCachedNewsByUrl(String url) throws ExecutionException, InterruptedException {
        return new GetCachedNewsByUrlAsyncTask(cachedNewsDao).execute(url).get();
    }

    private static class InsertCachedNewsAsyncTask extends AsyncTask<CachedNews, Void, Void> {
        private final CachedNewsDao cachedNewsDao;

        private InsertCachedNewsAsyncTask(CachedNewsDao cachedNewsDao) {
            this.cachedNewsDao = cachedNewsDao;
        }

        @Override
        protected Void doInBackground(CachedNews... cachedNews) {
            cachedNewsDao.insert(cachedNews[0]);
            return null;
        }
    }

    private static class UpdateCachedNewsAsyncTask extends AsyncTask<CachedNews, Void, Void> {
        private final CachedNewsDao cachedNewsDao;

        private UpdateCachedNewsAsyncTask(CachedNewsDao cachedNewsDao) {
            this.cachedNewsDao = cachedNewsDao;
        }

        @Override
        protected Void doInBackground(CachedNews... cachedNews) {
            cachedNewsDao.update(cachedNews[0]);
            return null;
        }
    }

    private static class DeleteCachedNewsAsyncTask extends AsyncTask<CachedNews, Void, Void> {
        private final CachedNewsDao cachedNewsDao;

        private DeleteCachedNewsAsyncTask(CachedNewsDao cachedNewsDao) {
            this.cachedNewsDao = cachedNewsDao;
        }

        @Override
        protected Void doInBackground(CachedNews... cachedNews) {
            cachedNewsDao.delete(cachedNews[0]);
            return null;
        }
    }

    private static class DeleteAllCachedNewsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final CachedNewsDao cachedNewsDao;

        private DeleteAllCachedNewsAsyncTask(CachedNewsDao cachedNewsDao) {
            this.cachedNewsDao = cachedNewsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cachedNewsDao.deleteAllCachedNews();
            return null;
        }
    }

    private static class GetCachedNewsByUrlAsyncTask extends AsyncTask<String, Void, CachedNews> {
        private final CachedNewsDao cachedNewsDao;

        private GetCachedNewsByUrlAsyncTask(CachedNewsDao cachedNewsDao) {
            this.cachedNewsDao = cachedNewsDao;
        }

        @Override
        protected CachedNews doInBackground(String... strings) {
            return cachedNewsDao.getCachedNewsByUrl(strings[0]);
        }
    }
}
