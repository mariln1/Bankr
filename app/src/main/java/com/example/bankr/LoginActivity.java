package com.example.bankr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { connect(); }
        });

    }

    public void connect() {
        TextView username = (TextView) findViewById(R.id.etUsername);
        TextView password = (TextView) findViewById(R.id.etPassword);


        // TODO: neutralize inputs
        DatabaseHelper databaseHelper = new DatabaseHelper(LoginActivity.this);
        if (databaseHelper.checkUsernameAndPassword(username.getText().toString(), password.getText().toString())) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("username", username.getText().toString());
            startActivity(intent);
        }
        else {
            Toast.makeText(LoginActivity.this, "Username and/or Password is incorrect", Toast.LENGTH_SHORT).show();
        }
    }
}