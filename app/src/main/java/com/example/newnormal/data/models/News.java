package com.example.newnormal.data.models;

public class News {
    private String title;
    private String description;
    private String publishingDate;
    private String imageUrl;

    public News(String title, String description, String publishingDate, String imageUrl) {
        this.title = title;
        this.description = description;
        this.publishingDate = publishingDate;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(String publishingDate) {
        this.publishingDate = publishingDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
