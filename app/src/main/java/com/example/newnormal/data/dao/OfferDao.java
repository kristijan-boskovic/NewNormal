package com.example.newnormal.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.newnormal.data.models.Offer;

import java.util.List;

@Dao
public interface OfferDao {
    @Insert
    void insert(Offer offer);

    @Update
    void update(Offer offer);

    @Delete
    void delete(Offer offer);

    @Query("DELETE FROM offer_table")
    void deleteAllOffers();

    @Query("SELECT * FROM offer_table")
    LiveData<List<Offer>> getAllOffers();

    @Query("SELECT * FROM offer_table WHERE userId = :userId")
    LiveData<List<Offer>> getOffersByUserId(int userId);

    @Query("SELECT * FROM offer_table WHERE offerId = :offerId")
    Offer getOfferById(int offerId);
}
