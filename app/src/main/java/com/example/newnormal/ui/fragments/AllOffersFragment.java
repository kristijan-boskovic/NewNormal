package com.example.newnormal.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newnormal.R;
import com.example.newnormal.data.models.Offer;
import com.example.newnormal.data.models.Reservation;
import com.example.newnormal.data.models.User;
import com.example.newnormal.ui.activities.OfferDetailsActivity;
import com.example.newnormal.ui.adapters.OfferAdapter;
import com.example.newnormal.util.UserClient;
import com.example.newnormal.vm.OffersViewModel;
import com.example.newnormal.vm.ReservationsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AllOffersFragment extends Fragment {
    private static final int OFFER_DETAILS_REQUEST = 1;
    private OffersViewModel offersViewModel;
    private ReservationsViewModel reservationsViewModel;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_offers, container, false);
        user = ((UserClient) (getActivity().getApplicationContext())).getUser();

        FloatingActionButton btnAddOffer = root.findViewById(R.id.btn_add_offer);
        btnAddOffer.setVisibility(View.GONE);

        RecyclerView rvOffers = root.findViewById(R.id.rv_offers);
        rvOffers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOffers.setHasFixedSize(true);

        final OfferAdapter offerAdapter = new OfferAdapter();
        rvOffers.setAdapter(offerAdapter);

        offersViewModel = ViewModelProviders.of(this).get(OffersViewModel.class);
        reservationsViewModel = ViewModelProviders.of(this).get(ReservationsViewModel.class);
        offersViewModel.getAllOffers().observe(getViewLifecycleOwner(), new Observer<List<Offer>>() {
            @Override
            public void onChanged(@Nullable List<Offer> offers) {
                offerAdapter.setOffers(offers);
            }
        });

        offerAdapter.setOnItemClickListener(new OfferAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Offer offer) {
                Intent i = new Intent(getActivity(), OfferDetailsActivity.class);
                i.putExtra(OfferDetailsActivity.EXTRA_OFFER_ID, offer.getOfferId());
                i.putExtra(OfferDetailsActivity.EXTRA_USER_ID, offer.getUserId());
                i.putExtra(OfferDetailsActivity.EXTRA_OFFER_NAME, offer.getName());
                i.putExtra(OfferDetailsActivity.EXTRA_OFFER_ADDRESS, offer.getAddress());
                i.putExtra(OfferDetailsActivity.EXTRA_OFFER_CITY, offer.getCity());
                i.putExtra(OfferDetailsActivity.EXTRA_OFFER_POST_CODE, offer.getPostCode());
                i.putExtra(OfferDetailsActivity.EXTRA_OFFER_DESCRIPTION, offer.getDescription());
                i.putExtra(OfferDetailsActivity.EXTRA_PRICE, offer.getPrice());
                startActivityForResult(i, OFFER_DETAILS_REQUEST);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OFFER_DETAILS_REQUEST && resultCode == RESULT_OK) {

            assert data != null;
            int offerId = data.getIntExtra(OfferDetailsActivity.EXTRA_OFFER_ID, -1);
            String arrivalDate = data.getStringExtra(OfferDetailsActivity.EXTRA_ARRIVAL_DATE);

            Reservation reservation = new Reservation(user.getUserId(), offerId, arrivalDate);
            reservationsViewModel.insert(reservation);

            Toast.makeText(getContext(), getString(R.string.successful_accommodation_reservation), Toast.LENGTH_SHORT).show();
        }
    }
}
