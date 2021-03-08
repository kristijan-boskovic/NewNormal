package com.example.newnormal.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.newnormal.R;

public class RegistrationActivity extends AppCompatActivity {
    public static final String EXTRA_USER_ID =
            "com.example.newnormal.EXTRA_USER_ID";
    public static final String EXTRA_FULL_NAME =
            "com.example.newnormal.EXTRA_FULL_NAME";
    public static final String EXTRA_EMAIL =
            "com.example.newnormal.EXTRA_EMAIL";
    public static final String EXTRA_PASSWORD =
            "com.example.newnormal.EXTRA_PASSWORD";
    public static final String EXTRA_CITY =
            "com.example.newnormal.EXTRA_CITY";
    public static final String EXTRA_POST_CODE =
            "com.example.newnormal.EXTRA_POST_CODE";
    public static final String EXTRA_PHONE_NUMBER =
            "com.example.newnormal.EXTRA_PHONE_NUMBER";

    private EditText etFullName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etCity;
    private EditText etPostCode;
    private EditText etPhoneNumber;
    private Button registerReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle((getString(R.string.registration)));

        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email_reg);
        etPassword = findViewById(R.id.et_password_reg);
        etCity = findViewById(R.id.et_city);
        etPostCode = findViewById(R.id.et_post_code);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        registerReg = findViewById(R.id.et_register_reg);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        registerReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkForErrors()) {
                    saveUser();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveUser() {
        String fullName = etFullName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String city = etCity.getText().toString();
        String postCode = etPostCode.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();

        Intent data = new Intent();
        data.putExtra(EXTRA_FULL_NAME, fullName);
        data.putExtra(EXTRA_EMAIL, email);
        data.putExtra(EXTRA_PASSWORD, password);
        data.putExtra(EXTRA_CITY, city);
        data.putExtra(EXTRA_POST_CODE, postCode);
        data.putExtra(EXTRA_PHONE_NUMBER, phoneNumber);
        int id = getIntent().getIntExtra(EXTRA_USER_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_USER_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean checkForErrors() {
        boolean errorExists = false;

        if (isEmpty(etFullName)) {
            etFullName.setError(getString(R.string.enter_name));
            errorExists = true;
        }

        if (!isEmail(etEmail)) {
            etEmail.setError(getString(R.string.enter_email));
            errorExists = true;
        }

        if (isEmpty(etPassword)) {
            etPassword.setError(getString(R.string.enter_password));
            errorExists = true;
        }

        if (isEmpty(etCity)) {
            etCity.setError(getString(R.string.enter_surname));
            errorExists = true;
        }

        if (isEmpty(etPostCode)) {
            etPostCode.setError(getString(R.string.enter_post_code));
            errorExists = true;
        }

        if (isEmpty(etPhoneNumber)) {
            etPhoneNumber.setError(getString(R.string.enter_phone_number));
            errorExists = true;
        }

        return errorExists;
    }
}
