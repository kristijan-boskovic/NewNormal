package com.example.newnormal.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newnormal.R;
import com.example.newnormal.data.models.Offer;
import com.example.newnormal.data.models.Reservation;
import com.example.newnormal.vm.OffersViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationHolder> {
    private List<Reservation> reservations = new ArrayList<>();
    private OffersViewModel offersViewModel;


    @NonNull
    @Override
    public ReservationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);
        offersViewModel = ViewModelProviders.of((FragmentActivity) parent.getContext()).get(OffersViewModel.class);
        return new ReservationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationHolder holder, int position) {
        Reservation currentReservation = reservations.get(position);
        Offer offer = null;
        try {
            offer = offersViewModel.getOfferById(currentReservation.getOfferId());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        holder.tvReservationOfferName.setText(offer.getName());
        holder.tvReservationOfferPrice.setText(offer.getPrice() + " kn/noć");
        holder.tvReservationDate.setText("Datum dolaska: " + currentReservation.getArrivalDate());
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }

    public Reservation getReservationAt(int position) {
        return reservations.get(position);
    }

    class ReservationHolder extends RecyclerView.ViewHolder {
        private TextView tvReservationOfferName;
        private TextView tvReservationOfferPrice;
        private TextView tvReservationDate;

        public ReservationHolder(@NonNull View itemView) {
            super(itemView);
            tvReservationOfferName = itemView.findViewById(R.id.tv_reservation_offer_name);
            tvReservationOfferPrice = itemView.findViewById(R.id.tv_reservation_offer_price);
            tvReservationDate = itemView.findViewById(R.id.tv_reservation_date);
        }
    }
}
