package com.example.newnormal.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.newnormal.data.dao.BookmarkedNewsDao;
import com.example.newnormal.data.models.BookmarkedNews;

@Database(entities = {BookmarkedNews.class}, version = 1)
public abstract class BookmarkedNewsDatabase extends RoomDatabase {
    private static BookmarkedNewsDatabase instance;
    public abstract BookmarkedNewsDao bookmarkedNewsDao();

    public static synchronized BookmarkedNewsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), BookmarkedNewsDatabase.class, "bookmarked_news_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}