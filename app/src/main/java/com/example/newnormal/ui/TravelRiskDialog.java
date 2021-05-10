package com.example.newnormal.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.newnormal.R;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TravelRiskDialog extends AppCompatDialogFragment {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle mArgs = getArguments();
        String lastUpdatedDate = mArgs.getString("lastUpdated");

//        @SuppressLint("SimpleDateFormat") // TODO: avoiding API calls, uncomment later
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = null;
//        try {
//            date = sdf.parse(lastUpdatedDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        @SuppressLint("SimpleDateFormat")
//        Format formatter = new SimpleDateFormat("d MMMM y, HH:mm:ss");
//        String lastUpdatedDateFormatted = formatter.format(date);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_travel_risk, null);
        TextView tvLastUpdated = view.findViewById(R.id.tv_last_updated);
//        String lastUpdatedText = "Last updated: " + lastUpdatedDateFormatted + " CET"; // TODO: avoiding API calls, uncomment later
//        tvLastUpdated.setText(lastUpdatedText); // TODO: avoiding API calls, uncomment later
        String lastUpdatedText = "Last updated: Placeholder"; // TODO: delete this line and uncomment two lines above after everything else is developed
        tvLastUpdated.setText(lastUpdatedText); // TODO: delete this line and uncomment two lines above after everything else is developed

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Info")
                .setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        return builder.create();
    }
}
