package com.example.newnormal.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.newnormal.data.models.CachedNews;

import java.util.List;

@Dao
public interface CachedNewsDao {
    @Query("SELECT * FROM cached_news")
    LiveData<List<CachedNews>> getAllCachedNews();

    @Query("SELECT * FROM cached_news WHERE url LIKE :url LIMIT 1")
    CachedNews getCachedNewsByUrl(String url);

    @Insert
    void insert(CachedNews cachedNews);

    @Update
    void update(CachedNews cachedNews);

    @Delete
    void delete(CachedNews cachedNews);

    @Query("DELETE FROM cached_news")
    void deleteAllCachedNews();
}