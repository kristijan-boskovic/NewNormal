package com.example.newnormal.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.newnormal.util.UserClient;
import com.example.newnormal.data.models.User;
import com.example.newnormal.vm.LoginViewModel;
import com.example.newnormal.R;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private static final int REGISTER_USER_REQUEST = 1;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegisterLog;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.et_email_log);
        etPassword = findViewById(R.id.et_password_log);
        btnLogin = findViewById(R.id.btn_login);
        btnRegisterLog = findViewById(R.id.btn_register_log);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkUsername();
                }
                catch (ExecutionException e) {
                    e.printStackTrace();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        btnRegisterLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivityForResult(intent, REGISTER_USER_REQUEST);
            }
        });

        try {
            loginViewModel.getAllUsers();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REGISTER_USER_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            String fullName = data.getStringExtra(RegistrationActivity.EXTRA_FULL_NAME);
            String email = data.getStringExtra(RegistrationActivity.EXTRA_EMAIL);
            String password = data.getStringExtra(RegistrationActivity.EXTRA_PASSWORD);
            String city = data.getStringExtra(RegistrationActivity.EXTRA_CITY);
            int postCode = data.getIntExtra(RegistrationActivity.EXTRA_POST_CODE, 11);
            String phoneNumber = data.getStringExtra(RegistrationActivity.EXTRA_PHONE_NUMBER);

            Random generator = new Random();
            User user = new User(Math.abs(generator.nextInt()), fullName, email, password, city, postCode, phoneNumber);
            loginViewModel.insert(user);

            Toast.makeText(this, getString(R.string.successful_registration), Toast.LENGTH_SHORT).show();
        }
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void checkUsername() throws ExecutionException, InterruptedException {
        boolean isValid = true;
        if (isEmpty(etEmail) || isEmail(etEmail)) {
            etEmail.setError(getString(R.string.incorrect_email));
            isValid = false;
        }

        if (isEmpty(etPassword)) {
            etPassword.setError(getString(R.string.enter_password));
            isValid = false;
        }

        if (isValid) {
            String userEmail = etEmail.getText().toString().trim();
            String userPassword = etPassword.getText().toString().trim();
            User signedInUser = loginViewModel.getUserByEmailAndPassword(userEmail, userPassword);
            ((UserClient)(getApplicationContext())).setUser(signedInUser);
            if (signedInUser != null) {
                Intent i = new Intent(LoginActivity.this, OldMainActivity.class);
                startActivity(i);
                this.finish();
            } else {
                Toast t = Toast.makeText(this, R.string.wrong_email_or_password, Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }
}
