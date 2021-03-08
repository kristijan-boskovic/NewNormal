package com.example.newnormal.data.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "reservation_table")
public class Reservation {

    @PrimaryKey(autoGenerate = true)
    private int reservationId;

    @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userId", onDelete = ForeignKey.CASCADE)
    private int userId;

    @ForeignKey(entity = Offer.class, parentColumns = "offerId", childColumns = "offerId", onDelete = ForeignKey.CASCADE)
    private int offerId;

    private String arrivalDate;


    public Reservation(int userId, int offerId, String arrivalDate) {
        this.userId = userId;
        this.offerId = offerId;
        this.arrivalDate = arrivalDate;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public int getUserId() {
        return userId;
    }

    public int getOfferId() {
        return offerId;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }
}
