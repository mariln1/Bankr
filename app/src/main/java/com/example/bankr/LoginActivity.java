package com.example.bankr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        // neutralize inputs
        String usernameStr = username.getText().toString();
        Log.d("My Log", "Inputted username: " + usernameStr );
        String cleanUsername = usernameStr.replaceAll("[^a-z0-9_.\\-]", "");
        Log.d("My Log", "Clean username: " + cleanUsername);
        String passwordStr = password.getText().toString();

        DatabaseHelper databaseHelper = new DatabaseHelper(LoginActivity.this);
        String user = databaseHelper.checkUsernameAndPassword(usernameStr, passwordStr);
        if (!user.equals("")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("username", user);
            startActivity(intent);
        }
        else {
            Toast.makeText(LoginActivity.this, "Username and/or Password is incorrect", Toast.LENGTH_SHORT).show();
        }
    }
}