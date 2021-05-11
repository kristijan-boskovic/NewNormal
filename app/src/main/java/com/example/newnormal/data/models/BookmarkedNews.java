package com.example.newnormal.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarked_news")
public class BookmarkedNews {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "source")
    public String source;

    @ColumnInfo(name = "publishing_date")
    public String publishingDate;

    @ColumnInfo(name = "image_url")
    public String imageUrl;

    public BookmarkedNews(String url, String title, String description, String source, String publishingDate, String imageUrl) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.source = source;
        this.publishingDate = publishingDate;
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return source;
    }

    public String getPublishingDate() {
        return publishingDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}