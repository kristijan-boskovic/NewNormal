package com.example.newnormal.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.newnormal.data.models.Offer;
import com.example.newnormal.data.repositories.OfferRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class OffersViewModel extends AndroidViewModel {
    private OfferRepository offerRepository;

    public OffersViewModel(@NonNull Application application) {
        super(application);
        offerRepository = new OfferRepository(application);
    }

    public void insert(Offer offer) {
        offerRepository.insert(offer);
    }

    public void update(Offer offer) {
        offerRepository.update(offer);
    }

    public void delete(Offer offer) {
        offerRepository.delete(offer);
    }

    public void deleteAllOffers(Offer offer) {
        offerRepository.deleteAllOffers();
    }

    public LiveData<List<Offer>> getAllOffers() {
        return offerRepository.getAllOffers();
    }

    public LiveData<List<Offer>> getOffersByUserId(int userId) throws ExecutionException, InterruptedException {
        return offerRepository.getOffersByUserId(userId);
    }

    public Offer getOfferById(int offerId) throws ExecutionException, InterruptedException {
        return offerRepository.getOfferById(offerId);
    }
}