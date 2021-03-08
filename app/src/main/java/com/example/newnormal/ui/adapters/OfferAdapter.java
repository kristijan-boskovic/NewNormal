package com.example.newnormal.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newnormal.R;
import com.example.newnormal.data.models.Offer;

import java.util.ArrayList;
import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferHolder> {
    private List<Offer> offers = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public OfferHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);
        return new OfferHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferHolder holder, int position) {
        Offer currentOffer = offers.get(position);
        holder.tvOfferName.setText(currentOffer.getName());
        holder.tvOfferDescription.setText(currentOffer.getDescription());
        holder.tvOfferPrice.setText(currentOffer.getPrice() + " kn/noć");
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
        notifyDataSetChanged();
    }

    public Offer getOfferAt(int position) {
        return offers.get(position);
    }

    class OfferHolder extends RecyclerView.ViewHolder {
        private TextView tvOfferName;
        private TextView tvOfferDescription;
        private TextView tvOfferPrice;

        public OfferHolder(@NonNull View itemView) {
            super(itemView);
            tvOfferName = itemView.findViewById(R.id.tv_offer_name);
            tvOfferDescription = itemView.findViewById(R.id.tv_offer_description);
            tvOfferPrice = itemView.findViewById(R.id.tv_offer_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(offers.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Offer offer);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
