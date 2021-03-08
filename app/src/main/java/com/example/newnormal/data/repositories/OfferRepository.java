package com.example.newnormal.data.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.newnormal.data.AhsDatabase;
import com.example.newnormal.data.dao.OfferDao;
import com.example.newnormal.data.models.Offer;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class OfferRepository {

    private OfferDao offerDao;
    private LiveData<List<Offer>> allOffers;

    public OfferRepository(Application application) {
        AhsDatabase ahsDatabase = AhsDatabase.getInstance(application);
        offerDao = ahsDatabase.offerDao();
        allOffers = offerDao.getAllOffers();
    }

    public void insert(Offer offer) {
        new InsertOfferAsyncTask(offerDao).execute(offer);
    }

    public void update(Offer offer) {
        new UpdateOfferAsyncTask(offerDao).execute(offer);
    }

    public void delete(Offer offer) {
        new DeleteOfferAsyncTask(offerDao).execute(offer);
    }

    public void deleteAllOffers() {
        new DeleteAllOffersAsyncTask(offerDao).execute();
    }

    public LiveData<List<Offer>> getAllOffers() {
        return allOffers;
    }

    public LiveData<List<Offer>> getOffersByUserId(int userId) throws ExecutionException, InterruptedException {
        return new GetOffersByUserIdAsyncTask(offerDao).execute(userId).get();
    }

    public Offer getOfferById(int offerId) throws ExecutionException, InterruptedException {
        return new GetOfferByIdAsyncTask(offerDao).execute(offerId).get();
    }

    private static class InsertOfferAsyncTask extends AsyncTask<Offer, Void, Void> {
        private OfferDao offerDao;

        private InsertOfferAsyncTask(OfferDao offerDao) {
            this.offerDao = offerDao;
        }

        @Override
        protected Void doInBackground(Offer... offers) {
            offerDao.insert(offers[0]);
            return null;
        }
    }

    private static class UpdateOfferAsyncTask extends AsyncTask<Offer, Void, Void> {
        private OfferDao offerDao;

        private UpdateOfferAsyncTask(OfferDao offerDao) {
            this.offerDao = offerDao;
        }

        @Override
        protected Void doInBackground(Offer... offers) {
            offerDao.update(offers[0]);
            return null;
        }
    }

    private static class DeleteOfferAsyncTask extends AsyncTask<Offer, Void, Void> {
        private OfferDao offerDao;

        private DeleteOfferAsyncTask(OfferDao offerDao) {
            this.offerDao = offerDao;
        }

        @Override
        protected Void doInBackground(Offer... offers) {
            offerDao.delete(offers[0]);
            return null;
        }
    }

    private static class DeleteAllOffersAsyncTask extends AsyncTask<Void, Void, Void> {
        private OfferDao offerDao;

        private DeleteAllOffersAsyncTask(OfferDao offerDao) {
            this.offerDao = offerDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            offerDao.deleteAllOffers();
            return null;
        }
    }

    private static class GetOffersByUserIdAsyncTask extends AsyncTask<Integer, Void, LiveData<List<Offer>>> {
        private OfferDao offerDao;

        private GetOffersByUserIdAsyncTask(OfferDao offerDao) {
            this.offerDao = offerDao;
        }

        @Override
        protected LiveData<List<Offer>> doInBackground(Integer... integers) {
            return offerDao.getOffersByUserId(integers[0]);
        }
    }

    private static class GetOfferByIdAsyncTask extends AsyncTask<Integer, Void, Offer> {
        private OfferDao offerDao;

        private GetOfferByIdAsyncTask(OfferDao offerDao) {
            this.offerDao = offerDao;
        }

        @Override
        protected Offer doInBackground(Integer... integers) {
            return offerDao.getOfferById(integers[0]);
        }
    }
}
