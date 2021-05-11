package com.example.bankr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class SignupActivity extends AppCompatActivity {

    Button signup;
    EditText username;
    EditText amount;
    EditText password;
    EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        amount = (EditText) findViewById(R.id.etInitialBalance);
        amount.addTextChangedListener(new TextWatcher() {
            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    amount.removeTextChangedListener(this);

                    String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
                    String cleanString = s.toString().replaceAll(replaceable, "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    current = formatted;
                    amount.setText(formatted);
                    amount.setSelection(formatted.length());
                    amount.addTextChangedListener(this);
                }
            }
        });

        signup = (Button) findViewById(R.id.btnSignup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { connect(); }
        });
    }

    public void connect() {
        username = findViewById(R.id.etUsername);
        amount = findViewById(R.id.etInitialBalance);
        password = findViewById(R.id.etPassword);
        confirmPassword = findViewById(R.id.etConfirmPassword);

        String usernameStr = username.getText().toString();
        String amountStr = amount.getText().toString();
        String passwordStr = password.getText().toString();
        String confirmPasswordStr = confirmPassword.getText().toString();

        String cleanUsername =  usernameStr.replaceAll("[^a-z0-9_.\\-]", "");
        String cleanAmount = amountStr.replaceAll("[^\\d.]", "");
        String cleanPassword = passwordStr.replaceAll("[^a-z0-9_.\\-]", "");
        String cleanConfirmPassword = confirmPasswordStr.replaceAll("[^a-z0-9_.\\-]", "");

        if (!usernameStr.equals(cleanUsername) || !passwordStr.equals(cleanPassword) || !confirmPasswordStr.equals(cleanConfirmPassword)) {
            Toast.makeText(SignupActivity.this,
                    "invalid_input", Toast.LENGTH_SHORT).show();
            return;
        }

        if (usernameStr.length() > 127 || passwordStr.length() > 127 || confirmPasswordStr.length() > 127) {
            Toast.makeText(SignupActivity.this,
                    "input_input", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.getText().toString().isEmpty() || amount.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {
            Toast.makeText(SignupActivity.this, "invalid_input", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        else {

            DatabaseHelper databaseHelper = new DatabaseHelper(SignupActivity.this);
            if (databaseHelper.checkIfUserExists(username.getText().toString())) {
                Toast.makeText(SignupActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                return;
            }
            BigDecimal balance = new BigDecimal(cleanAmount);
            User user = new User(cleanUsername, balance, cleanPassword);
            boolean success = databaseHelper.addUser(user);

            if (success) {
                Toast.makeText(SignupActivity.this, "Account created", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(SignupActivity.this, "Error creating account", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }
}