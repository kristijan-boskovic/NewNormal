package com.example.newnormal.ui.fragments;

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
import com.example.newnormal.data.models.Reservation;
import com.example.newnormal.data.models.User;
import com.example.newnormal.ui.adapters.ReservationAdapter;
import com.example.newnormal.util.UserClient;
import com.example.newnormal.vm.ReservationsViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReservationsFragment extends Fragment {
    private ReservationsViewModel reservationsViewModel;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reservations, container, false);
        user = ((UserClient)(getActivity().getApplicationContext())).getUser();

        RecyclerView rvReservations = root.findViewById(R.id.rv_reservations);
        rvReservations.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReservations.setHasFixedSize(true);

        final ReservationAdapter reservationAdapter = new ReservationAdapter();
        rvReservations.setAdapter(reservationAdapter);

        reservationsViewModel = ViewModelProviders.of(this).get(ReservationsViewModel.class);
        try {
            reservationsViewModel.getReservationsByUserId(user.getUserId()).observe(getViewLifecycleOwner(), new Observer<List<Reservation>>() {
                @Override
                public void onChanged(@Nullable List<Reservation> reservations) {
                    reservationAdapter.setReservations(reservations);
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
                reservationsViewModel.delete(reservationAdapter.getReservationAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getActivity(), R.string.reservation_canceled, Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rvReservations);

        return root;
    }
}
