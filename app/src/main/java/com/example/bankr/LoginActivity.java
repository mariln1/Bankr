package com.example.bankr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import net.sqlcipher.database.SQLiteDatabase;

/**
 * Login Screen of Bankr app
 */
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
        String cleanUsername = usernameStr.replaceAll("[^a-z0-9_.\\-]", "");
        String passwordStr = password.getText().toString();
        String cleanPassword = passwordStr.replaceAll("[^a-z0-9_.\\-]", "");

        SQLiteDatabase.loadLibs(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(LoginActivity.this);
        String user = databaseHelper.checkUsernameAndPassword(cleanUsername, cleanPassword);
        if (!user.equals("")) {
            Log.d("BANKR LOG", "Successful login as " + cleanUsername);
            Log.d("BANKR LOG", "Moving to main activity as " + cleanUsername);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("username", user);
            startActivity(intent);
        }
        else {
            Log.d("BANKR LOG", "Failed Login");
            Toast.makeText(LoginActivity.this, "Username and/or Password is incorrect", Toast.LENGTH_SHORT).show();
        }
    }
}