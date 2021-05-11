package com.example.newnormal.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarked_news")
public class BookmarkedNews implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "url")
    private final String url;

    @ColumnInfo(name = "title")
    private final String title;

    @ColumnInfo(name = "description")
    private final String description;

    @ColumnInfo(name = "source")
    private final String source;

    @ColumnInfo(name = "publishing_date")
    private final String publishingDate;

    @ColumnInfo(name = "image_url")
    private final String imageUrl;

    public BookmarkedNews(String url, String title, String description, String source, String publishingDate, String imageUrl) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.source = source;
        this.publishingDate = publishingDate;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(url);
        out.writeString(title);
        out.writeString(description);
        out.writeString(source);
        out.writeString(publishingDate);
        out.writeString(imageUrl);
    }

    public static final Creator<BookmarkedNews> CREATOR = new Creator<BookmarkedNews>() {
        public BookmarkedNews createFromParcel(Parcel in) {
            return new BookmarkedNews(in);
        }

        public BookmarkedNews[] newArray(int size) {
            return new BookmarkedNews[size];
        }
    };

    protected BookmarkedNews(Parcel in) {
        url = in.readString();
        title = in.readString();
        description = in.readString();
        source = in.readString();
        publishingDate = in.readString();
        imageUrl = in.readString();
    }
}