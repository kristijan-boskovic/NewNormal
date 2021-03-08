package com.example.newnormal.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newnormal.R;
import com.example.newnormal.data.models.User;
import com.example.newnormal.util.UserClient;

public class AddEditOfferActivity extends AppCompatActivity {
    public static final String EXTRA_OFFER_ID =
            "com.example.newnormal.EXTRA_OFFER_ID";
    public static final String EXTRA_USER_ID =
            "com.example.newnormal.EXTRA_USER_ID";
    public static final String EXTRA_OFFER_NAME =
            "com.example.v.EXTRA_OFFER_NAME";
    public static final String EXTRA_OFFER_ADDRESS =
            "com.example.newnormal.EXTRA_OFFER_ADDRESS";
    public static final String EXTRA_OFFER_CITY =
            "com.example.newnormal.EXTRA_OFFER_CITY";
    public static final String EXTRA_OFFER_POST_CODE =
            "com.example.newnormal.EXTRA_OFFER_POST_CODE";
    public static final String EXTRA_OFFER_DESCRIPTION =
            "com.example.newnormal.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRICE =
            "com.example.newnormal.EXTRA_PRIORITY";

    private EditText etOfferName;
    private EditText etOfferAddress;
    private EditText etOfferCity;
    private EditText etOfferPostCode;
    private EditText etOfferDescription;
    private EditText etOfferPrice;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_offer);
        user = ((UserClient)(getApplicationContext())).getUser();

        etOfferName = findViewById(R.id.et_offer_name);
        etOfferAddress = findViewById(R.id.et_offer_address);
        etOfferCity = findViewById(R.id.et_offer_city);
        etOfferPostCode = findViewById(R.id.et_offer_post_code);
        etOfferDescription = findViewById(R.id.et_offer_description);
        etOfferPrice = findViewById(R.id.et_offer_price);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_OFFER_ID)) {
            setTitle(getString(R.string.edit_offer));
            etOfferName.setText(intent.getStringExtra(EXTRA_OFFER_NAME));
            etOfferAddress.setText(intent.getStringExtra(EXTRA_OFFER_ADDRESS));
            etOfferCity.setText(intent.getStringExtra(EXTRA_OFFER_CITY));
            etOfferPostCode.setText(intent.getStringExtra(EXTRA_OFFER_POST_CODE));
            etOfferDescription.setText(intent.getStringExtra(EXTRA_OFFER_DESCRIPTION));
            etOfferPrice.setText(intent.getStringExtra(EXTRA_PRICE));
        } else {
            setTitle(getString(R.string.add_offer));
        }
    }
    private void saveOffer() {
        String offerName = etOfferName.getText().toString();
        String offerAddress = etOfferAddress.getText().toString();
        String offerCity = etOfferCity.getText().toString();
        String offerPostCode = etOfferPostCode.getText().toString();
        String offerDescription = etOfferDescription.getText().toString();
        String offerPrice = etOfferPrice.getText().toString();

        if (offerName.trim().isEmpty() || offerAddress.trim().isEmpty() || offerCity.trim().isEmpty() || offerPostCode.trim().isEmpty() || offerDescription.trim().isEmpty() || offerPrice.trim().isEmpty()) {
            Toast.makeText(this, R.string.input_all_data, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent();
        i.putExtra(EXTRA_USER_ID, user.getUserId());
        i.putExtra(EXTRA_OFFER_NAME, offerName);
        i.putExtra(EXTRA_OFFER_ADDRESS, offerAddress);
        i.putExtra(EXTRA_OFFER_CITY, offerCity);
        i.putExtra(EXTRA_OFFER_POST_CODE, offerPostCode);
        i.putExtra(EXTRA_OFFER_DESCRIPTION, offerDescription);
        i.putExtra(EXTRA_PRICE, offerPrice);
        int id = getIntent().getIntExtra(EXTRA_OFFER_ID, -1);
        if (id != -1) {
            i.putExtra(EXTRA_OFFER_ID, id);
        }

        setResult(RESULT_OK, i);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_offer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_offer:
                saveOffer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
