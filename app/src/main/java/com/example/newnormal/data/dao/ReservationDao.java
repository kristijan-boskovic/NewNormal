package com.example.newnormal.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.newnormal.data.models.Reservation;

import java.util.List;

@Dao
public interface ReservationDao {
    @Insert
    void insert(Reservation reservation);

    @Update
    void update(Reservation reservation);

    @Delete
    void delete(Reservation reservation);

    @Query("DELETE FROM reservation_table")
    void deleteAllReservations();

    @Query("SELECT * FROM reservation_table")
    LiveData<List<Reservation>> getAllReservations();

    @Query("SELECT * FROM reservation_table WHERE userId = :userId")
    LiveData<List<Reservation>> getReservationsByUserId(int userId);

}
