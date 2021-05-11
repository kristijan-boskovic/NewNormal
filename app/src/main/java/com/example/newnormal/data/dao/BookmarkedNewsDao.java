package com.example.newnormal.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.newnormal.data.models.BookmarkedNews;

import java.util.List;

@Dao
public interface BookmarkedNewsDao {
    @Query("SELECT * FROM bookmarked_news")
    LiveData<List<BookmarkedNews>> getAllBookmarkedNews();

    @Query("SELECT * FROM bookmarked_news WHERE url LIKE :url LIMIT 1")
    BookmarkedNews getBookmarkedNewsByUrl(String url);

    @Insert
    void insert(BookmarkedNews bookmarkedNews);

    @Update
    void update(BookmarkedNews bookmarkedNews);

    @Delete
    void delete(BookmarkedNews bookmarkedNews);

    @Query("DELETE FROM bookmarked_news")
    void deleteAllBookmarkedNews();

}