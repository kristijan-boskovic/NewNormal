package com.example.newnormal.data.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.newnormal.data.BookmarkedNewsDatabase;
import com.example.newnormal.data.dao.BookmarkedNewsDao;
import com.example.newnormal.data.models.BookmarkedNews;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BookmarkedNewsRepository {
    private final BookmarkedNewsDao bookmarkedNewsDao;
    private final LiveData<List<BookmarkedNews>> allBookmarkedNews;

    public BookmarkedNewsRepository(Application application) {
        BookmarkedNewsDatabase bookmarkedNewsDatabase = BookmarkedNewsDatabase.getInstance(application);
        bookmarkedNewsDao = bookmarkedNewsDatabase.bookmarkedNewsDao();
        allBookmarkedNews = bookmarkedNewsDao.getAllBookmarkedNews();
    }

    public void insert(BookmarkedNews bookmarkedNews) {
        new InsertBookmarkedNewsAsyncTask(bookmarkedNewsDao).execute(bookmarkedNews);
    }

    public void update(BookmarkedNews bookmarkedNews) {
        new UpdateBookmarkedNewsAsyncTask(bookmarkedNewsDao).execute(bookmarkedNews);
    }

    public void delete(BookmarkedNews bookmarkedNews) {
        new DeleteBookmarkedNewsAsyncTask(bookmarkedNewsDao).execute(bookmarkedNews);
    }

    public void deleteAllBookmarkedNews() {
        new DeleteAllBookmarkedNewsAsyncTask(bookmarkedNewsDao).execute();
    }

    public LiveData<List<BookmarkedNews>> getAllBookmarkedNews() {
        return allBookmarkedNews;
    }

    public BookmarkedNews getBookmarkedNewsByUrl(String url) throws ExecutionException, InterruptedException {
        return new GetBookmarkedNewsByUrlAsyncTask(bookmarkedNewsDao).execute(url).get();
    }

    private static class InsertBookmarkedNewsAsyncTask extends AsyncTask<BookmarkedNews, Void, Void> {
        private final BookmarkedNewsDao bookmarkedNewsDao;

        private InsertBookmarkedNewsAsyncTask(BookmarkedNewsDao bookmarkedNewsDao) {
            this.bookmarkedNewsDao = bookmarkedNewsDao;
        }

        @Override
        protected Void doInBackground(BookmarkedNews... bookmarkedNews) {
            bookmarkedNewsDao.insert(bookmarkedNews[0]);
            return null;
        }
    }

    private static class UpdateBookmarkedNewsAsyncTask extends AsyncTask<BookmarkedNews, Void, Void> {
        private final BookmarkedNewsDao bookmarkedNewsDao;

        private UpdateBookmarkedNewsAsyncTask(BookmarkedNewsDao bookmarkedNewsDao) {
            this.bookmarkedNewsDao = bookmarkedNewsDao;
        }

        @Override
        protected Void doInBackground(BookmarkedNews... bookmarkedNews) {
            bookmarkedNewsDao.update(bookmarkedNews[0]);
            return null;
        }
    }

    private static class DeleteBookmarkedNewsAsyncTask extends AsyncTask<BookmarkedNews, Void, Void> {
        private final BookmarkedNewsDao bookmarkedNewsDao;

        private DeleteBookmarkedNewsAsyncTask(BookmarkedNewsDao bookmarkedNewsDao) {
            this.bookmarkedNewsDao = bookmarkedNewsDao;
        }

        @Override
        protected Void doInBackground(BookmarkedNews... bookmarkedNews) {
            bookmarkedNewsDao.delete(bookmarkedNews[0]);
            return null;
        }
    }

    private static class DeleteAllBookmarkedNewsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final BookmarkedNewsDao bookmarkedNewsDao;

        private DeleteAllBookmarkedNewsAsyncTask(BookmarkedNewsDao bookmarkedNewsDao) {
            this.bookmarkedNewsDao = bookmarkedNewsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bookmarkedNewsDao.deleteAllBookmarkedNews();
            return null;
        }
    }

    private static class GetBookmarkedNewsByUrlAsyncTask extends AsyncTask<String, Void, BookmarkedNews> {
        private final BookmarkedNewsDao bookmarkedNewsDao;

        private GetBookmarkedNewsByUrlAsyncTask(BookmarkedNewsDao bookmarkedNewsDao) {
            this.bookmarkedNewsDao = bookmarkedNewsDao;
        }

        @Override
        protected BookmarkedNews doInBackground(String... strings) {
            return bookmarkedNewsDao.getBookmarkedNewsByUrl(strings[0]);
        }
    }
}
