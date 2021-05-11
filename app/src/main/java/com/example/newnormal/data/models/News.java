package com.example.newnormal.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {
    private final String url;
    private final String title;
    private final String description;
    private final String source;
    private final String publishingDate;
    private final String imageUrl;

    public News(String url, String title, String description, String source, String publishingDate, String imageUrl) {
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

    public static final Creator<News> CREATOR = new Creator<News>() {
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        public News[] newArray(int size) {
            return new News[size];
        }
    };

    protected News(Parcel in) {
        url = in.readString();
        title = in.readString();
        description = in.readString();
        source = in.readString();
        publishingDate = in.readString();
        imageUrl = in.readString();
    }
}
