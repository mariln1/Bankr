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
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
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
        balance.setText("$" + String.format("%,.2f", databaseHelper.getBalance(username)));

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
        String amountToWithdrawStr = amount.getText().toString().replaceAll("[^\\d.]", "");
        if (amountToWithdrawStr.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter an amount to withdraw", Toast.LENGTH_SHORT).show();
            return;
        }
        BigDecimal amountToWithdraw = new BigDecimal(amountToWithdrawStr);

        BigDecimal old =  databaseHelper.getBalance(user);
        if(old.compareTo(amountToWithdraw) == -1) {
            Toast.makeText(MainActivity.this, "Overdraft, please enter less to withdraw", Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal newAmount = old.subtract(amountToWithdraw);
        System.out.println(newAmount);
        databaseHelper.updateUser(user, newAmount);
        BigDecimal b = databaseHelper.getBalance(user);
        balance.setText("$" + String.format("%,.2f", databaseHelper.getBalance(user)));
        Toast.makeText(MainActivity.this, "Successful withdrawal", Toast.LENGTH_SHORT).show();

    }

    private void deposit() {

        String amountToDepositStr = amount.getText().toString().replaceAll("[^\\d.]", "");

        if (amountToDepositStr.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter an amount to withdraw", Toast.LENGTH_SHORT).show();
            return;
        }
        BigDecimal amountToDeposit = new BigDecimal(amountToDepositStr);

        BigDecimal old =  databaseHelper.getBalance(user);
        System.out.println(old);


        BigDecimal newAmount = old.add(amountToDeposit);
        System.out.println(newAmount);
        databaseHelper.updateUser(user, newAmount);
        balance.setText("$" + String.format("%,.2f", databaseHelper.getBalance(user)));
        Toast.makeText(MainActivity.this, "Successful deposit", Toast.LENGTH_SHORT).show();

    }

    private void logout() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        Toast.makeText(MainActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }


}