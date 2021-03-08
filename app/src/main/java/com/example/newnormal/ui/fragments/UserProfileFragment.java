package com.example.newnormal.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.newnormal.R;
import com.example.newnormal.data.models.User;
import com.example.newnormal.util.UserClient;

public class UserProfileFragment extends Fragment {
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);
        user = ((UserClient)(getActivity().getApplicationContext())).getUser();

        final TextView tvFullName = root.findViewById(R.id.tv_full_name);
        final TextView tvEmail = root.findViewById(R.id.tv_email);
        final TextView tvPhoneNumber = root.findViewById(R.id.tv_phone_number);

        tvFullName.setText("Ime i prezime: " + user.getFullName());
        tvEmail.setText("E-mail: " + user.getEmail());
        tvPhoneNumber.setText("Telefon: " + user.getPhoneNumber());

        return root;
    }
}
