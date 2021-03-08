package com.example.newnormal.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userId;

    private String fullName;

    private String email;

    private String password;

    private String city;

    private int postCode;

    private String phoneNumber;

    public User(int userId, String fullName, String email, String password, String city, int postCode, String phoneNumber) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.city = city;
        this.postCode = postCode;
        this.phoneNumber = phoneNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCity() {
        return city;
    }

    public int getPostCode() {
        return postCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
