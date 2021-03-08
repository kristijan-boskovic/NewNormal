package com.example.newnormal.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.newnormal.data.models.Reservation;
import com.example.newnormal.data.repositories.ReservationRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReservationsViewModel extends AndroidViewModel {
    private ReservationRepository reservationRepository;

    public ReservationsViewModel(@NonNull Application application) {
        super(application);
        reservationRepository = new ReservationRepository(application);
    }

    public void insert(Reservation reservation) {
        reservationRepository.insert(reservation);
    }

    public void update(Reservation reservation) {
        reservationRepository.update(reservation);
    }

    public void delete(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    public void deleteAllReservations(Reservation reservation) {
        reservationRepository.deleteAllReservations();
    }

    public LiveData<List<Reservation>> getAllReservations() {
        return reservationRepository.getAllReservations();
    }

    public LiveData<List<Reservation>> getReservationsByUserId(int userId) throws ExecutionException, InterruptedException {
        return reservationRepository.getReservationsByUserId(userId);
    }

}