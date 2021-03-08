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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newnormal.R;
import com.example.newnormal.data.models.Offer;
import com.example.newnormal.data.models.User;
import com.example.newnormal.ui.activities.AddEditOfferActivity;
import com.example.newnormal.ui.adapters.OfferAdapter;
import com.example.newnormal.util.UserClient;
import com.example.newnormal.vm.OffersViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;

public class OffersFragment extends Fragment {
    private static final int ADD_OFFER_REQUEST = 1;
    private static final int EDIT_OFFER_REQUEST = 2;
    private OffersViewModel offersViewModel;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_offers, container, false);
        user = ((UserClient)(getActivity().getApplicationContext())).getUser();

        FloatingActionButton btnAddOffer = root.findViewById(R.id.btn_add_offer);
        btnAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddEditOfferActivity.class);
                startActivityForResult(i, ADD_OFFER_REQUEST);
            }
        });

        RecyclerView rvOffers = root.findViewById(R.id.rv_offers);
        rvOffers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOffers.setHasFixedSize(true);

        final OfferAdapter offerAdapter = new OfferAdapter();
        rvOffers.setAdapter(offerAdapter);

        offersViewModel = ViewModelProviders.of(this).get(OffersViewModel.class);
        try {
            offersViewModel.getOffersByUserId(user.getUserId()).observe(getViewLifecycleOwner(), new Observer<List<Offer>>() {
                @Override
                public void onChanged(@Nullable List<Offer> offers) {
                    offerAdapter.setOffers(offers);
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                offersViewModel.delete(offerAdapter.getOfferAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getActivity(), R.string.offer_deleted, Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rvOffers);

        offerAdapter.setOnItemClickListener(new OfferAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Offer offer) {
                Intent i = new Intent(getActivity(), AddEditOfferActivity.class);
                i.putExtra(AddEditOfferActivity.EXTRA_OFFER_ID, offer.getOfferId());
                i.putExtra(AddEditOfferActivity.EXTRA_OFFER_NAME, offer.getName());
                i.putExtra(AddEditOfferActivity.EXTRA_OFFER_ADDRESS, offer.getAddress());
                i.putExtra(AddEditOfferActivity.EXTRA_OFFER_CITY, offer.getCity());
                i.putExtra(AddEditOfferActivity.EXTRA_OFFER_POST_CODE, offer.getPostCode());
                i.putExtra(AddEditOfferActivity.EXTRA_OFFER_DESCRIPTION, offer.getDescription());
                i.putExtra(AddEditOfferActivity.EXTRA_PRICE, offer.getPrice());
                startActivityForResult(i, EDIT_OFFER_REQUEST);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_OFFER_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            String offerName = data.getStringExtra(AddEditOfferActivity.EXTRA_OFFER_NAME);
            String offerAddress = data.getStringExtra(AddEditOfferActivity.EXTRA_OFFER_ADDRESS);
            String offerCity = data.getStringExtra(AddEditOfferActivity.EXTRA_OFFER_CITY);
            String offerPostCode = data.getStringExtra(AddEditOfferActivity.EXTRA_OFFER_POST_CODE);
            String offerDescription = data.getStringExtra(AddEditOfferActivity.EXTRA_OFFER_DESCRIPTION);
            String offerPrice = data.getStringExtra(AddEditOfferActivity.EXTRA_PRICE);

            Offer offer = new Offer(user.getUserId(), offerName, offerAddress, offerCity, offerPostCode, offerDescription, offerPrice);
            offersViewModel.insert(offer);

            Toast.makeText(getContext(), getString(R.string.offer_saved), Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_OFFER_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditOfferActivity.EXTRA_OFFER_ID, -1);
            if (id == -1) {
                Toast.makeText(getContext(), R.string.offer_cant_be_updated, Toast.LENGTH_SHORT).show();
                return;
            }
            String offerName = data.getStringExtra(AddEditOfferActivity.EXTRA_OFFER_NAME);
            String offerAddress = data.getStringExtra(AddEditOfferActivity.EXTRA_OFFER_ADDRESS);
            String offerCity = data.getStringExtra(AddEditOfferActivity.EXTRA_OFFER_CITY);
            String offerPostCode = data.getStringExtra(AddEditOfferActivity.EXTRA_OFFER_POST_CODE);
            String offerDescription = data.getStringExtra(AddEditOfferActivity.EXTRA_OFFER_DESCRIPTION);
            String offerPrice = data.getStringExtra(AddEditOfferActivity.EXTRA_PRICE);

            Offer offer = new Offer(user.getUserId(), offerName, offerAddress, offerCity, offerPostCode, offerDescription, offerPrice);
            offer.setOfferId(id);
            offersViewModel.update(offer);

            Toast.makeText(getContext(), getString(R.string.offer_updated), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), R.string.offer_not_saved, Toast.LENGTH_SHORT).show();
        }
    }
}
