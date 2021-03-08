package com.example.newnormal.data.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.newnormal.data.AhsDatabase;
import com.example.newnormal.data.dao.ReservationDao;
import com.example.newnormal.data.models.Reservation;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReservationRepository {

    private ReservationDao reservationDao;

    public ReservationRepository(Application application) {
        AhsDatabase ahsDatabase = AhsDatabase.getInstance(application);
        reservationDao = ahsDatabase.reservationDao();
    }

    public void insert(Reservation reservation) {
        new InsertReservationAsyncTask(reservationDao).execute(reservation);
    }

    public void update(Reservation reservation) {
        new UpdateReservationAsyncTask(reservationDao).execute(reservation);
    }

    public void delete(Reservation reservation) {
        new DeleteReservationAsyncTask(reservationDao).execute(reservation);
    }

    public void deleteAllReservations() {
        new DeleteAllReservationsAsyncTask(reservationDao).execute();
    }

    public LiveData<List<Reservation>> getAllReservations() {
        return reservationDao.getAllReservations();
    }

    public LiveData<List<Reservation>> getReservationsByUserId(int userId) throws ExecutionException, InterruptedException {
        return new GetReservationsByUserIdAsyncTask(reservationDao).execute(userId).get();
    }

    private static class InsertReservationAsyncTask extends AsyncTask<Reservation, Void, Void> {
        private ReservationDao reservationDao;

        private InsertReservationAsyncTask(ReservationDao reservationDao) {
            this.reservationDao = reservationDao;
        }

        @Override
        protected Void doInBackground(Reservation... reservations) {
            reservationDao.insert(reservations[0]);
            return null;
        }
    }

    private static class UpdateReservationAsyncTask extends AsyncTask<Reservation, Void, Void> {
        private ReservationDao reservationDao;

        private UpdateReservationAsyncTask(ReservationDao reservationDao) {
            this.reservationDao = reservationDao;
        }

        @Override
        protected Void doInBackground(Reservation... reservations) {
            reservationDao.update(reservations[0]);
            return null;
        }
    }

    private static class DeleteReservationAsyncTask extends AsyncTask<Reservation, Void, Void> {
        private ReservationDao reservationDao;

        private DeleteReservationAsyncTask(ReservationDao reservationDao) {
            this.reservationDao = reservationDao;
        }

        @Override
        protected Void doInBackground(Reservation... reservations) {
            reservationDao.delete(reservations[0]);
            return null;
        }
    }

    private static class DeleteAllReservationsAsyncTask extends AsyncTask<Void, Void, Void> {
        private ReservationDao reservationDao;

        private DeleteAllReservationsAsyncTask(ReservationDao reservationDao) {
            this.reservationDao = reservationDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            reservationDao.deleteAllReservations();
            return null;
        }
    }

    private static class GetReservationsByUserIdAsyncTask extends AsyncTask<Integer, Void, LiveData<List<Reservation>>> {
        private ReservationDao reservationDao;

        private GetReservationsByUserIdAsyncTask(ReservationDao reservationDao) {
            this.reservationDao = reservationDao;
        }

        @Override
        protected LiveData<List<Reservation>> doInBackground(Integer... integers) {
            return reservationDao.getReservationsByUserId(integers[0]);
        }
    }

}
