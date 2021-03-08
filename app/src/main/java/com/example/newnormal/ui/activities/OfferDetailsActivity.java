package com.example.newnormal.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newnormal.R;
import com.example.newnormal.data.models.User;
import com.example.newnormal.util.UserClient;

public class OfferDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_OFFER_ID =
            "com.example.newnormal.EXTRA_OFFER_ID";
    public static final String EXTRA_USER_ID =
            "com.example.newnormal.EXTRA_USER_ID";
    public static final String EXTRA_OFFER_NAME =
            "com.example.newnormal.EXTRA_OFFER_NAME";
    public static final String EXTRA_OFFER_ADDRESS =
            "com.example.newnormal.EXTRA_OFFER_ADDRESS";
    public static final String EXTRA_OFFER_CITY =
            "com.example.newnormal.EXTRA_OFFER_CITY";
    public static final String EXTRA_OFFER_POST_CODE =
            "com.example.newnormal.EXTRA_OFFER_POST_CODE";
    public static final String EXTRA_OFFER_DESCRIPTION =
            "com.example.newnormal.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRICE =
            "com.example.newnormal.EXTRA_PRICE";
    public static final String EXTRA_ARRIVAL_DATE =
            "com.example.newnormal.EXTRA_ARRIVAL_DATE";

    private TextView tvOfferName;
    private TextView tvOfferAddress;
    private TextView tvOfferDescription;
    private TextView tvOfferPrice;
    private TextView tvArrivalDate;
    private Button btnReserve;
    private CalendarView cvArrivalDate;

    private User user;
    private String arrivalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        setTitle((getString(R.string.offer_details_title)));
        user = ((UserClient) (getApplicationContext())).getUser();

        tvOfferName = findViewById(R.id.tv_offer_details_name);
        tvOfferAddress = findViewById(R.id.tv_offer_details_address);
        tvOfferDescription = findViewById(R.id.tv_offer_details_description);
        tvOfferPrice = findViewById(R.id.tv_offer_details_price);
        tvArrivalDate = findViewById(R.id.tv_arrival_date);
        btnReserve = findViewById(R.id.btn_reserve);
        cvArrivalDate = findViewById(R.id.cv_arrival_date);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        tvOfferName.setText(intent.getStringExtra(EXTRA_OFFER_NAME));
        tvOfferAddress.setText(intent.getStringExtra(EXTRA_OFFER_ADDRESS) + ", " + intent.getStringExtra(EXTRA_OFFER_POST_CODE) + " " + intent.getStringExtra(EXTRA_OFFER_CITY));
        tvOfferDescription.setText(intent.getStringExtra(EXTRA_OFFER_DESCRIPTION));
        tvOfferPrice.setText(intent.getStringExtra(EXTRA_PRICE) + " kn/noć");

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeReservation();
            }
        });

        cvArrivalDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;
                arrivalDate = dayOfMonth + "." + month + "." + year + ".";
            }
        });

        int offerUserId = getIntent().getIntExtra(EXTRA_USER_ID, -1);
        if (offerUserId == user.getUserId()) {
            btnReserve.setVisibility(View.GONE);
            cvArrivalDate.setVisibility(View.GONE);
            tvArrivalDate.setVisibility(View.GONE);
        }
    }

    private void makeReservation() {
        String offerName = tvOfferName.getText().toString();
        String offerAddress = tvOfferAddress.getText().toString();
        String offerDescription = tvOfferDescription.getText().toString();
        String offerPrice = tvOfferPrice.getText().toString();

        if (arrivalDate == null) {
            Toast.makeText(this, R.string.pick_dates, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent();
        i.putExtra(EXTRA_USER_ID, user.getUserId());
        i.putExtra(EXTRA_OFFER_NAME, offerName);
        i.putExtra(EXTRA_OFFER_ADDRESS, offerAddress);
        i.putExtra(EXTRA_OFFER_DESCRIPTION, offerDescription);
        i.putExtra(EXTRA_PRICE, offerPrice);
        i.putExtra(EXTRA_ARRIVAL_DATE, arrivalDate);
        int id = getIntent().getIntExtra(EXTRA_OFFER_ID, -1);
        if (id != -1) {
            i.putExtra(EXTRA_OFFER_ID, id);
        }

        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
