package com.example.newnormal.data.models;

public class News {
    private final String url;
    private final String title;
    private final String description;
    private final String author;
    private final String source;
    private final String publishingDate;
    private final String imageUrl;

    public News(String url, String title, String description, String author, String source, String publishingDate, String imageUrl) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.author = author;
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

    public String getAuthor() {
        return author;
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
