package com.example.newnormal.data.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "offer_table")
public class Offer {

    @PrimaryKey(autoGenerate = true)
    private int offerId;

    @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userId", onDelete = ForeignKey.CASCADE)
    private int userId;

    private String name;

    private String address;

    private String city;

    private String postCode;

    private String description;

    private String price;

    public Offer(int userId, String name, String address, String city, String postCode, String description, String price) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.postCode = postCode;
        this.description = description;
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public int getOfferId() {
        return offerId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }
}
