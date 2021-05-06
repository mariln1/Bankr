package com.example.bankr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    Button withdraw;
    Button deposit;
    Button logout;
    String user;
    String current = "";

    TextView welcomeMessage;
    TextView balance;
    TextView yourBalance;

    EditText amount;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(MainActivity.this);

        welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        Intent intent = getIntent();
        String username = intent.getExtras().getString("username");
        user = username;
        welcomeMessage.setText("Welcome " + username);

        balance = (TextView) findViewById(R.id.balance);
        balance.setText("$" + String.format("%.2f", databaseHelper.getBalance(username)));

        yourBalance = (TextView) findViewById(R.id.strYourBalance);

        amount = (EditText) findViewById(R.id.etAmount);
        amount.addTextChangedListener(new TextWatcher() {

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

        withdraw = findViewById(R.id.btnWithdraw);
        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { withdraw(); }
        });

        deposit = findViewById(R.id.btnDeposit);
        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { deposit(); }
        });

        logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { logout(); }
        });

    }

    private void withdraw() {
        //TODO: withdrawal functionality
        String newer = amount.getText().toString();
        String[] amt = newer.split("\\$");

        String old = String.format("%.02f", databaseHelper.getBalance(user));
        System.out.println(old);


        float newAmount = Float.valueOf(old) - Float.valueOf(amt[1]);

        System.out.println(newAmount);
        balance.setText("$" + newAmount);
        databaseHelper.updateUser(user, newAmount);
    }

    private void deposit() {

        String newer = amount.getText().toString();
        String[] amt = newer.split("\\$");

        String old = String.format("%.02f", databaseHelper.getBalance(user));
        System.out.println(old);


        float newAmount = Float.valueOf(old) + Float.valueOf(amt[1]);

        System.out.println(newAmount);
        balance.setText("$" + newAmount);
        databaseHelper.updateUser(user, newAmount);

    }

    private void logout() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }


}